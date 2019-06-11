package de.xm.yangying

import de.xm.yangying.comparison.PngComparisonTest
import spock.lang.Specification

class FileSnapshotsIntegrationTest extends Specification {

  static class Sample {
    String name
    int number
  }

  static class StreamSample {
    String name
    int number
    InputStream stream
    URL url
    double random
  }

  def "Check comparison of files"() {
    when:
      FileSnapshots.snapshot("ABC", Comparisons.BINARY) == FileSnapshots.current("ABC", Comparisons.BINARY)
    then:
      noExceptionThrown()
  }

  def "Check comparison of objects"() {
    given:
      def sample = new Sample(name: "Donald", number: 121)
    when:
      FileSnapshots.snapshot(sample, Comparisons.OBJECT_AS_JSON) == FileSnapshots.current(sample, Comparisons.OBJECT_AS_JSON)
    then:
      noExceptionThrown()
  }

  def "Check comparison of objects with assert"() {
    when:
      def sample = new Sample(name: "Mickey Mouse", number: 121)
    then:
      FileSnapshots.assertSnapshot(sample, Comparisons.OBJECT_AS_JSON)
  }

  def "Check comparison of stream"() {
    given:
      def sample = new StreamSample(name: "Donald",
        number: 121,
        stream: new ByteArrayInputStream("Hello World".bytes),
        url: new URL("http://www.google.com"),
        random: 2.7182818284590)
    when:
      def snapshot = FileSnapshots.snapshot(sample, Comparisons.OBJECT_AS_JSON)
      def current = FileSnapshots.current(sample, Comparisons.OBJECT_AS_JSON)
    then:
      snapshot == current
  }

  def "Multiple test in one method should be possible"() {
    when:
      def donald = new Sample(name: "Donald", number: 121)
      def mickey = new Sample(name: "Mickey", number: 133)
      def pluto = new Sample(name: "Pluto", number: 42)
    then:
      FileSnapshots.snapshot(donald, Comparisons.OBJECT_AS_JSON) == FileSnapshots.current(donald, Comparisons.OBJECT_AS_JSON)
      FileSnapshots.snapshot(mickey, Comparisons.OBJECT_AS_JSON) == FileSnapshots.current(mickey, Comparisons.OBJECT_AS_JSON)
      FileSnapshots.snapshot(pluto, Comparisons.OBJECT_AS_JSON) == FileSnapshots.current(pluto, Comparisons.OBJECT_AS_JSON)
  }

  def "Compare PNG files"() {
    when:
      def png = PngComparisonTest.getResourceAsStream("hikaku-logo.png").bytes
    then:
      FileSnapshots.snapshot(png, Comparisons.PNG) == FileSnapshots.current(png, Comparisons.PNG)
  }

  def "Compare XML files"() {
    when:
      def xmlString = "<note><to>Tove</to><from>Jani</from><heading>Reminder</heading><body>Don't forget me this weekend!</body></note>"

    then:
      FileSnapshots.snapshot(xmlString, Comparisons.XML) == FileSnapshots.current(xmlString, Comparisons.XML)
  }
}
