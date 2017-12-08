package org.benhur.capgemini.fruitshop

import org.junit.runner.RunWith
import org.scalatest.FlatSpec
import org.scalatest.Matchers
import org.scalatest.junit.JUnitRunner

/**
 * Class which test the Checkout mechnism.
 */
@RunWith(classOf[JUnitRunner])
class CheckoutTest extends FlatSpec with Matchers {
  
  /**
   * Verify an apple cost 60p. 
   */
  "1 apple" should "cost 60p" in {
    val checkout = new Checkout();
    val cart = new Cart(List(Item.Apple))
    val cost = checkout.computeCost(cart)
    cost should be (0.60)
  }
    
  /**
   * Verify an orange cost 25p.
   */
  "1 orange" should "cost 25p" in {
    val checkout = new Checkout();
    val cart = new Cart(List(Item.Orange))
    val cost = checkout.computeCost(cart)
    cost should be (0.25)
  }
    
  /**
   * Verify 3 apples and 1 orange cost £1.45. (Offer 241 on apples)
   */
  "3 apples and 1 orange" should "cost £1.45" in {
    val checkout = new Checkout();
    val cart = new Cart(List(Item.Apple, Item.Apple, Item.Orange, Item.Apple))
    val cost = checkout.computeCost(cart)
    cost should be (1.45)
  }
  
  /**
   * Verify 4 apples cost £1.20. (Offer 241 on apples)
   */
  "3 apples and 1 orange" should "cost £1.20" in {
    val checkout = new Checkout();
    val cart = new Cart(List(Item.Apple, Item.Apple, Item.Apple, Item.Apple))
    val cost = checkout.computeCost(cart)
    cost should be (1.20)
  }
}