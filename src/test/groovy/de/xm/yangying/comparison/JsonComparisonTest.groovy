package de.xm.yangying.comparison

import spock.lang.Specification


class JsonComparisonTest extends Specification {

  static class Sample {
    String name
    int number
  }
  def cut = new JsonComparison()

  def "Data should be be compared as map object"() {
    given:
      def data = new Sample(name: "Joe", number: 42)
    when:
      def json = cut.beforeComparison(data)
    then:
      json == [name: "Joe", number: 42]
  }

  def "Data should be be stored as json object"() {
    given:
      def data = new Sample(name: "Joe", number: 42)
    when:
      def json = cut.beforeStore(data)
    then:
      new String(json, "utf-8") == """{
    "name": "Joe",
    "number": 42
}"""
  }
}
