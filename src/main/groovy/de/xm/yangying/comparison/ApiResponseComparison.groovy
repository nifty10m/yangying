package de.xm.yangying.comparison

import de.xm.yangying.Comparison
import groovy.json.JsonGenerator
import groovy.json.JsonOutput
import groovy.json.JsonSlurper

import java.nio.charset.Charset

class ApiResponseComparison implements Comparison {

  private JsonGenerator JSON_GENERATOR = new JsonGenerator.Options()
    .excludeFieldsByName("id", "createdAt", "lastModified")
    .build()

  @Override
  String fileExtension() {
    return "json"
  }

  @Override
  def beforeComparison(def original) {
    def jsonString
    if (original instanceof CharSequence) {
      jsonString = toJson(new JsonSlurper().parseText(original.toString()))
    } else {
      jsonString = toJson(original)
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
      jsonString = toJson(original)
    }
    return JsonOutput.prettyPrint(jsonString).getBytes(Charset.forName("utf-8"))
  }

  private String toJson(original) {
    JSON_GENERATOR.toJson(original)
  }

  @Override
  def afterRestore(byte[] fileContent) {
    def lazyMap = new JsonSlurper().parseText(new String(fileContent))
    return lazyMap
  }

}
