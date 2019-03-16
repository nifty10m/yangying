package de.xm.yangying.comparison

import de.xm.yangying.Comparison
import groovy.json.JsonOutput
import groovy.json.JsonSlurper

import java.nio.charset.Charset

class JsonComparison implements Comparison {

  @Override
  String fileExtension() {
    return "json"
  }

  @Override
  def beforeComparison(def original) {
    def jsonString
    if (original instanceof String) {
      jsonString = original
    } else {
      jsonString = JsonOutput.toJson(original)
    }
    def lazyMap = new JsonSlurper().parseText(jsonString)
    return lazyMap
  }

  @Override
  byte[] beforeStore(Object original) {
    def jsonString
    if (original instanceof String) {
      jsonString = original
    } else {
      jsonString = JsonOutput.toJson(original)
    }
    return JsonOutput.prettyPrint(jsonString).getBytes(Charset.forName("utf-8"))
  }

  @Override
  def afterRestore(byte[] fileContent) {
    def lazyMap = new JsonSlurper().parseText(new String(fileContent))
    return lazyMap
  }


}
