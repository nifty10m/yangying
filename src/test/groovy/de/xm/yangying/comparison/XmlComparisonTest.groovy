package de.xm.yangying.comparison

import groovy.xml.XmlUtil
import spock.lang.Specification

class XmlComparisonTest extends Specification {

  def "should compare and format XML"() {
    given:
      def sampleXml = "<note><to>Tove</to><from>Jani</from><heading>Reminder</heading><body>Don't forget me this weekend!</body></note>"

      def cut = new XmlComparison()

    when:
      def comparison = cut.beforeComparison(sampleXml)

    then:
      "<?xml version=\"1.0\" encoding=\"UTF-8\"?><note>\n" +
        "  <to>Tove</to>\n" +
        "  <from>Jani</from>\n" +
        "  <heading>Reminder</heading>\n" +
        "  <body>Don't forget me this weekend!</body>\n" +
        "</note>\n" == XmlUtil.serialize(comparison)
  }

  def "should compare and format XML ignoring Attribute Order"() {
    given:
      def sampleXml1 = "<sample foo='123' bar='456'>Dies ist ein Test</sample>"
      def sampleXml2 = "<sample bar='456' foo='123'>Dies ist ein Test</sample>"

      def cut = new XmlComparison()

    expect:
      cut.beforeComparison(sampleXml1) == cut.beforeComparison(sampleXml2)
  }

}
