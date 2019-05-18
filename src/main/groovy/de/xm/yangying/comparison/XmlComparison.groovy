package de.xm.yangying.comparison

import de.xm.yangying.Comparison
import groovy.xml.XmlUtil

import java.nio.charset.StandardCharsets

class XmlComparison implements Comparison {

  @Override
  String fileExtension() {
    return "xml"
  }

  @Override
  def beforeComparison(def xmlAsString) {
    return prettyPrintXml(xmlAsString)
  }

  @Override
  byte[] beforeStore(def content) {

    return prettyPrintXml(content).getBytes(StandardCharsets.UTF_8)

  }

  @Override
  def afterRestore(byte[] fileContent) {
    new String(fileContent, StandardCharsets.UTF_8)
  }

  private static String prettyPrintXml(String xml) {
    def xmlNode = new XmlSlurper().parseText(xml)
    return XmlUtil.serialize(xmlNode)
  }
}
