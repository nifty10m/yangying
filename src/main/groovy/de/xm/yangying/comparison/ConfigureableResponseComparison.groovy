package de.xm.yangying.comparison

import de.xm.yangying.Comparison

@Deprecated
class ConfigureableResponseComparison extends JsonComparison implements Comparison {

  @Deprecated
  protected ConfigureableResponseComparison(String... excludedProperties) {
    this.excludedProperties = excludedProperties
  }
}
