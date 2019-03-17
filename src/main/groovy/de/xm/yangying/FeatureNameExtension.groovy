package de.xm.yangying


import org.spockframework.runtime.extension.IGlobalExtension
import org.spockframework.runtime.extension.IMethodInterceptor
import org.spockframework.runtime.extension.IMethodInvocation
import org.spockframework.runtime.model.SpecInfo

class FeatureNameExtension implements IGlobalExtension {

  static ThreadLocal<String> featureNameContext = new ThreadLocal<>()
  static ThreadLocal<String> packageNameContext = new ThreadLocal<>()
  static ThreadLocal<String> classNameContext = new ThreadLocal<>()

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

  @Override
  void visitSpec(SpecInfo spec) {
    spec.allFeatures*.featureMethod*.addInterceptor(new MethodNameInterceptor())
    spec.addSetupSpecInterceptor(new IMethodInterceptor() {
      @Override
      void intercept(IMethodInvocation invocation) throws Throwable {
        println "Interception ${invocation.instance.getClass().getSimpleName()}"
        packageNameContext.set(invocation.getSpec().getPackage())
        classNameContext.set(invocation.getInstance().getClass().getSimpleName())
        if (FileSnapshots.updating()) {
          FileSnapshots.packageDir().toFile().listFiles().each {
            println it
            it.delete()
          }
        }
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