package org.benhur.jpmorgan.supersimplestocks.data;

/**
 * Trade information for a given stock.
 *
 * @author GBesancon
 */
public class Trade
{
  public final Stock stock;

  public enum Indicator
  {
    BUY, SELL
  }

  public final Indicator indicator;
  public final double price;
  public final int quantity;
  public final long timestamp;

  /**
   * Constructor.
   * 
   * @param stock
   * @param indicator
   * @param price
   * @param quantity
   */
  public Trade(Stock stock, Indicator indicator, double price, int quantity, long timestamp)
  {
    this.stock = stock;
    this.indicator = indicator;
    this.price = price;
    this.quantity = quantity;
    this.timestamp = timestamp;
  }
}
