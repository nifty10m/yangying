package de.xm.yangying.environment

import spock.lang.Specification


class ContinousIntegrationTest extends Specification {

  def "test isCi should check for CI"() {
    setup:
      def before = ContinousIntegration.resolveEnvironmentVariable
      ContinousIntegration.resolveEnvironmentVariable = {
        it == "GITLAB_CI"
      }
    expect:
      ContinousIntegration.isCi()
    cleanup:
      ContinousIntegration.resolveEnvironmentVariable = before
  }

  def "test vendor list should be readable"() {
    expect:
      ContinousIntegration.vendors.size() >= 20
  }

}
