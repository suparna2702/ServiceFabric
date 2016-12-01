package com.similan.service.impl.document.util;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.imageio.ImageIO;

import com.similan.service.api.asset.dto.basic.AssetType;

public class ImageUtil {

  public enum Anchor {

    /**
     * Scales height to fit a fixed width, keeps ratio.
     */
    WIDTH {
      @Override
      protected int[] getSize(int originalWidth, int originalHeight,
          int outputWidth, int outputHeight) {
        double scale = (double) outputWidth / originalWidth;
        int height = (int) (outputHeight * scale);
        return new int[] { outputWidth, height };
      }
    },

    /**
     * Scales width to fit a fixed height, keeps ratio.
     */
    HEIGHT {
      @Override
      protected int[] getSize(int originalWidth, int originalHeight,
          int outputWidth, int outputHeight) {
        double scale = (double) outputHeight / originalHeight;
        int width = (int) (originalWidth * scale);
        return new int[] { width, outputHeight };
      }
    },

    /**
     * Stretches to fit fixed width and height, does not keep ratio.
     */
    BOTH {
      @Override
      protected int[] getSize(int originalWidth, int originalHeight,
          int outputWidth, int outputHeight) {
        return new int[] { outputWidth, outputHeight };
      }
    },

    /**
     * Scales either width or height so that the other one does not exceed the
     * output, expands if necessary, keeps ratio.
     */
    LARGEST {
      @Override
      protected int[] getSize(int originalWidth, int originalHeight,
          int outputWidth, int outputHeight) {
        double widthScale = (double) outputWidth / originalWidth;
        double heightScale = (double) outputHeight / originalHeight;
        double scale = Math.min(widthScale, heightScale);
        int width = (int) (originalWidth * scale);
        int height = (int) (originalHeight * scale);
        return new int[] { width, height };
      }
    },

    /**
     * Same as LARGEST, but does not expand if smaller, keeps ratio
     */
    NONE {
      @Override
      protected int[] getSize(int originalWidth, int originalHeight,
          int outputWidth, int outputHeight) {
        double widthScale = (double) outputWidth / originalWidth;
        double heightScale = (double) outputHeight / originalHeight;
        double scale = Math.min(widthScale, heightScale);
        int width;
        int height;
        if (scale >= 1) {
          width = originalWidth;
          height = originalHeight;
        } else {
          width = (int) (originalWidth * scale);
          height = (int) (originalHeight * scale);
        }
        return new int[] { width, height };
      }
    },

    ;

    protected abstract int[] getSize(int originalWidth, int originalHeight,
        int outputWidth, int outputHeight);
  }

  public static void resize(InputStream input, Anchor anchor, int outputWidth,
      int outputHeight, OutputStream output, AssetType outputType)
      throws IOException {
    String formatName = outputType.getDefaultExtension();

    BufferedImage originalImage = ImageIO.read(input);

    int type = originalImage.getType() == 0 ? BufferedImage.TYPE_INT_ARGB
        : originalImage.getType();

    int[] size = anchor.getSize(originalImage.getWidth(),
        originalImage.getHeight(), outputWidth, outputHeight);
    int width = size[0];
    int height = size[1];

    BufferedImage resizedImage = new BufferedImage(width, height, type);
    Graphics2D graphics = resizedImage.createGraphics();

    graphics.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
        RenderingHints.VALUE_INTERPOLATION_BILINEAR);
    graphics.setRenderingHint(RenderingHints.KEY_RENDERING,
        RenderingHints.VALUE_RENDER_QUALITY);
    graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
        RenderingHints.VALUE_ANTIALIAS_ON);

    graphics.drawImage(originalImage, 0, 0, width, height, null);
    graphics.dispose();
    graphics.setComposite(AlphaComposite.Src);

    ImageIO.write(resizedImage, formatName, output);

  }
}
