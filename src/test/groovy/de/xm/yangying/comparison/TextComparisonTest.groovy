package de.xm.yangying.comparison

import spock.lang.Specification


class TextComparisonTest extends Specification {

  def cut = new TextComparison()

  def "Data should be be compared without whitespace"() {
    when:
      def txt = cut.beforeComparison("Hallo Welt")
    then:
      txt == "HalloWelt"
  }
}
