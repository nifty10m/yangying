package de.xm.yangyin.comparison

import de.xm.yangyin.Comparison

@Deprecated
class ConfigureableResponseComparison extends JsonComparison implements Comparison {

  @Deprecated
  protected ConfigureableResponseComparison(String... excludedProperties) {
    this.excludedProperties = excludedProperties
  }
}
