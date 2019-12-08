package de.xm.yangying


import de.xm.yangying.environment.ContinousIntegration
import org.slf4j.Logger
import org.slf4j.LoggerFactory

import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

import static java.util.Locale.ENGLISH

class FileSnapshots {

  private static final Logger LOG = LoggerFactory.getLogger(FileSnapshots.class)

  private static String lastWrittenFeatureName
  private static int featuresWritten = 0

  static packageNameProvider = {
    return FeatureNameExtension.getPackageName()
  }
  static classNameProvider = {
    return FeatureNameExtension.getClassName()
  }
  static featureName = {
    return FeatureNameExtension.getFeatureName()
  }
  static updating = {
    "true".equalsIgnoreCase(System.getenv("SPOCK_UPDATE"))
  }

  static void assertSnapshot(def sample) {
    assertSnapshot(sample, new ComparisonDetector().detect(sample))
  }

  static void assertSnapshot(def sample, Comparison comparison) {
    def ying = snapshot(sample, comparison)
    def yang = current(sample, comparison)
    assert ying == yang
  }

  static def snapshot(def content, Comparison comparison) {
    Path packageDir = packageDir()
    if (!packageDir.toFile().exists()) {
      Files.createDirectories(packageDir)
    }

    def filename = featureName().toLowerCase(ENGLISH).replaceAll("[^a-z0-9]+", "-")
    if (lastWrittenFeatureName && lastWrittenFeatureName.startsWith(featureName())) {
      featuresWritten++
    } else {
      lastWrittenFeatureName = featureName()
      featuresWritten = 0
    }

    def extension = comparison.fileExtension()
    def resource = packageDir.resolve("${filename}${featuresWritten > 0 ? "-${featuresWritten}" : ""}.${extension}")

    def resourceFile = resource.toFile()
    if (updating()) {
      "Updating ${resourceFile.getPath()}"
      File file = new File(resourceFile.getPath())
      if (!file.exists()) {
        file.createNewFile()
      }
      resourceFile.bytes = comparison.beforeStore(content)
    } else if (!resourceFile.canRead() || resourceFile.bytes.length == 0) {
      if (ContinousIntegration.isCi()) {
        throw new FileNotFoundException(resourceFile.getPath())
      }
      LOG.debug "Creating ${resourceFile.getPath()}}"
      new File(resourceFile.getPath()).createNewFile()
      resourceFile.bytes = comparison.beforeStore(content)
    }
    def bytes = resourceFile.getBytes()
    def afterRestore = comparison.afterRestore(bytes)
    return comparison.beforeComparison(afterRestore)
  }

  static Path packageDir() {
    def packagePath = packageNameProvider().replaceAll("\\.", File.separator)
    def className = classNameProvider().replaceAll("(?<upper>[A-Z])", '-${upper}').toLowerCase().substring(1)

    def packageDir = Paths.get("src/test/resources/${packagePath}/${className}/snapshots")
    packageDir
  }

  static def current(def content, Comparison comparison) {
    return comparison.beforeComparison(content)
  }

}
