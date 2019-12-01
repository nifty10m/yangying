package de.xm.yangying.comparison

import spock.lang.Specification
import spock.lang.Unroll

class ArrayComparisonTest extends Specification {
  def "test file split"() {
    given:
      def cut = new ArrayComparison()
    expect:
      cut.afterRestore(bytes).size() == rowCount
    where:
      bytes                        || rowCount
      """ab,cd\n ef,gh""".bytes    || 2
      """1.0,1.1\n1.0,-2""".bytes  || 2
      """1.0,1.1\n\n24,23""".bytes || 2
  }

  def "test restore with limit"() {
    given:
      def cut = new ArrayComparison(ignoreValues: [7], minValue: 0, maxValue: 10)
    expect:
      def rest = cut.afterRestore(bytes)
      rest.size() == rowCount
    where:
      bytes                        || rowCount
      """1.0,1.1\n\n24,23""".bytes || 1
      """1.0,7\n\n24,23""".bytes   || 1
  }

  @Unroll
  def "Test creation of csv without exception"() {
    given:
      def cut = new ArrayComparison()
    expect:
      cut.toCsv(data)
    where:
      data                                                                           || result
      ["ab", "cd"]                                                                   || true
      ["ab", "cd"] as String[]                                                       || true
      [["ab", "cd"]]                                                                 || true
      [["ab", "cd"]]                                                                 || true
      [["ab", "cd"], ["ef", "hi"]]                                                   || true
      [[1, "cd"], [2, "hi"]]                                                         || true
      [[1.02390 as float, 1 as float], [Float.MAX_VALUE as float, 342.234 as float]] || true
      [3.14, 2.7182818284590]                                                        || true
  }
}
