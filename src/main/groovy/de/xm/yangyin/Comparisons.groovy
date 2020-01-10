package de.xm.yangyin


import de.xm.yangyin.comparison.BinaryComparison
import de.xm.yangyin.comparison.JsonComparison
import de.xm.yangyin.comparison.PngComparison
import de.xm.yangyin.comparison.TextComparison
import de.xm.yangyin.comparison.TiffComparison
import de.xm.yangyin.comparison.XmlComparison
/**
 * Helper class with preconfigured comparison storages
 *
 * Keep in mind, no Comparison here is final, you can customize the Comparison in your application,
 * use this class only if you really know what you are doing.
 *
 */
class Comparisons {
  /**
   * Compare data base on PngComparison checking bytes as an PNG image
   */
  public static Comparison PNG = new PngComparison()

  /**
   * Storing data as JSON (using build in serialization) and compare result by excluding the given properties
   * @deprecated Use constructor and inject properties new JsonComparison(excludedProperties: excluding)
   */
  @Deprecated
  public static Comparison png(PngComparison.MODE comparisonMode) {
    return new PngComparison(comparisonMode)
  }

  /**
   * Compare data base on TiffComparison checking bytes as an TIFF image
   * @Deprecated Use BINARY instead
   */
  @Deprecated
  public static Comparison TIFF = new TiffComparison()

  /**
   * A preconfigured Json response excluding no attributes
   *
   * @Deprecated Use JSON instead
   */
  @Deprecated
  public static Comparison OBJECT_AS_JSON = new JsonComparison()

  /**
   * A preconfigured Json response excluding no attributes
   *
   */
  public static Comparison JSON = new JsonComparison()

  /**
   * A preconfigured Json response excluding lastModified, id and createdAt
   *
   */
  public static Comparison API_RESPONSE = new JsonComparison(excludedProperties: ["id", "createdAt", "lastModified"])

  /**
   * Comparing given data as byte[] on compare data byte by byte (without interpreting)
   */
  public static Comparison BINARY = new BinaryComparison("bin")

  /**
   * Comparing data as xml, ignoring whitespace and attribute order
   */
  public static Comparison XML = new XmlComparison()

  /**
   * Comparing data as txt, ignoring whitespace
   */
  public static Comparison TXT = new TextComparison()

  /**
   * Storing data as JSON (using build in serialization) and compare result by excluding the given properties
   * @deprecated Use constructor and inject properties new JsonComparison(excludedProperties: excluding)
   */
  @Deprecated
  static Comparison jsonExcludingProperties(String... excluding) {
    return new JsonComparison(excludedProperties: excluding)
  }

}
