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
   * Verify 2 apples cost 60p. (Offer 241 on apples)
   */
  "2 apples" should "cost 60p" in {
    val checkout = new Checkout();
    val cart = new Cart(List(Item.Apple, Item.Apple))
    val cost = checkout.computeCost(cart)
    cost should be (0.60)
  }
  
  /**
   * Verify 3 apples cost £1.20. (Offer 241 on apples)
   */
  "3 apples" should "cost £1.20" in {
    val checkout = new Checkout();
    val cart = new Cart(List(Item.Apple, Item.Apple, Item.Apple))
    val cost = checkout.computeCost(cart)
    cost should be (1.20)
  }
  
  /**
   * Verify 4 apples cost £1.20. (Offer 241 on apples)
   */
  "4 apples" should "cost £1.20" in {
    val checkout = new Checkout();
    val cart = new Cart(List(Item.Apple, Item.Apple, Item.Apple, Item.Apple))
    val cost = checkout.computeCost(cart)
    cost should be (1.20)
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
   * Verify 2 oranges cost 50p. (Offer 342 on oranges)
   */
  "2 oranges" should "cost 50p" in {
    val checkout = new Checkout();
    val cart = new Cart(List(Item.Orange, Item.Orange))
    val cost = checkout.computeCost(cart)
    cost should be (0.50)
  }
  
  /**
   * Verify 3 oranges cost 50p. (Offer 342 on oranges)
   */
  "3 oranges" should "cost 50p" in {
    val checkout = new Checkout();
    val cart = new Cart(List(Item.Orange, Item.Orange, Item.Orange))
    val cost = checkout.computeCost(cart)
    cost should be (0.50)
  }
  
  /**
   * Verify 4 oranges cost 75p. (Offer 342 on oranges)
   */
  "4 oranges" should "cost 75p" in {
    val checkout = new Checkout();
    val cart = new Cart(List(Item.Orange, Item.Orange, Item.Orange, Item.Orange))
    val cost = checkout.computeCost(cart)
    cost should be (0.75)
  }
  
  /**
   * Verify 5 oranges cost £1.00. (Offer 342 on oranges)
   */
  "5 oranges" should "cost £1.00" in {
    val checkout = new Checkout();
    val cart = new Cart(List(Item.Orange, Item.Orange, Item.Orange, Item.Orange, Item.Orange))
    val cost = checkout.computeCost(cart)
    cost should be (1.00)
  }
  
  /**
   * Verify 6 oranges cost £1.00. (Offer 342 on oranges)
   */
  "6 oranges" should "cost £1.00" in {
    val checkout = new Checkout();
    val cart = new Cart(List(Item.Orange, Item.Orange, Item.Orange, Item.Orange, Item.Orange, Item.Orange))
    val cost = checkout.computeCost(cart)
    cost should be (1.00)
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
   * Verify 3 apples and 4 orange cost £1.95. (Offer 241 on apples) (Offer 342 on oranges)
   */
  "3 apples and 4 oranges" should "cost £1.95" in {
    val checkout = new Checkout();
    val cart = new Cart(List(Item.Apple, Item.Orange, Item.Orange, Item.Apple, Item.Orange, Item.Apple, Item.Orange))
    val cost = checkout.computeCost(cart)
    cost should be (1.95)
  }
}