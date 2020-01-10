package de.xm.yangyin

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.spockframework.runtime.extension.IGlobalExtension
import org.spockframework.runtime.extension.IMethodInterceptor
import org.spockframework.runtime.extension.IMethodInvocation
import org.spockframework.runtime.model.SpecInfo

import java.nio.file.Files
import java.nio.file.Path

class FileCleanupExtension implements IGlobalExtension {

  private static final Logger LOG = LoggerFactory.getLogger(FileCleanupExtension.class)

  static ThreadLocal<List<String>> classSpecifications = new ThreadLocal<>()

  void start() {

  }

  static void addPackageFile(String filename) {
    classSpecifications.set((classSpecifications.get() ?: []) + filename)
  }

  @Override
  void visitSpec(SpecInfo spec) {
    spec.addCleanupSpecInterceptor(new CleanupMethodInterceptor())
  }

  void stop() {

  }

  static class CleanupMethodInterceptor implements IMethodInterceptor {
    @Override
    void intercept(IMethodInvocation invocation) throws Throwable {
      def specs = classSpecifications.get() ?: []
      def packageDir = FileSnapshots.packageDir()
      def updating = FileSnapshots.updating()

      cleanupFiles(specs, packageDir, updating)
    }

    protected void cleanupFiles(List<String> specs, Path packageDir, boolean updating) {
      if (updating) {
        def files = packageDir.toFile().listFiles()
        def obsolete = files == null ? [] : files.toList().collect { it.getName() }
        obsolete.removeAll(specs)
        obsolete.each { Files.delete(packageDir.resolve(it)) }
        LOG.warn("Deleted {}", obsolete)
      } else {
        LOG.debug("Not in UPDATE mode, no files are deleted")
      }
    }
  }

}
