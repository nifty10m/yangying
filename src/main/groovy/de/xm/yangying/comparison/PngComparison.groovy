package de.xm.yangying.comparison

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

import javax.imageio.ImageIO
import java.awt.image.DataBufferByte


class PngComparison extends BinaryComparison {

  enum MODE {
    SIZE, PIXEl
  }

  private MODE mode = MODE.SIZE

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
    this.mode = mode
  }

  @Override
  def beforeComparison(Object original) {
    def image = ImageIO.read(new ByteArrayInputStream(original))
    def raster = image.getRaster()

    if (mode == MODE.PIXEl) {
      def bufferByte = (DataBufferByte) raster.getDataBuffer()
      byte[] pixels = bufferByte.getData();
      def encoded = Base64.getEncoder().encode(pixels)
      return encoded
    } else if (mode == MODE.SIZE) {
      def width = raster.getWidth()
      def height = raster.getHeight()
      return new Result(
        with: width,
        height: height,
      )
    }
    throw new RuntimeException("Unable to compare image in mode " + mode)
  }
}
