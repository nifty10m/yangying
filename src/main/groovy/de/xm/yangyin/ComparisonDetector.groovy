package de.xm.yangyin

import de.xm.yangyin.comparison.ArrayComparison
import de.xm.yangyin.comparison.BinaryComparison
import de.xm.yangyin.comparison.JsonComparison
import de.xm.yangyin.comparison.PngComparison
import de.xm.yangyin.comparison.TextComparison
import de.xm.yangyin.comparison.XmlComparison
import groovy.json.JsonException
import groovy.json.JsonSlurper
import org.xml.sax.SAXException

import java.nio.charset.StandardCharsets

class ComparisonDetector {

  CanCompare[] compares = [
    new CanComparePng(),
    new CanCompareArray(),
    new CanCompareJson(),
    new CanCompareXml(),
    new CanCompareText()
  ]

  Comparison detect(def input) {
    def result = compares.collect { it.detect(input) }
      .findAll { it != null }
    return result.isEmpty() ? new JsonComparison() : result[0]
  }

  class CanCompareText implements CanCompare {
    @Override
    Comparison detect(def input) {
      if (input instanceof String) {
        if (isText(input.getBytes())) {
          return new TextComparison();
        }
      }
    }

    boolean isText(byte[] input) {
      String text = new String(input, StandardCharsets.UTF_8)
      return text.length() > 4 && text.substring(0, 4).matches(/[A-Za-z0-9]+/)
    }
  }

  class CanCompareXml implements CanCompare {

    def slurper = new XmlSlurper()

    @Override
    Comparison detect(def input) {
      if (input instanceof String) {
        if (isXml(input)) {
          return new XmlComparison();
        }
      }
      return null
    }

    private boolean isXml(String input) {
      try {
        slurper.parseText(input)
      } catch (SAXException je) {
        return false
      }
      return true
    }
  }

  static class CanCompareJson implements CanCompare {
    def slurper = new JsonSlurper()

    @Override
    Comparison detect(def input) {
      if (input instanceof String) {
        if (isJson(input)) {
          return new JsonComparison();
        }
      }
      return null
    }

    boolean isJson(String json) {
      try {
        slurper.parseText(json)
      } catch (JsonException je) {
        return false
      }
      return true
    }
  }

  static class CanCompareArray implements CanCompare {
    @Override
    Comparison detect(def input) {
      if (isArrayOrCollection(input)) {
        return new ArrayComparison()
      }
      return null
    }

    private boolean isArrayOrCollection(def input) {
      ((input.getClass().isArray()) || (input instanceof Collection))
    }
  }

  static class CanComparePng implements CanCompare {
    @Override
    Comparison detect(def input) {
      if (input instanceof byte[]) {
        if (isPng(input)) {
          return new PngComparison()
        }
        return new BinaryComparison()
      }
      return null
    }

    private boolean isPng(byte[] input) {
      byte[] magicHeader = [0x89, 0x50, 0x4e, 0x47, 0x0d, 0x0a, 0x1a, 0x0a] as byte[]
      byte[] start = Arrays.copyOf(input, 8)
      return Arrays.equals(start, magicHeader)
    }
  }


}
