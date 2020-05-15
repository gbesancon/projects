package org.benhur.capgemini.fruitshop

/**
 * Items.
 * AS we have a fixed number of Item, we can use an enumeration.
 */
object Item extends Enumeration { 
   protected case class Val(val name: String, val price: Double) extends super.Val { 
   } 
   implicit def valueToItemVal(x: Value) = x.asInstanceOf[Val] 

   val Apple = Val("Apple", 0.6)
   val Orange = Val("Orange", 0.25)
}