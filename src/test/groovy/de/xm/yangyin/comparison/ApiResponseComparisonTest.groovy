package de.xm.yangyin.comparison

import spock.lang.Specification

import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.Year


class ApiResponseComparisonTest extends Specification {

  static class Sample {
    String name
    int number
    LocalDate day
    LocalDateTime time
    Instant moment
    Year yearOfTheLord
  }

  def "Object should be compared including date and times as map object"() {
    given:
      def comparison = new ApiResponseComparison()
      def data = new Sample(name: "Joe",
        number: 42,
        yearOfTheLord: Year.of(2012),
        day: LocalDate.of(2019, 1, 1),
        moment: Instant.ofEpochSecond(10000))
    when:
      def json = comparison.beforeComparison(data)
    then:
      json == [day: "2019-01-01", time: null, moment: 10000000, name: "Joe", number: 42, yearOfTheLord: 2012]
  }

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
