package de.xm.yangying

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.spockframework.runtime.extension.IGlobalExtension
import org.spockframework.runtime.extension.IMethodInterceptor
import org.spockframework.runtime.extension.IMethodInvocation
import org.spockframework.runtime.model.SpecInfo

import java.nio.file.Files
import java.nio.file.Paths

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
    spec.addCleanupSpecInterceptor(new IMethodInterceptor() {
      @Override
      void intercept(IMethodInvocation invocation) throws Throwable {
        def specs = classSpecifications.get() ?: []

        def files = FileSnapshots.packageDir().toFile().listFiles()
        def obsolete = files == null ? [] : files.toList().collect { it.getName() }
        obsolete.removeAll(specs)
        if (FileSnapshots.updating()) {
          obsolete.each { Files.delete(Paths.get("${FileSnapshots.packageDir().toFile()}/${it}")) }
          LOG.warn("Deleted {}", obsolete)
        }
      }
    })
  }

  void stop() {

  }


}
