package org.benhur.jpmorgan.supersimplestocks;

import static org.junit.Assert.assertEquals;

import org.benhur.jpmorgan.supersimplestocks.data.Stock;
import org.benhur.jpmorgan.supersimplestocks.data.Stock.Type;
import org.benhur.jpmorgan.supersimplestocks.data.Trade;
import org.benhur.jpmorgan.supersimplestocks.data.Trade.Indicator;
import org.junit.Test;

/**
 * Test for Trading service.
 *
 * @author gbesancon
 */
public class TradingServiceTest
{
  private static final double DELTA = 0.001;

  /**
   * Would need better test data !!!
   */

  @Test
  public void testBuyStock()
  {
    Stock stock = new Stock("GIN", Type.COMMON, 8.0, 0.02, 100.0);
    Trade trade = TradingService.buyStock(stock, 10.0, 10.0);
    long timestamp = System.currentTimeMillis();
    assertEquals(stock, trade.stock);
    assertEquals(Indicator.BUY, trade.indicator);
    assertEquals(10.0, trade.price, DELTA);
    assertEquals(10.0, trade.quantity, DELTA);
    assertEquals(timestamp, trade.timestamp, 1l);
  }

  @Test
  public void testSellStock()
  {
    Stock stock = new Stock("GIN", Type.COMMON, 8.0, 0.02, 100.0);
    Trade trade = TradingService.sellStock(stock, 10.0, 10.0);
    long timestamp = System.currentTimeMillis();
    assertEquals(stock, trade.stock);
    assertEquals(Indicator.SELL, trade.indicator);
    assertEquals(10.0, trade.price, DELTA);
    assertEquals(10.0, trade.quantity, DELTA);
    assertEquals(timestamp, trade.timestamp, 1l);
  }
}
