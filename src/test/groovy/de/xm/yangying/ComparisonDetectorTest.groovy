package de.xm.yangying

import de.xm.yangying.comparison.ArrayComparison
import spock.lang.Specification
import spock.lang.Unroll


class ComparisonDetectorTest extends Specification {

  @Unroll
  def "test '#text' isJson == #expected"() {
    expect:
      def cut = new ComparisonDetector()
      cut.isJson(text) == expected
    where:
      text                                               || expected
      '{"abc":42}'                                       || true
      'Hallo Welt'                                       || false
      '{\n  "name": "Mickey Mouse",\n  "number": 122\n}' || true
  }

  @Unroll
  def "test '#text' is text == #expected"() {
    expect:
      def cut = new ComparisonDetector()
      cut.isText(text) == expected
    where:
      text               || expected
      '{"abc":42}'.bytes || false
      'Hallo Welt'.bytes || true
      '\t\tABC#*&'.bytes || false
  }

  @Unroll
  def "test '#text' is array == #expected "() {
    expect:
      def cut = new ComparisonDetector()
      cut.isArrayOrCollection(text) == expected
    where:
      text                     || expected
      [1, 2, 3]                || true
      ["ab", "ab"] as String[] || true
      'Hallo Welt'             || false
  }

  def "test detect comparison"() {
    given:
      def cut = new ComparisonDetector()
    when:
      def comparison = cut.detect([1, 2, 3])
    then:
      comparison instanceof ArrayComparison
  }


}
