package org.benhur.capgemini.fruitshop

/**
 * A Cart item.
 * A cart item has a price.
 */
trait Item {
  val name: String
  /** Price in GBP. */
  val price: Double
}

/**
 * An Apple.
 */
object Apple extends Item {
  override val name = "Apple"
  override val price = 0.6
}

/**
 * An Orange.
 */
object Orange extends Item {
  override val name = "Orange"
  override val price = 0.25
}