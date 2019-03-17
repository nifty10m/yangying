package de.xm.yangying.comparison

import spock.lang.Specification

class PngComparisonTest extends Specification {

  def "test beforeComparison"() {
    given:
      def png = PngComparisonTest.getResourceAsStream("hikaku-logo.png").bytes
    when:
      def comparison = new PngComparison()
    then:
      new PngComparison.Result(with: 1200, height: 450) == comparison.beforeComparison(png)
  }
}
