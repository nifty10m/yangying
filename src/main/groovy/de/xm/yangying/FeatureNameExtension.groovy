package de.xm.yangying

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.spockframework.runtime.extension.IGlobalExtension
import org.spockframework.runtime.extension.IMethodInterceptor
import org.spockframework.runtime.extension.IMethodInvocation
import org.spockframework.runtime.model.SpecInfo

import java.nio.file.Files
import java.nio.file.Paths

class FeatureNameExtension implements IGlobalExtension {

  private static final Logger LOG = LoggerFactory.getLogger(FeatureNameExtension.class)

  static ThreadLocal<String> featureNameContext = new ThreadLocal<>()
  static ThreadLocal<String> packageNameContext = new ThreadLocal<>()
  static ThreadLocal<String> classNameContext = new ThreadLocal<>()
  static ThreadLocal<List<String>> classSpecifications = new ThreadLocal<>()
  static ThreadLocal<List<String>> packageFiles = new ThreadLocal<>()

  static String getFeatureName() {
    return featureNameContext.get()
  }

  static String getPackageName() {
    return packageNameContext.get()
  }

  static String getClassName() {
    return classNameContext.get()
  }

  void start() {

  }

  static void addPackageFile(String filename) {
    classSpecifications.set((classSpecifications.get() ?: []) + filename)
  }

  @Override
  void visitSpec(SpecInfo spec) {
    spec.allFeatures*.featureMethod*.addInterceptor(new MethodNameInterceptor())
    spec.addSetupSpecInterceptor(new IMethodInterceptor() {
      @Override
      void intercept(IMethodInvocation invocation) throws Throwable {
        LOG.debug "Interception ${invocation.instance.getClass().getSimpleName()}"
        packageNameContext.set(invocation.getSpec().getPackage())
        classNameContext.set(invocation.getInstance().getClass().getSimpleName())
      }
    })
    spec.addCleanupSpecInterceptor(new IMethodInterceptor() {
      @Override
      void intercept(IMethodInvocation invocation) throws Throwable {
        def specs = classSpecifications.get()

        def files = FileSnapshots.packageDir().toFile().listFiles()
        def obsolete = files == null ? [] : files.toList().collect { it.getName() }
        obsolete.removeAll(specs)
        if (FileSnapshots.updating()) {
          obsolete.each { file ->
            Files.delete(Paths.get("${FileSnapshots.packageDir().toFile()}/${file}"))
          }
        }
        LOG.warn("Deleted {}", obsolete)
      }
    })
  }

  void stop() {

  }

  static class MethodNameInterceptor implements IMethodInterceptor {
    @Override
    void intercept(IMethodInvocation invocation) {
      def methodName = invocation.getMethod().getName()
      featureNameContext.set(methodName)
      invocation.proceed()
    }
  }
}
