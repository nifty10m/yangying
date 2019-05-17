package de.xm.yangying.comparison

import de.xm.yangying.Comparison
import groovy.xml.XmlUtil

import java.nio.charset.Charset

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
    return prettyPrintXml(content).getBytes(Charset.forName("utf-8"))

  }

  @Override
  def afterRestore(byte[] fileContent) {
    new String(fileContent, Charset.forName("utf-8"))
  }

  private static String prettyPrintXml(String xml) {
    def xmlNode = new XmlSlurper().parseText(xml)
    return XmlUtil.serialize(xmlNode)
  }
}
