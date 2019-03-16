package de.xm.yangying


interface Comparison {

  String fileExtension()

  def beforeComparison(def original)

  byte[] beforeStore(def original)

  def afterRestore(byte[] fileContent)
}