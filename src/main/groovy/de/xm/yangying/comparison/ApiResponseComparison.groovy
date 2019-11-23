package de.xm.yangying.comparison

import de.xm.yangying.Comparison

/**
 * @deprecated Use JsonComparison instead
 */
class ApiResponseComparison extends ConfigureableResponseComparison implements Comparison {

  ApiResponseComparison() {
    super("id", "createdAt", "lastModified")
  }

}
