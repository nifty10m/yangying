package de.xm.yangying

import de.xm.yangying.comparison.*

class Comparisons {
  public static Comparison PNG = new PngComparison()
  public static Comparison TIFF = new TiffComparison()
  public static Comparison OBJECT_AS_JSON = new JsonComparison()
  public static Comparison JSON = new JsonComparison()
  public static Comparison API_RESPONSE = new ApiResponseComparison()
  public static Comparison BINARY = new BinaryComparison("bin")
}
