package de.xm.yangying.comparison

import java.nio.charset.StandardCharsets

class TextComparison extends BinaryComparison {

  TextComparison() {
    super("txt")
  }

  @Override
  def beforeComparison(Object original) {
    return original.toString().replaceAll("\\s+", "")
  }

  @Override
  byte[] beforeStore(Object original) {
    return original.toString().getBytes(StandardCharsets.UTF_8);
  }

  @Override
  def afterRestore(byte[] fileContent) {
    return new String(fileContent, StandardCharsets.UTF_8)
  }
}
