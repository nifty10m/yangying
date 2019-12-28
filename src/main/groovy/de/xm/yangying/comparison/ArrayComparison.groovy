package de.xm.yangying.comparison

import de.xm.yangying.Comparison

import java.nio.charset.StandardCharsets
import java.util.stream.Collectors
import java.util.stream.Stream

class ArrayComparison implements Comparison {

  BigDecimal minValue = BigDecimal.valueOf(Integer.MIN_VALUE)
  BigDecimal maxValue = BigDecimal.valueOf(Integer.MAX_VALUE)
  List ignoreValues = []
  String columnSeparator = ","
  int rounding = 4

  @Override
  String fileExtension() {
    return "csv"
  }

  @Override
  def beforeComparison(Object original) {
    def mapped = createStream(original)
      .map({ row ->
        createStream(row)
          .map({ column -> column instanceof Float ? new BigDecimal(column) : column })
          .map({ column -> column instanceof Double ? new BigDecimal(column) : column })
          .map({ column -> column instanceof BigDecimal ? column.round(rounding) : column })
          .collect(Collectors.toList())
      })
      .collect(Collectors.toList())
    return mapped
  }

  @Override
  byte[] beforeStore(Object original) {
    toCsv(original).getBytes(StandardCharsets.UTF_8)
  }

  @Override
  def afterRestore(byte[] fileContent) {
    String file = new String(fileContent, StandardCharsets.UTF_8)
    def lines = []
    file.readLines()
      .findAll { line -> !line.isAllWhitespace() }
      .eachWithIndex { String line, int lineNo ->
        lines << line.trim().split(columnSeparator)
          .collect { column ->
            if (column.isBigDecimal()) {
              def value = new BigDecimal(column.trim()).round(rounding)
              return value >= minValue && value <= maxValue ? value : ""
            }
            return column
          }
          .findAll { !ignoreValues.contains(it) }
      }
    return lines
      .findAll { line -> !isEmptyLine(line) }
      .collect { line -> line.size() == 1 ? line.first() : line }
  }

  private boolean isEmptyLine(Collection line) {
    line.findAll { it.toString().isEmpty() }.size() == line.size()
  }

  String toCsv(original) {
    String file = createStream(original)
      .map({
        createStream(it)
          .map({ it.toString() })
          .map({ String s -> s.replaceAll(columnSeparator, "") })
          .collect(Collectors.joining(columnSeparator))
      })
      .collect(Collectors.joining("\n"))
    return file
  }

  private Stream createStream(def original) {
    if (original instanceof Collection) {
      return ((Collection) original).stream()
    }
    if (original.getClass().isArray()) {
      return Arrays.stream(original)
    }
    return Stream.of(original)
  }

}
