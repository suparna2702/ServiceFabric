package com.similan.service.api.asset.dto.basic;

import static com.google.common.base.Preconditions.checkArgument;

import java.util.HashMap;
import java.util.Map;

public enum AssetMediaType {

  UNKNOWN(AssetMediaMainType.UNKNOWN, "*"),

  // application types

  APPLICATION_UNKNOWN(AssetMediaMainType.APPLICATION, "*"),

  APPLICATION_ATOM(AssetMediaMainType.APPLICATION, "atom+xml"),

  APPLICATION_BZIP2(AssetMediaMainType.APPLICATION, "x-bzip2"),

  APPLICATION_FORM_DATA(AssetMediaMainType.APPLICATION, "x-www-form-urlencoded"),

  APPLICATION_GZIP(AssetMediaMainType.APPLICATION, "x-gzip"),

  APPLICATION_JAVASCRIPT(AssetMediaMainType.APPLICATION, "javascript"),

  APPLICATION_JSON(AssetMediaMainType.APPLICATION, "json"),

  APPLICATION_KML(AssetMediaMainType.APPLICATION, "vnd.google-earth.kml+xml"),

  APPLICATION_KMZ(AssetMediaMainType.APPLICATION, "vnd.google-earth.kmz"),

  APPLICATION_MS_EXCEL(AssetMediaMainType.APPLICATION, "vnd.ms-excel"),

  APPLICATION_MS_POWERPOINT(AssetMediaMainType.APPLICATION, "vnd.ms-powerpoint"),

  APPLICATION_MS_WORD(AssetMediaMainType.APPLICATION, "msword"),

  APPLICATION_MS_VISIO(AssetMediaMainType.APPLICATION, "vnd.visio"),

  APPLICATION_OCTET_STREAM(AssetMediaMainType.APPLICATION, "octet-stream",
      Precision.GENERIC_TYPE),

  APPLICATION_OGG(AssetMediaMainType.APPLICATION, "ogg",
      Precision.AMBIGUOUS_TYPE),

  APPLICATION_OOXML_DOCUMENT(AssetMediaMainType.APPLICATION,
      "vnd.openxmlformats-officedocument.wordprocessingml.document"),

  APPLICATION_OOXML_PRESENTATION(AssetMediaMainType.APPLICATION,
      "vnd.openxmlformats-officedocument.presentationml.presentation"),

  APPLICATION_OOXML_SHEET(AssetMediaMainType.APPLICATION,
      "vnd.openxmlformats-officedocument.spreadsheetml.sheet"),

  APPLICATION_OPENDOCUMENT_GRAPHICS(AssetMediaMainType.APPLICATION,
      "vnd.oasis.opendocument.graphics"),

  APPLICATION_OPENDOCUMENT_PRESENTATION(AssetMediaMainType.APPLICATION,
      "vnd.oasis.opendocument.presentation"),

  APPLICATION_OPENDOCUMENT_SPREADSHEET(AssetMediaMainType.APPLICATION,
      "vnd.oasis.opendocument.spreadsheet"),

  APPLICATION_OPENDOCUMENT_TEXT(AssetMediaMainType.APPLICATION,
      "vnd.oasis.opendocument.text"),

  APPLICATION_PDF(AssetMediaMainType.APPLICATION, "pdf"),

  APPLICATION_POSTSCRIPT(AssetMediaMainType.APPLICATION, "postscript"),

  APPLICATION_RTF(AssetMediaMainType.APPLICATION, "rtf"),

  APPLICATION_SHOCKWAVE_FLASH(AssetMediaMainType.APPLICATION,
      "x-shockwave-flash"),

  APPLICATION_TAR(AssetMediaMainType.APPLICATION, "x-tar"),

  APPLICATION_XHTML(AssetMediaMainType.APPLICATION, "xhtml+xml"),

  APPLICATION_XML(AssetMediaMainType.APPLICATION, "xml"),

  APPLICATION_ZIP(AssetMediaMainType.APPLICATION, "zip"),

  // audio types

  AUDIO_UNKNOWN(AssetMediaMainType.AUDIO, "*"),

  AUDIO_MP4(AssetMediaMainType.AUDIO, "mp4"),

  AUDIO_MPEG(AssetMediaMainType.AUDIO, "mpeg"),

  AUDIO_OGG(AssetMediaMainType.AUDIO, "ogg"),

  AUDIO_WEBM(AssetMediaMainType.AUDIO, "webm"),

  // image types

  IMAGE_UNKNOWN(AssetMediaMainType.IMAGE, "*"),

  IMAGE_GIF(AssetMediaMainType.IMAGE, "gif"),

  IMAGE_ICO(AssetMediaMainType.IMAGE, "vnd.microsoft.icon"),

  IMAGE_JPEG(AssetMediaMainType.IMAGE, "jpeg"),

  IMAGE_PNG(AssetMediaMainType.IMAGE, "png"),

  IMAGE_SVG(AssetMediaMainType.IMAGE, "svg+xml"),

  IMAGE_TIFF(AssetMediaMainType.IMAGE, "tiff"),

  // message types

  MESSAGE_UNKNOWN(AssetMediaMainType.MESSAGE, "*"),

  // model types

  MODEL_UNKNOWN(AssetMediaMainType.MODEL, "*"),

  // multipart types

  MULTIPART_UNKNOWN(AssetMediaMainType.MULTIPART, "*"),

  // text types

  TEXT_UNKNOWN(AssetMediaMainType.TEXT, "*"),

  TEXT_CACHE_MANIFEST(AssetMediaMainType.TEXT, "cache-manifest"),

  TEXT_CSS(AssetMediaMainType.TEXT, "css"),

  TEXT_CSV(AssetMediaMainType.TEXT, "csv"),

  TEXT_HTML(AssetMediaMainType.TEXT, "html"),

  TEXT_ICALENDAR(AssetMediaMainType.TEXT, "calendar"),

  TEXT_PLAIN(AssetMediaMainType.TEXT, "plain"),

  TEXT_JAVASCRIPT(AssetMediaMainType.TEXT, "javascript"),

  TEXT_VCARD(AssetMediaMainType.TEXT, "vcard"),

  TEXT_XML(AssetMediaMainType.TEXT, "xml"),

  // video types

  VIDEO_UNKNOWN(AssetMediaMainType.VIDEO, "*"),

  VIDEO_MP4(AssetMediaMainType.VIDEO, "mp4"),

  VIDEO_MPEG(AssetMediaMainType.VIDEO, "mpeg"),

  VIDEO_OGG(AssetMediaMainType.VIDEO, "ogg"),

  VIDEO_QUICKTIME(AssetMediaMainType.VIDEO, "quicktime"),

  VIDEO_WEBM(AssetMediaMainType.VIDEO, "webm"),

  VIDEO_WMV(AssetMediaMainType.VIDEO, "x-ms-wmv"),

  ;

  public enum Precision {
    NOTHING_KNOWN,

    GENERIC_TYPE,

    ONLY_MAIN_KNOWN,

    ONLY_SUBTYPE_KNOWN,

    AMBIGUOUS_TYPE,

    ALL_KNOWN, ;
  };

  private final AssetMediaMainType mainType;

  private final String subType;

  private final Precision precision;

  private static class StaticData {
    private static final Map<String, AssetMediaType> BY_DESCRIPTOR = new HashMap<String, AssetMediaType>();
  }

  private static Precision getPrecision(AssetMediaMainType mainType,
      String subType) {
    boolean mainTypeKnown = mainType != AssetMediaMainType.UNKNOWN;
    boolean subTypeKnown = !subType.equals("*");
    if (mainTypeKnown && subTypeKnown) {
      return Precision.ALL_KNOWN;
    } else if (!mainTypeKnown && subTypeKnown) {
      return Precision.ONLY_SUBTYPE_KNOWN;
    } else if (mainTypeKnown && !subTypeKnown) {
      return Precision.ONLY_MAIN_KNOWN;
    } else {
      return Precision.NOTHING_KNOWN;
    }
  }

  private AssetMediaType(AssetMediaMainType mainType, String subType) {
    this(mainType, subType, getPrecision(mainType, subType));
  }

  private AssetMediaType(AssetMediaMainType mainType, String subType,
      Precision precision) {
    this.mainType = mainType;
    this.subType = subType;
    this.precision = precision;
    String descriptor = getDescriptor();
    checkArgument(StaticData.BY_DESCRIPTOR.put(descriptor, this) == null,
        "Duplicate media type descriptor " + descriptor);

  }

  public AssetMediaMainType getMainType() {
    return mainType;
  }

  public String getSubType() {
    return subType;
  }

  public String getDescriptor() {
    return mainType.getDescriptor() + '/' + subType;
  }

  public Precision getPrecision() {
    return precision;
  }

  private static final String[] UNKNOWN_DESCRIPTOR = { "*", "*" };

  public static AssetMediaType find(String contentType) {
    String[] parsed = parse(contentType);
    AssetMediaType type;
    type = StaticData.BY_DESCRIPTOR.get(parsed[0] + '/' + parsed[1]);
    if (type != null) {
      return type;
    }
    type = StaticData.BY_DESCRIPTOR.get("*/" + parsed[1]);
    if (type != null) {
      return type;
    }
    type = StaticData.BY_DESCRIPTOR.get(parsed[0] + "/*");
    if (type != null) {
      return type;
    }
    return UNKNOWN;
  }

  private static String[] parse(String contentType) {
    if (contentType == null) {
      return UNKNOWN_DESCRIPTOR;
    }
    int semicolonIndex = contentType.indexOf(';');
    if (semicolonIndex != -1) {
      contentType = contentType.substring(0, semicolonIndex);
    }
    contentType = contentType.trim();
    if (contentType.isEmpty()) {
      return UNKNOWN_DESCRIPTOR;
    }
    contentType = contentType.toLowerCase();
    int slashIndex = contentType.indexOf('/');
    String mainType;
    String subType;
    if (slashIndex == -1) {
      mainType = contentType;
      subType = "*";
    } else {
      mainType = contentType.substring(0, slashIndex).trim();
      subType = contentType.substring(slashIndex + 1).trim();
    }
    if (mainType.isEmpty()) {
      mainType = "*";
    }
    if (subType.isEmpty()) {
      subType = "*";
    }
    return new String[] { mainType, subType };
  }

  public AssetMediaType best(AssetMediaType other) {
    return this.precision.ordinal() >= other.precision.ordinal() ? this : other;
  }

}