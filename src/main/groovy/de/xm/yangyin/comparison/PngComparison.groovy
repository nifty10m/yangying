package de.xm.yangyin.comparison


import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import org.slf4j.Logger
import org.slf4j.LoggerFactory

import javax.imageio.ImageIO
import java.awt.image.DataBufferByte

@ToString
class PngComparison extends BinaryComparison {

  private static final Logger LOG = LoggerFactory.getLogger(PngComparison.class)

  enum MODE {
    SIZE, PIXEL
  }

  static final PIXEL = MODE.PIXEL
  static final SIZE = MODE.SIZE

  private MODE comparisonMode = MODE.SIZE

  /**
   * @Deprecated Use named argument construct new PngComparison(comparisonMode: MODE) instead
   */
  static PngComparison withMode(MODE mode) {
    return new PngComparison(mode)
  }

  @EqualsAndHashCode
  @ToString(includeNames = true, includeFields = true)
  static class Result {
    int with
    int height
  }

  PngComparison() {
    super("png")
  }

  PngComparison(MODE mode) {
    super("png")
    this.comparisonMode = mode
  }

  @Override
  def beforeComparison(Object original) {
    def image = ImageIO.read(new ByteArrayInputStream(original))
    def raster = image.getRaster()

    if (comparisonMode == MODE.PIXEL) {
      def bufferByte = (DataBufferByte) raster.getDataBuffer()
      byte[] pixels = bufferByte.getData();
      def encoded = Base64.getEncoder().encode(pixels)
      return encoded
    } else if (comparisonMode == MODE.SIZE) {
      def width = raster.getWidth()
      def height = raster.getHeight()

      def result = new Result(
        with: width,
        height: height,
      )
      LOG.debug("Extracted result {}", result)
      return result
    }
    throw new RuntimeException("Unable to compare image in mode ${comparisonMode}")
  }
}
