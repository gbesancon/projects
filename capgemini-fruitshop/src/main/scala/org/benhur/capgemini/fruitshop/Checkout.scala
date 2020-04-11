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
    
    def offer241(nbItems: Integer, priceItem: Double): Double = {
      ((nbItems / 2) + (nbItems % 2)) * priceItem
    }
    
    def offer342(nbItems: Integer, priceItem: Double): Double = {
      ((nbItems / 3) * 2 + (nbItems % 3)) * priceItem
    }
    
    var nbApples = cart.items.groupBy(identity).mapValues(_.size).getOrElse(Item.Apple, 0)
    var nbOranges = cart.items.groupBy(identity).mapValues(_.size).getOrElse(Item.Orange, 0)
    
    cost = offer241(nbApples, Item.Apple.price) + offer342(nbOranges, Item.Orange.price)
    
    cost
  }
}