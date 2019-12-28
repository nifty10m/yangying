package de.xm.yangyin

/**
 * Comparison is the central interface to compare snapshots
 *
 * @see FileSnapshots
 */
interface Comparison {

  /**
   * The file extension which will be used when creating a snapshot file
   *
   * The filename is created based on the specification method (omitting none asc chars) but the extension depends on the comparison
   *
   * @return The file extension
   */
  String fileExtension()

  /**
   * Callback method which is applied before comparison (the current object and the restored object)
   *
   * @param original The original object
   * @return The original object after applying the comparison preparations
   */
  def beforeComparison(def original)

  /**
   * Callback method to transfer the java object into a byte[] to be stored
   *
   * @param original The original java object
   * @return A byte[] representation of the object
   */
  byte[] beforeStore(def original)

  /**
   * Callback method which must be applied to convert the byte[] content from the file system to an java object
   *
   * @param fileContent as found in the persistent storage
   * @return The restored java object
   */
  def afterRestore(byte[] fileContent)
}
