package de.xm.yangying.comparison

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

import javax.imageio.ImageIO


class PngComparison extends BinaryComparison {

  @EqualsAndHashCode
  @ToString(includeNames = true, includeFields = true)
  static class Result {
    int with
    int height
  }

  PngComparison() {
    super("png")
  }

  @Override
  def beforeComparison(Object original) {
    def image = ImageIO.read(new ByteArrayInputStream(original))
    def raster = image.getRaster()
    def width = raster.getWidth()
    def height = raster.getHeight()
    return new Result(
      with: width,
      height: height,
    )
  }
}
