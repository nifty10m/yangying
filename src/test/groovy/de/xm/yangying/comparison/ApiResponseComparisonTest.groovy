package de.xm.yangying.comparison

import spock.lang.Specification


class ApiResponseComparisonTest extends Specification {

  def "Data should be be compared as map object"() {
    given:
      def comparison = new ApiResponseComparison()
      def data = """{"id": "${UUID.randomUUID()}","name":"Jörg Herbst", "number": 42}"""
    when:
      def json = comparison.beforeComparison(data)
    then:
      json == [name: "Jörg Herbst", number: 42]
  }


}
