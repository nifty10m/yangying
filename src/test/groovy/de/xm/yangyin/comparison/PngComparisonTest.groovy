package de.xm.yangyin.comparison

import spock.lang.Specification

class PngComparisonTest extends Specification {

  def "test beforeComparison"() {
    given:
      def png = PngComparisonTest.getResourceAsStream("yin-yang.png").bytes
    when:
      def comparison = new PngComparison()
    then:
      new PngComparison.Result(with: 200, height: 200) == comparison.beforeComparison(png)
  }

  def "test beforeComparison by pixel"() {
    given:
      def png = PngComparisonTest.getResourceAsStream("yin-yang.png").bytes
    when:
      def comparison = PngComparison.withMode(PngComparison.MODE.PIXEL)
      def beforeComapre = comparison.beforeComparison(png)
    then:
      beforeComapre.length == 106668
  }
}
