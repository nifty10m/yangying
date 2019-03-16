package de.xm.yangying.comparison

import de.xm.yangying.Comparison
import groovy.json.JsonSlurper

class ApiResponseComparison extends JsonComparison implements Comparison {

  @Override
  def beforeComparison(def original) {
    def lazyMap = new JsonSlurper().parseText(original)
    removeUntrackedKeys(lazyMap)
    return lazyMap
  }

  private removeUntrackedKeys(def jsonObject) {
    if (jsonObject instanceof Map) {
      def map = (Map) (jsonObject)
      map.remove("id")
      map.remove("createdAt")
      map.remove("lastModified")
      map.values().each {
        if (it instanceof Map) {
          removeUntrackedKeys(it)
        }
      }
    }
  }
}
