package de.xm.yangying.environment

import spock.lang.Specification


class ContinousIntegrationTest extends Specification {

  def "test isCi should check for CI"() {
    setup:
//      ContinousIntegration.resolveEnvironmentVariable = {
//        it == "GITLAB_CI"
//      }
    expect:
      ContinousIntegration.isCi()
  }

  def "test vendor list should be readable"() {
    expect:
      ContinousIntegration.vendors.size() >= 20
  }

}
