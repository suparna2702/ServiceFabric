package com.similan.service.impl.document.viewer.presentation;

import static com.google.common.base.Preconditions.checkNotNull;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.imageio.ImageIO;

import lombok.Cleanup;

import org.apache.poi.xslf.usermodel.XMLSlideShow;
import org.apache.poi.xslf.usermodel.XSLFSlide;
import org.springframework.stereotype.Component;

import com.similan.service.api.asset.dto.basic.AssetInfoDto;
import com.similan.service.api.asset.dto.basic.AssetType;
import com.similan.service.impl.document.viewer.DocumentViewerComponentFactory;
import com.similan.service.internal.api.asset.io.TemporaryFile;

@Component
public class OoxmlPresentationPoiDocumentViewer extends
    AbstractPresentationDocumentViewer {

  public OoxmlPresentationPoiDocumentViewer() {
    super("ooxml-presentation-poi");
  }

  @Override
  public void prepare(AssetInfoDto info, TemporaryFile file,
      DocumentViewerComponentFactory componentFactory) throws IOException {
    checkNotNull(info);
    checkNotNull(file);
    checkNotNull(componentFactory);

    @Cleanup
    InputStream input = file.openInput();
    XMLSlideShow ppt = new XMLSlideShow(input);
    input.close();

    Dimension pgsize = ppt.getPageSize();

    XSLFSlide[] slides = ppt.getSlides();

    for (int i = 0; i < slides.length; i++) {
      XSLFSlide slide = slides[i];
      BufferedImage image = new BufferedImage(pgsize.width, pgsize.height, 1);

      Graphics2D graphics = image.createGraphics();
      graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
          RenderingHints.VALUE_ANTIALIAS_ON);
      graphics.setRenderingHint(RenderingHints.KEY_RENDERING,
          RenderingHints.VALUE_RENDER_QUALITY);
      graphics.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
          RenderingHints.VALUE_INTERPOLATION_BICUBIC);
      graphics.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS,
          RenderingHints.VALUE_FRACTIONALMETRICS_ON);

      graphics.setColor(Color.white);
      graphics.clearRect(0, 0, pgsize.width, pgsize.height);
      graphics.fill(new Rectangle2D.Float(0, 0, pgsize.width, pgsize.height));

      // render
      slide.draw(graphics);

      // save the output
      AssetType assetType = AssetType.PNG;
      @Cleanup
      TemporaryFile pageFile = componentFactory.createTemporaryFile();
      @Cleanup
      OutputStream pageOutput = pageFile.openOutput();
      ImageIO.write(image, assetType.getDefaultExtension(), pageOutput);
      pageOutput.close();
      @Cleanup
      InputStream pageInput = pageFile.openInput();
      componentFactory.createPage(assetType, pageInput);
    }
  }
}