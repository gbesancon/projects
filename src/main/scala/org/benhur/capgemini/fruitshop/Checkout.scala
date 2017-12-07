package org.benhur.capgemini.fruitshop

/**
 * Checkout.
 * The checkout system allow to compute the cost of a cart.
 */
class Checkout {
  /**
   * Compute the cost of a cart.
   */
  def computeCost(cart: Cart): Double = {
    var cost = 0.0
    for( item <- cart.items ) {
      cost += item.price
    }
    cost
  }
}