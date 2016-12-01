package com.similan.service.impl.document.viewer.textdocument;

import static com.google.common.base.Preconditions.checkNotNull;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.List;

import javax.imageio.ImageIO;

import lombok.Cleanup;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.springframework.stereotype.Component;

import com.similan.service.api.asset.dto.basic.AssetInfoDto;
import com.similan.service.api.asset.dto.basic.AssetType;
import com.similan.service.impl.document.viewer.DocumentViewerComponentFactory;
import com.similan.service.internal.api.asset.io.TemporaryFile;

/**
 * A document viewer that extract thumbnails from a PDF's pages.
 * 
 * @author psaavedra
 */
@Component
public class PdfPdfBoxDocumentViewer extends AbstractTextDocumentViewer {
  public PdfPdfBoxDocumentViewer() {
    super("pdf-pdfbox");
  }

  @Override
  public void prepare(AssetInfoDto info, TemporaryFile file,
      DocumentViewerComponentFactory componentFactory) throws IOException {

    checkNotNull(info);
    checkNotNull(file);
    checkNotNull(componentFactory);

    PDDocument document = PDDocument.load(file.get());
    List<?> pages = document.getDocumentCatalog().getAllPages();
    for (Iterator<?> it = pages.iterator(); it.hasNext();) {
      PDPage page = (PDPage) it.next();
      BufferedImage image = page
          .convertToImage(BufferedImage.TYPE_INT_RGB, 320);
      // save the output
      AssetType assetType = AssetType.PNG;
      @Cleanup
      TemporaryFile pageFile = componentFactory.createTemporaryFile();
      @Cleanup
      OutputStream pageOutput = pageFile.openOutput();
      ImageIO.write(image, assetType.getDefaultExtension(), pageOutput);
      pageOutput.close();
      @Cleanup
      InputStream pageOnput = pageFile.openInput();
      componentFactory.createPage(assetType, pageOnput);
    }
  }

}
