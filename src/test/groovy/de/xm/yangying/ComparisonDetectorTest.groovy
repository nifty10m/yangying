package de.xm.yangying

import de.xm.yangying.comparison.ArrayComparison
import de.xm.yangying.comparison.JsonComparison
import de.xm.yangying.comparison.TextComparison
import spock.lang.Specification
import spock.lang.Unroll


class ComparisonDetectorTest extends Specification {

  @Unroll
  def "test '#text' isJson == #expected"() {
    expect:
      def cut = new CanCompareJson()
      expected == cut.detect(text) instanceof JsonComparison
    where:
      text                                               || expected
      '{"abc":42}'                                       || true
      'Hallo Welt'                                       || false
      '{\n  "name": "Mickey Mouse",\n  "number": 122\n}' || true
  }

  @Unroll
  def "test '#text' is text == #expected"() {
    expect:
      def cut = new CanCompareText()
      cut.detect(text) instanceof TextComparison == expected
    where:
      text         || expected
      '{"abc":42}' || false
      'Hallo Welt' || true
      '\t\tABC#*&' || false
  }

  @Unroll
  def "test '#text' is array == #expected "() {
    expect:
      def cut = new CanCompareArray()
      cut.detect(text) instanceof ArrayComparison == expected
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
      def comparison = cut.detect([[1, 2, 3]])
    then:
      comparison instanceof ArrayComparison
  }

}
