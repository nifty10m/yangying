package de.xm.yangyin

import de.xm.yangyin.environment.ContinousIntegration
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
  static updating = { "true".equalsIgnoreCase(System.getenv("SPOCK_UPDATE")) }

  /**
   * Check if current version is equals with SNAPSHOT version using detected resource from sample
   * @param sample
   */
  static void assertSnapshot(def sample) {
    assertSnapshot(sample, new ComparisonDetector().detect(sample))
  }

  static void assertSnapshot(def content, Comparison comparison) {
    Path resource = detectResource(comparison)
    def yin = readResource(resource, comparison)
    def yang = current(content, comparison)
    if ("" == yin || (yin != yang && updating())) {
      yin = upsertResource(content, resource, comparison)
    }
    assert yin == yang
  }

  static def snapshot(Object content, Comparison comparison) {
    Path resource = detectResource(comparison)
    if (LOG.isDebugEnabled()) {
      def debugString = content.toString()
      LOG.debug("No information of update of {} present, checking current as snapshot", debugString.substring(0, Math.min(20, debugString.size())))
    }
    def yang = current(content, comparison)
    def yin = readResource(resource, comparison)
    if (yin == yang) {
      LOG.debug("No SNAPSHOT changed for {} not update required", content)
      return yin;
    }
    upsertResource(content, resource, comparison)
  }

  static Path packageDir() {
    def packagePath = packageNameProvider().replaceAll("\\.", File.separator)
    def className = classNameProvider().replaceAll("(?<upper>[A-Z])", '-${upper}').toLowerCase().substring(1)

    def testDir = Paths.get("src").resolve("test").resolve("resources")
    return testDir.resolve(packagePath).resolve(className).resolve("snapshots")
  }

  static def current(def content, Comparison comparison) {
    return comparison.beforeComparison(content)
  }

  private static def upsertResource(Object content, Path resource, Comparison comparison) {
    def file = resource.toFile()
    if (updating()) {
      "Updating ${file.getPath()}"
      if (!file.exists()) {
        file.createNewFile()
      }
      file.bytes = comparison.beforeStore(content)
    } else if (!file.canRead() || file.bytes.length == 0) {
      if (ContinousIntegration.isCi()) {
        throw new FileNotFoundException(file.getPath())
      }
      LOG.debug "Creating ${file.getPath()}}"
      file.createNewFile()
      file.bytes = comparison.beforeStore(content)
    }
    return readResource(resource, comparison)
  }

  private static Path detectResource(Comparison comparison) {
    Path packageDir = packageDir()
    if (!Files.exists(packageDir)) {
      LOG.debug("Creating package dir {}", packageDir)
      Files.createDirectories(packageDir)
    }

    def filename = featureName()
      .toLowerCase(ENGLISH)
      .replaceAll("[^a-z0-9]+", "-")
    if (lastWrittenFeatureName && lastWrittenFeatureName.startsWith(featureName())) {
      featuresWritten++
    } else {
      lastWrittenFeatureName = featureName()
      featuresWritten = 0
    }

    def extension = comparison.fileExtension()
    return packageDir.resolve("${filename}${featuresWritten > 0 ? "-${featuresWritten}" : ""}.${extension}")
  }

  private static Object readResource(Path resource, Comparison comparison) {
    def file = resource.toFile()
    if (!file.canRead()) {
      return ""
    }
    FileCleanupExtension.addPackageFile(file.getName())
    LOG.debug("Restoring resources from {}", file.getPath())
    def bytes = file.getBytes()
    def afterRestore = comparison.afterRestore(bytes)
    return comparison.beforeComparison(afterRestore)
  }

}
