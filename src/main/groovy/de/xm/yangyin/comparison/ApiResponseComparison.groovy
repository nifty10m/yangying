package de.xm.yangyin.comparison

import de.xm.yangyin.Comparison

/**
 * @deprecated Use JsonComparison instead
 */
class ApiResponseComparison extends ConfigureableResponseComparison implements Comparison {

  ApiResponseComparison() {
    super("id", "createdAt", "lastModified")
  }

}
