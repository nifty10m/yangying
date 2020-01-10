package de.xm.yangyin


import spock.lang.Specification

import java.nio.file.Path
import java.nio.file.Paths

class FileCleanupExtensionTest extends Specification {

  def "Check cleanup of obsolete files"() {
    given:
      new File("obsolete").mkdir()
      File obsolete = new File("obsolete/dummy.txt")
      obsolete.createNewFile()
      obsolete << "Hallo Welt"
      File keep = new File("obsolete/keep.txt")
      keep.createNewFile()
      keep << "Hallo Welt"

      keep.deleteOnExit()
      Path packageDir = Paths.get(obsolete.getAbsolutePath()).getParent()

      def cut = new FileCleanupExtension.CleanupMethodInterceptor()
    when:
      cut.cleanupFiles(["keep.txt"], packageDir, true)
    then:
      !obsolete.exists()
      keep.exists()
  }

  def "Keep file if not updating"() {
    given:
      File keepdir = new File("keepdir")
      keepdir.mkdir()
      File keep = new File("keepdir/keep.txt")
      keep.createNewFile()
      keep << "Hallo Welt"
      Path packageDir = Paths.get(keep.getAbsolutePath()).getParent()

      keep.deleteOnExit()
      keepdir.deleteOnExit()

      def cut = new FileCleanupExtension.CleanupMethodInterceptor()
    when:
      cut.cleanupFiles([], packageDir, false)
    then:
      keep.exists()
  }
}
