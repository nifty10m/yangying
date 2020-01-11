package de.xm.yangyin.comparison
/**
 * Comparison to compare HTML content ignoring case and whitespace
 */
class HtmlComparison extends TextComparison {

  HtmlComparison() {
    ignoreCase = false
    ignoreWhitespace = false
  }

  @Override
  String fileExtension() {
    return "html"
  }

}
