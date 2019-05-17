package de.xm.yangying.comparison

import spock.lang.Specification

class XmlComparisionTest extends Specification {

  def "should compare and format XML"() {
    given:
      def sampleXml = "<note><to>Tove</to><from>Jani</from><heading>Reminder</heading><body>Don't forget me this weekend!</body></note>"

      def cut = new XmlComparision()

    when:
      def comparision = cut.beforeComparison(sampleXml)

    then:
      "<?xml version=\"1.0\" encoding=\"UTF-8\"?><note>\n" +
        "  <to>Tove</to>\n" +
        "  <from>Jani</from>\n" +
        "  <heading>Reminder</heading>\n" +
        "  <body>Don't forget me this weekend!</body>\n" +
        "</note>\n" == comparision
  }
}
