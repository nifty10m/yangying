package de.xm.yangying.comparison

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

import javax.imageio.ImageIO
import java.awt.image.DataBufferByte

@ToString
class PngComparison extends BinaryComparison {

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
      return new Result(
        with: width,
        height: height,
      )
    }
    throw new RuntimeException("Unable to compare image in mode " + comparisonMode)
  }
}
