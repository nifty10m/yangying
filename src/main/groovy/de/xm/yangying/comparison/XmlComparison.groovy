package de.xm.yangying.comparison

import de.xm.yangying.Comparison
import groovy.util.slurpersupport.GPathResult
import groovy.xml.XmlUtil

import java.nio.charset.StandardCharsets

class XmlComparison implements Comparison {

  @Override
  String fileExtension() {
    return "xml"
  }

  @Override
  def beforeComparison(def original) {
    String xmlString
    if (original instanceof String) {
      xmlString = original
    } else if (original instanceof GPathResult) {
      return original
    } else {
      throw new RuntimeException("XmlComparison input must provide XML as String, input was " + original.getClass())
    }
    def gPathResult = new XmlSlurper().parseText(xmlString)
    return gPathResult
  }

  @Override
  byte[] beforeStore(def content) {
    def xmlNode = new XmlSlurper().parseText(content)
    return XmlUtil.serialize(xmlNode).getBytes(StandardCharsets.UTF_8)
  }

  @Override
  def afterRestore(byte[] fileContent) {
    def gPathResult = new XmlSlurper().parseText(new String(fileContent, StandardCharsets.UTF_8))
    return gPathResult
  }

}
