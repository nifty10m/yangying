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

  def "test beforeComparison by pixel"() {
    given:
      def png = PngComparisonTest.getResourceAsStream("hikaku-logo.png").bytes
    when:
      def comparison = PngComparison.withMode(PngComparison.MODE.PIXEL)
      def beforeComapre = comparison.beforeComparison(png)
    then:
      beforeComapre.length == 2880000
  }
}
