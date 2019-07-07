package de.xm.yangying.comparison

import de.xm.yangying.Comparison

class ApiResponseComparison extends ConfigureableResponseComparison implements Comparison {

  ApiResponseComparison() {
    super("id", "createdAt", "lastModified")
  }

}
