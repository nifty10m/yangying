package de.xm.yangying.environment

import groovy.json.JsonSlurper
import groovy.transform.ToString

class ContinousIntegration {
  static Vendor[] vendors

  protected static Closure resolveEnvironmentVariable = { it && System.getenv(it) }

  static {
    vendors = new JsonSlurper().parse(ContinousIntegration.class.getResourceAsStream("vendors.json"), "utf-8") as Vendor[]
  }

  static boolean isCi() {
    vendors.any {
      resolveEnvironmentVariable.call(it.env)
    }
  }

  @ToString
  static class Vendor {
    String name
    String constant
    String env
    Object pr
  }
}
