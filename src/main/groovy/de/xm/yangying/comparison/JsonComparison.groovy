package de.xm.yangying.comparison

import de.xm.yangying.Comparison
import groovy.json.JsonGenerator
import groovy.json.JsonOutput
import groovy.json.JsonSlurper

import java.nio.charset.StandardCharsets
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.Year
import java.time.format.DateTimeFormatter

class JsonComparison implements Comparison {

  private JsonGenerator JSON_GENERATOR;

  String[] excludedProperties = []
  String[] excludedTypes = []
  Map<Class, Closure> converter = [
    (Instant.class)      : { it -> it.toEpochMilli() },
    (Year.class)         : { it.getValue() },
    (LocalDate.class)    : { it.format(DateTimeFormatter.ISO_DATE) },
    (LocalDateTime.class): { it.format(DateTimeFormatter.ISO_DATE) }
  ]

  /**
   * Class should not be instanced directly use JsonComparison subclass
   */
  @Deprecated
  protected ConfigureableResponseComparison(String... excludedProperties) {
    this.excludedProperties = excludedProperties
  }

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
    return JsonOutput.prettyPrint(jsonString).getBytes(StandardCharsets.UTF_8)
  }

  private String toJson(original) {
    if (JSON_GENERATOR == null) {
      def options = new JsonGenerator.Options()
        .excludeFieldsByName(excludedProperties)
        .excludeFieldsByName(excludedTypes)
      converter.each({ options.addConverter(it.key, it.value) })
      JSON_GENERATOR = options.build()
    }
    JSON_GENERATOR.toJson(original)
  }

  @Override
  def afterRestore(byte[] fileContent) {
    def lazyMap = new JsonSlurper().parseText(new String(fileContent))
    return lazyMap
  }

}
