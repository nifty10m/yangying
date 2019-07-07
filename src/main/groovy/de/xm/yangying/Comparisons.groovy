package de.xm.yangying

import de.xm.yangying.comparison.*

/**
 * Central instance or factory methods for all yangying comparions
 */
class Comparisons {
  /**
   * Compare data base on PngComparison checking bytes as an PNG image
   */
  public static Comparison PNG = new PngComparison()

  /**
   * Compare data base on TiffComparison checking bytes as an TIFF image
   */
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
  public static Comparison JSON = new ConfigureableResponseComparison()

  /**
   * A preconfigured Json response excluding lastModified, id and createdAt
   *
   * @Deprecated Use jsonExcludingProperties to get an instance for an individual API
   */
  @Deprecated
  public static Comparison API_RESPONSE = new ApiResponseComparison()

  /**
   * Comparing given data as byte[] on compare data byte by byte (without interpreting)
   */
  public static Comparison BINARY = new BinaryComparison("bin")

  /**
   * Comparing data as xml, ignoring whitespace and attribute order
   */
  public static Comparison XML = new XmlComparison()

  /**
   * Storing data as JSON (using build in serialization) and compare result by excluding the given properties
   */
  static Comparison jsonExcludingProperties(String... excluding) {
    return new ConfigureableResponseComparison(excluding)
  }

}
