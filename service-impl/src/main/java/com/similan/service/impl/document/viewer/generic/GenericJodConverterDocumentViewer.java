package com.similan.service.impl.document.viewer.generic;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkState;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ConnectException;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import lombok.Cleanup;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.artofsolving.jodconverter.DefaultDocumentFormatRegistry;
import com.artofsolving.jodconverter.DocumentFamily;
import com.artofsolving.jodconverter.DocumentFormat;
import com.artofsolving.jodconverter.openoffice.connection.OpenOfficeConnection;
import com.artofsolving.jodconverter.openoffice.connection.SocketOpenOfficeConnection;
import com.artofsolving.jodconverter.openoffice.converter.OpenOfficeDocumentConverter;
import com.similan.service.api.asset.dto.basic.AssetInfoDto;
import com.similan.service.api.asset.dto.basic.AssetMediaType;
import com.similan.service.api.asset.dto.basic.AssetType;
import com.similan.service.api.asset.dto.basic.AssetType.Precision;
import com.similan.service.impl.document.viewer.ChainedDocumentViewer;
import com.similan.service.impl.document.viewer.textdocument.PdfPdfBoxDocumentViewer;
import com.similan.service.internal.api.asset.io.TemporaryFile;
import com.similan.service.internal.impl.asset.AssetMediaTypeFinder;
import com.similan.service.internal.impl.asset.AssetTypeFinder;

/**
 * A document viewer that handles word like files (doc, docx, odt).
 * <p>
 * It does so by exporting the document to PDF and then generating thumbnails
 * for each of the PDF pages. It delegates the latter behavior to the
 * {@link PdfPdfBoxDocumentViewer}.
 * 
 * @author psaavedra
 */
@Slf4j
@Component
public class GenericJodConverterDocumentViewer extends ChainedDocumentViewer {

  private static class CustomDocumentFormatRegistry extends
      DefaultDocumentFormatRegistry {

    private final Map<AssetType, DocumentFormat> formatsByType = new HashMap<AssetType, DocumentFormat>();

    private final AssetTypeFinder typeFinder;
    private final AssetMediaTypeFinder mediaTypeFinder;

    public CustomDocumentFormatRegistry(AssetTypeFinder typeFinder,
        AssetMediaTypeFinder mediaTypeFinder) {
      this.typeFinder = typeFinder;
      this.mediaTypeFinder = mediaTypeFinder;

      // deal with superclass generated formats
      for (Object formatObject : getDocumentFormats()) {
        DocumentFormat format = (DocumentFormat) formatObject;
        registerFormat(format);
      }
      addDocumentFormat(createFormat("Microsoft Word 2007 XML",
          DocumentFamily.TEXT, AssetType.OOXML_DOCUMENT));
      addDocumentFormat(createFormat("Microsoft Excel 2007 XML",
          DocumentFamily.SPREADSHEET, AssetType.OOXML_SHEET));
      addDocumentFormat(createFormat("Microsoft Visio", DocumentFamily.DRAWING,
          AssetType.MS_VISIO));
    }

    private DocumentFormat createFormat(String name, DocumentFamily family,
        AssetType assetType) {
      DocumentFormat format = new DocumentFormat("Microsoft Word 2007 XML",
          DocumentFamily.TEXT, assetType.getDefaultMediaType().getDescriptor(),
          assetType.getDefaultExtension());
      return format;
    }

    @Override
    public void addDocumentFormat(DocumentFormat documentFormat) {
      super.addDocumentFormat(documentFormat);
      if (mediaTypeFinder != null) {
        registerFormat(documentFormat);
      }
    }

    private void registerFormat(DocumentFormat format) {
      String extension = format.getFileExtension();
      String filename = "any-name" + (extension == null ? "" : "." + extension);
      String contentType = format.getMimeType();
      AssetMediaType mediaType = mediaTypeFinder.find(contentType);
      AssetType type = typeFinder.find(mediaType, filename);
      if (type.getPrecision().ordinal() >= Precision.AMBIGUOUS.ordinal()) {
        DocumentFormat previousFormat = formatsByType.put(type, format);
        checkState(previousFormat == null,
            "Duplicate format for " + type.name() + " type: " + format
                + " and " + previousFormat);
      }
    }

    public DocumentFormat getFormatByType(AssetType type) {
      return formatsByType.get(type);
    }
  }

  @Autowired
  private AssetMediaTypeFinder mediaTypeFinder;
  @Autowired
  private AssetTypeFinder typeFinder;
  @Autowired
  private PdfPdfBoxDocumentViewer pdfDocumentViewer;
  
  private CustomDocumentFormatRegistry formatRegistry;
  private DocumentFormat outputFormat;
  private OpenOfficeDocumentConverter converter;

  
  public GenericJodConverterDocumentViewer() {
    super("generic-jodconverter", AssetType.PDF);
  }

  @PostConstruct
  public void initialize() {
    formatRegistry = new CustomDocumentFormatRegistry(typeFinder,
        mediaTypeFinder);
    outputFormat = formatRegistry.getFormatByType(AssetType.PDF);
    checkState(outputFormat != null, "Could not find format in registry for "
        + AssetType.PDF.name() + " type.");
    setChainedViewer(pdfDocumentViewer);
  }
  
  public synchronized void initializeConnection() throws ConnectException {
    if (converter != null) {
      return;
    }
    OpenOfficeConnection connection = new SocketOpenOfficeConnection();
    converter = new OpenOfficeDocumentConverter(connection, null);
  }

  @Override
  protected void prepareIntermediate(AssetInfoDto info, TemporaryFile file,  TemporaryFile intermediateFile) throws IOException {
    initializeConnection();
    log.info("Processing document {}", info.getFilename());
    AssetType type = info.getType();
    DocumentFormat inputFormat = formatRegistry.getFormatByType(type);
    checkArgument(inputFormat != null,
        "Could not find find format in registry for " + type.name() + " type");
    @Cleanup
    OutputStream intermediateOutput = intermediateFile.openOutput();
    @Cleanup
    InputStream input = file.openInput();
    converter.convert(input, inputFormat, intermediateOutput, outputFormat);
    log.info("Document processed successfully");
  }

}
