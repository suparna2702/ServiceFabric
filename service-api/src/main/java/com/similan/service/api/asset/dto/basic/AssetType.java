package com.similan.service.api.asset.dto.basic;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public enum AssetType {

  UNKNOWN($(AssetSort.UNKNOWN), $(AssetMediaType.UNKNOWN),
      Precision.NOTHING_KNOWN),

  UNKNOWN_TEXT($(AssetSort.TEXT_DOCUMENT), $(AssetMediaType.TEXT_UNKNOWN),
      Precision.ONLY_TYPE_KNOWN),

  UNKNOWN_IMAGE($(AssetSort.IMAGE), $(AssetMediaType.IMAGE_UNKNOWN),
      Precision.ONLY_TYPE_KNOWN),

  UNKNOWN_AUDIO($(AssetSort.AUDIO), $(AssetMediaType.AUDIO_UNKNOWN),
      Precision.ONLY_TYPE_KNOWN),

  UNKNOWN_VIDEO($(AssetSort.AUDIO), $(AssetMediaType.VIDEO_UNKNOWN),
      Precision.ONLY_TYPE_KNOWN),

  CACHE_MANIFEST($(AssetSort.MACHINE_CODE),
      $(AssetMediaType.TEXT_CACHE_MANIFEST), $("appcache")),

  CSS($(AssetSort.MACHINE_CODE), $(AssetMediaType.TEXT_CSS), $("css")),

  CSV($(AssetSort.SPREADSHEET), $(AssetMediaType.TEXT_CSV), $("csv")),

  HTML($(AssetSort.TEXT_DOCUMENT), $(AssetMediaType.TEXT_HTML),
      $("htm", "html")),

  ICALENDAR($(AssetSort.CALENDAR_INFO), $(AssetMediaType.TEXT_ICALENDAR), $(
      "ics", "ical", "ifb", "icalendar")),

  TEXT($(AssetSort.TEXT_DOCUMENT), $(AssetMediaType.TEXT_PLAIN), $("txt",
      "text")),

  LOG($(AssetSort.TEXT_DOCUMENT), $("log"), Precision.SPECIFIC),

  JAVASCRIPT($(AssetSort.MACHINE_CODE), $(
      AssetMediaType.APPLICATION_JAVASCRIPT, AssetMediaType.TEXT_JAVASCRIPT),
      $("js")),

  VCARD($(AssetSort.CONTACT_INFO), $(AssetMediaType.TEXT_VCARD), $("vcf",
      "vcard")),

  XML($(AssetSort.MACHINE_CODE), $(AssetMediaType.APPLICATION_XML,
      AssetMediaType.TEXT_XML), $("xml")),

  GIF($(AssetSort.IMAGE), $(AssetMediaType.IMAGE_GIF), $("gif")),

  ICO($(AssetSort.IMAGE), $(AssetMediaType.IMAGE_ICO), $("ico")),

  JPEG($(AssetSort.IMAGE), $(AssetMediaType.IMAGE_JPEG), $("jpg", "jpeg")),

  PNG($(AssetSort.IMAGE), $(AssetMediaType.IMAGE_PNG), $("png")),

  SVG($(AssetSort.IMAGE), $(AssetMediaType.IMAGE_PNG), $("png")),

  TIFF($(AssetSort.IMAGE), $(AssetMediaType.IMAGE_TIFF), $("tif", "tiff")),

  MP4_AUDIO($(AssetSort.AUDIO), $(AssetMediaType.AUDIO_MP4), $("m4a")),

  MP4_VIDEO($(AssetSort.VIDEO), $(AssetMediaType.VIDEO_MP4), $("m4v")),

  MP4($(AssetSort.AUDIO, AssetSort.VIDEO), $("mp4", "m4p", "m4b", "m4r"),
      Precision.AMBIGUOUS),

  MPEG_AUDIO($(AssetSort.AUDIO), $(AssetMediaType.AUDIO_MPEG), $("mp1", "mp2",
      "mp3", "m1a", "m2a", "mpa")),

  MPEG_VIDEO($(AssetSort.AUDIO), $(AssetMediaType.VIDEO_MPEG), $("m1v", "mpv")),

  MPEG($(AssetSort.AUDIO, AssetSort.VIDEO), $("mpg", "mpeg"),
      Precision.AMBIGUOUS),

  OGG_AUDIO($(AssetSort.AUDIO), $(AssetMediaType.AUDIO_OGG), $("oga")),

  OGG_VIDEO($(AssetSort.VIDEO), $(AssetMediaType.VIDEO_OGG), $("ogv")),

  OGG($(AssetSort.AUDIO, AssetSort.VIDEO), $(AssetMediaType.APPLICATION_OGG),
      $("ogg", "ogx", "spx", "opus"), Precision.AMBIGUOUS),

  QUICKTIME($(AssetSort.VIDEO), $(AssetMediaType.VIDEO_QUICKTIME), $("mov",
      "qt")),

  WEBM_AUDIO($(AssetSort.AUDIO), $(AssetMediaType.AUDIO_WEBM)),

  WEBM_VIDEO($(AssetSort.VIDEO), $(AssetMediaType.VIDEO_WEBM)),

  WEBM($(AssetSort.AUDIO, AssetSort.VIDEO), $("webm"), Precision.AMBIGUOUS),

  ATOM($(AssetSort.FEED), $(AssetMediaType.APPLICATION_ATOM), $("atom")),

  BZIP2($(AssetSort.ARCHIVE), $(AssetMediaType.APPLICATION_BZIP2), $("bz2")),

  FORM_DATA($(AssetSort.MACHINE_CODE), $(AssetMediaType.APPLICATION_FORM_DATA)),

  GZIP($(AssetSort.ARCHIVE), $(AssetMediaType.APPLICATION_GZIP), $("gz")),

  JSON($(AssetSort.MACHINE_CODE), $(AssetMediaType.APPLICATION_JSON), $("json")),

  KML($(AssetSort.GEOGRAPHIC), $(AssetMediaType.APPLICATION_KML), $("kml")),

  KMZ($(AssetSort.GEOGRAPHIC), $(AssetMediaType.APPLICATION_KMZ), $("kmz")),

  MS_EXCEL($(AssetSort.SPREADSHEET), $(AssetMediaType.APPLICATION_MS_EXCEL),
      $("xls")),

  MS_POWERPOINT($(AssetSort.PRESENTATION),
      $(AssetMediaType.APPLICATION_MS_POWERPOINT), $("ppt")),

  MS_WORD($(AssetSort.TEXT_DOCUMENT), $(AssetMediaType.APPLICATION_MS_WORD),
      $("doc")),

  MS_VISIO($(AssetSort.DIAGRAM), $(AssetMediaType.APPLICATION_MS_VISIO), $(
      "vsd", "vsdx")),

  OCTET_STREAM($(AssetSort.TEXT_DOCUMENT),
      $(AssetMediaType.APPLICATION_OCTET_STREAM), Precision.GENERIC),

  OOXML_DOCUMENT($(AssetSort.TEXT_DOCUMENT),
      $(AssetMediaType.APPLICATION_OOXML_DOCUMENT), $("docx", "docm")),

  OOXML_PRESENTATION($(AssetSort.PRESENTATION),
      $(AssetMediaType.APPLICATION_OOXML_PRESENTATION), $("pptx", "pptm")),

  OOXML_SHEET($(AssetSort.SPREADSHEET),
      $(AssetMediaType.APPLICATION_OOXML_SHEET), $("xlsx", "xlsm")),

  OPENDOCUMENT_GRAPHICS($(AssetSort.IMAGE),
      $(AssetMediaType.APPLICATION_OPENDOCUMENT_GRAPHICS), $("odg", "fodg")),

  OPENDOCUMENT_PRESENTATION($(AssetSort.PRESENTATION),
      $(AssetMediaType.APPLICATION_OPENDOCUMENT_PRESENTATION), $("odp", "fodp")),

  OPENDOCUMENT_SPREADSHEET($(AssetSort.SPREADSHEET),
      $(AssetMediaType.APPLICATION_OPENDOCUMENT_SPREADSHEET), $("ods", "fods")),

  OPENDOCUMENT_TEXT($(AssetSort.TEXT_DOCUMENT),
      $(AssetMediaType.APPLICATION_OPENDOCUMENT_TEXT), $("odt", "fodt")),

  PDF($(AssetSort.TEXT_DOCUMENT), $(AssetMediaType.APPLICATION_PDF), $("pdf")),

  POSTSCRIPT($(AssetSort.TEXT_DOCUMENT),
      $(AssetMediaType.APPLICATION_POSTSCRIPT), $("ps")),

  RTF($(AssetSort.TEXT_DOCUMENT), $(AssetMediaType.APPLICATION_RTF), $("rtf")),

  SHOCKWAVE_FLASH($(AssetSort.INTERACTIVE),
      $(AssetMediaType.APPLICATION_SHOCKWAVE_FLASH), $("swf")),

  TAR($(AssetSort.ARCHIVE), $(AssetMediaType.APPLICATION_TAR), $("tar")),

  XHTML($(AssetSort.TEXT_DOCUMENT), $(AssetMediaType.APPLICATION_XHTML), $(
      "xht", "xhtml")),

  ZIP($(AssetSort.ARCHIVE), $(AssetMediaType.APPLICATION_ZIP), $("zip", "zipx")),

  ;

  public enum Precision {
    NOTHING_KNOWN,

    GENERIC,

    ONLY_TYPE_KNOWN,

    AMBIGUOUS,

    WELL_KNOWN,

    SPECIFIC,

    ;
  }

  @SafeVarargs
  private static <E> E[] $(E... elements) {
    return elements;
  }

  private final AssetSort[] sorts;

  private final AssetMediaType[] mediaTypes;

  private final String[] extensions;

  private final String[] filenames;

  private final Precision precision;

  private static class StaticData {

    private static final Map<AssetSort, List<AssetType>> BY_SORT = new HashMap<AssetSort, List<AssetType>>();
    private static final Map<AssetMediaType, List<AssetType>> BY_MEDIA_TYPE = new HashMap<AssetMediaType, List<AssetType>>();
    private static final Map<String, List<AssetType>> BY_EXTENSION = new HashMap<String, List<AssetType>>();
    private static final Map<String, List<AssetType>> BY_FILENAME = new HashMap<String, List<AssetType>>();
  }

  private AssetType(AssetSort[] sorts, AssetMediaType[] mediaTypes) {
    this(sorts, mediaTypes, new String[0], Precision.WELL_KNOWN);
  }

  private AssetType(AssetSort[] sorts, AssetMediaType[] mediaTypes,
      Precision precision) {
    this(sorts, mediaTypes, new String[0], precision);
  }

  private AssetType(AssetSort[] sorts, AssetMediaType[] mediaTypes,
      String[] extensions) {
    this(sorts, mediaTypes, extensions, Precision.WELL_KNOWN);
  }

  private AssetType(AssetSort[] sorts, String[] extensions) {
    this(sorts, extensions, Precision.WELL_KNOWN);
  }

  private AssetType(AssetSort[] sorts, String[] extensions, Precision precision) {
    this(sorts, new AssetMediaType[0], extensions, precision);
  }

  private AssetType(AssetSort[] sorts, AssetMediaType[] mediaTypes,
      String[] extensions, Precision precision) {
    this(sorts, mediaTypes, extensions, new String[0], precision);
  }

  private AssetType(AssetSort[] sorts, AssetMediaType[] mediaTypes,
      String[] extensions, String[] filenames) {
    this(sorts, mediaTypes, extensions, filenames, Precision.WELL_KNOWN);
  }

  private AssetType(AssetSort[] sorts, AssetMediaType[] mediaTypes,
      String[] extensions, String[] filenames, Precision precision) {
    this.sorts = sorts;
    this.mediaTypes = mediaTypes;
    this.extensions = extensions;
    this.filenames = filenames;
    this.precision = precision;
    register(StaticData.BY_SORT, sorts, this);
    register(StaticData.BY_MEDIA_TYPE, mediaTypes, this);
    register(StaticData.BY_EXTENSION, extensions, this);
    register(StaticData.BY_FILENAME, filenames, this);
  }

  private static <E> void register(Map<E, List<AssetType>> registry, E[] keys,
      AssetType type) {
    for (E key : keys) {
      List<AssetType> types = registry.get(key);
      if (types == null) {
        types = new LinkedList<AssetType>();
        registry.put(key, types);
      }
      types.add(type);
    }
  }

  public static AssetType find(AssetMediaType mediaType, String filename) {
    String extension = getExtension(filename);
    filename = filename == null ? "" : filename.toLowerCase().trim();

    List<AssetType> typesByMediaType = StaticData.BY_MEDIA_TYPE.get(mediaType);
    List<AssetType> typesByExtension = StaticData.BY_EXTENSION.get(extension);
    List<AssetType> typesByFilename = StaticData.BY_EXTENSION.get(filename);

    AssetType best = AssetType.UNKNOWN;
    best = best(best, typesByMediaType);
    best = best(best, typesByExtension);
    best = best(best, typesByFilename);
    return best;
  }

  private static AssetType best(AssetType best, List<AssetType> others) {
    if (others == null) {
      return best;
    }
    for (AssetType other : others) {
      best = best.best(other);
    }
    return best;
  }

  public AssetType best(AssetType other) {
    return this.precision.ordinal() >= other.precision.ordinal() ? this : other;
  }

  public Precision getPrecision() {
    return precision;
  }

  private static String getExtension(String filename) {
    if (filename == null) {
      return "";
    }
    int dotIndex = filename.lastIndexOf('.');
    String extension;
    if (dotIndex == -1) {
      extension = "";
    } else {
      extension = filename.substring(dotIndex + 1).trim().toLowerCase();
    }
    return extension;
  }

  public boolean isOfSort(AssetSort sort) {
    for (AssetSort aSort : sorts) {
      if (aSort == sort) {
        return true;
      }
    }
    return false;
  }

  public boolean isValidMedaType(AssetMediaType mediaType) {
    for (AssetMediaType aMediatype : mediaTypes) {
      if (aMediatype == mediaType) {
        return true;
      }
    }
    return false;
  }

  public AssetSort[] getSorts() {
    return sorts.clone();
  }

  public AssetMediaType[] getMediaTypes() {
    return mediaTypes.clone();
  }

  public String[] getExtensions() {
    return extensions.clone();
  }

  public String[] getFilenames() {
    return filenames.clone();
  }

  public String getDefaultExtension() {
    if (extensions.length > 0) {
      return extensions[0];
    } else {
      return "";
    }
  }

  public String getDefaultFilename(String hint) {
    if (hint == null) {
      hint = "unnamed";
    }
    if (extensions.length > 0) {
      return hint + "." + extensions[0];
    } else if (filenames.length > 0) {
      return filenames[0];
    } else {
      return hint;
    }
  }

  public AssetMediaType getDefaultMediaType() {
    if (mediaTypes.length > 0) {
      return mediaTypes[0];
    } else {
      return AssetMediaType.UNKNOWN;
    }
  }

  public static AssetType[] getForSort(AssetSort sort) {
    List<AssetType> forSort = StaticData.BY_SORT.get(sort);
    if (forSort == null) {
      return new AssetType[0];
    }
    return forSort.toArray(new AssetType[forSort.size()]);
  }

}