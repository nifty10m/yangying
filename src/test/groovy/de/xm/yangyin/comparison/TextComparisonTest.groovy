package de.xm.yangyin.comparison

import spock.lang.Specification


class TextComparisonTest extends Specification {


  def "Data should be be compared without whitespace"() {
    given:
      def cut = new TextComparison()
    when:
      def txt = cut.beforeComparison("Hallo Welt")
    then:
      txt == "HalloWelt"
  }

  def "Data should be be compared with whitespace"() {
    given:
      def cut = new TextComparison(ignoreWhitespace: false)
    when:
      def txt = cut.beforeComparison("Hallo Welt")
    then:
      txt == "Hallo Welt"
  }

  def "Data should be be compared ignoring case"() {
    given:
      def cut = new TextComparison(ignoreWhitespace: false, ignoreCase: true)
    when:
      def txt = cut.beforeComparison("Hallo Welt")
    then:
      txt == "hallo welt"
  }
}
