package de.xm.yangyin.comparison

import spock.lang.Specification


class HtmlComparisonTest extends Specification {


  def "Data should be be compared without whitespace"() {
    given:
        def cut = new HtmlComparison()
    when:
        def txt = cut.beforeComparison("<p>Hallo Welt</p>")
    then:
        txt == "<p>Hallo Welt</p>"
  }

}
