package de.xm.yangying

import de.xm.yangying.comparison.ArrayComparison
import de.xm.yangying.comparison.BinaryComparison
import de.xm.yangying.comparison.JsonComparison
import de.xm.yangying.comparison.PngComparison
import de.xm.yangying.comparison.TextComparison
import de.xm.yangying.comparison.XmlComparison
import groovy.json.JsonException
import groovy.json.JsonSlurper

import java.nio.charset.StandardCharsets

public class ComparisonDetector {

  Comparison detect(def input) {
    if (input instanceof byte[]) {
      if (isPng(input)) {
        return new PngComparison()
      }
      return new BinaryComparison()
    }
    if (isArrayOrCollection(input)) {
      return new ArrayComparison()
    }
    if (input instanceof String) {
      if (isJson(input)) {
        return new JsonComparison();
      }
      if (isXml(input)) {
        return new XmlComparison();
      }
      if (isText(input.getBytes())) {
        return new TextComparison();
      }
    }
    return new JsonComparison()
  }

  boolean isJson(String json) {
    try {
      new JsonSlurper().parseText(json)
    } catch (JsonException je) {
      return false
    }
    return true
  }

  boolean isXml(String input) {
    try {
      new XmlSlurper().parseText(input)
    } catch (JsonException je) {
      return false
    }
    return true
  }

  boolean isPng(byte[] input) {
    byte[] magicHeader = [0x89, 0x50, 0x4e, 0x47, 0x0d, 0x0a, 0x1a, 0x0a] as byte[]
    byte[] start = Arrays.copyOf(input, 8)
    return Arrays.equals(start, magicHeader)
  }

  boolean isText(byte[] input) {
    String text = new String(input, StandardCharsets.UTF_8)
    return text.length() > 4 && text.substring(0, 4).matches(/[A-Za-z0-9]+/)
  }

  boolean isArrayOrCollection(def input) {
    ((input.getClass().isArray()) || (input instanceof Collection))
  }

}
