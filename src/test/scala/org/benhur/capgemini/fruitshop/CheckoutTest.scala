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
    val cart = new Cart(List(Apple))
    val cost = checkout.computeCost(cart)
    cost should be (0.60)
  }
    
  /**
   * Verify an orange cost 25p.
   */
  "1 orange" should "cost 25p" in {
    val checkout = new Checkout();
    val cart = new Cart(List(Orange))
    val cost = checkout.computeCost(cart)
    cost should be (0.25)
  }
    
  /**
   * Verify 3 apples and 1 orange cost �2.05.
   */
  "3 apples and 1 orange" should "cost £2.05" in {
    val checkout = new Checkout();
    val cart = new Cart(List(Apple, Apple, Orange, Apple))
    val cost = checkout.computeCost(cart)
    cost should be (2.05)
  }
}