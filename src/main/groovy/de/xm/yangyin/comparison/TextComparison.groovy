package de.xm.yangyin.comparison

import de.xm.yangyin.Comparison

import java.nio.charset.StandardCharsets

/**
 * Comparison to compare text content as plain text
 */
class TextComparison implements Comparison {

  boolean ignoreWhitespace = true
  boolean ignoreCase

  @Override
  String fileExtension() {
    return "txt"
  }

  @Override
  def beforeComparison(Object original) {
    def input = original.toString()
    def withoutWhitespace = ignoreWhitespace ? input.replaceAll("\\s+", "") : input
    def compare = ignoreCase ? withoutWhitespace.toLowerCase(Locale.ENGLISH) : withoutWhitespace
    return compare
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
