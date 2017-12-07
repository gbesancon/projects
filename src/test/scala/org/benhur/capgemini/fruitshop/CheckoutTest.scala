package org.benhur.capgemini.fruitshop

import org.junit.runner.RunWith
import org.scalatest.FlatSpec
import org.scalatest.Matchers
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class CheckoutTest extends FlatSpec with Matchers {
  "1 apple" should "cost 60p" in {
    val checkout = new Checkout();
    val cart = Array("Apple")
    val cost = checkout.computeCost(cart)
    cost should be (0.60)
  }
    
  "1 orange" should "cost 25p" in {
    val checkout = new Checkout();
    val cart = Array("Orange")
    val cost = checkout.computeCost(cart)
    cost should be (0.25)
  }
    
  "3 apples and 1 orange" should "cost £2.05" in {
    val checkout = new Checkout();
    val cart = Array("Apple", "Orange", "Apple", "Apple")
    val cost = checkout.computeCost(cart)
    cost should be (0.25)
  }
}