package org.benhur.jpmorgan.supersimplestocks;

import org.benhur.jpmorgan.supersimplestocks.data.Stock;
import org.benhur.jpmorgan.supersimplestocks.data.Trade;

/**
 * Trading service.
 *
 * @author GBesancon
 */
public class TradingService
{
  /**
   * Buy stock.
   *
   * @param stock
   * @param price
   * @param quantity
   * @return
   */
  public static Trade buyStock(Stock stock, double price, double quantity)
  {
    return new Trade(stock, org.benhur.jpmorgan.supersimplestocks.data.Trade.Indicator.BUY, price, quantity,
        System.currentTimeMillis());
  }

  /**
   * Sell stock
   * 
   * @param stock
   * @param price
   * @param quantity
   * @return
   */
  public static Trade sellStock(Stock stock, double price, double quantity)
  {
    return new Trade(stock, org.benhur.jpmorgan.supersimplestocks.data.Trade.Indicator.SELL, price, quantity,
        System.currentTimeMillis());
  }
}
