package de.xm.yangying.comparison

import de.xm.yangying.Comparison


class BinaryComparison implements Comparison {

  String extension

  public BinaryComparison(String extension) {
    this.extension = extension
  }

  @Override
  String fileExtension() {
    return extension
  }

  @Override
  def beforeComparison(Object original) {
    return original
  }

  @Override
  byte[] beforeStore(Object original) {
    return original;
  }

  @Override
  def afterRestore(byte[] fileContent) {
    return fileContent
  }
}
