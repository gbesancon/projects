package org.benhur.jpmorgan.supersimplestocks;

import static org.junit.Assert.assertEquals;

import org.benhur.jpmorgan.supersimplestocks.StockFormulas;
import org.benhur.jpmorgan.supersimplestocks.data.Stock;
import org.benhur.jpmorgan.supersimplestocks.data.Stock.Type;
import org.benhur.jpmorgan.supersimplestocks.data.Trade;
import org.benhur.jpmorgan.supersimplestocks.data.Trade.Indicator;
import org.junit.Test;

/**
 * Test for Stocks formulas.
 *
 * @author GBesancon
 */
public class StockFormulasTest
{
  private static final double DELTA = 0.001;

  /**
   * Would need better test data !!!
   */

  @Test
  public void testComputeDividendYieldFromLastDividend()
  {
    Stock stock1 = new Stock("GIN", Type.COMMON, 8.0, 0.02, 100.0);
    assertEquals(0.8, StockFormulas.ComputeDividendYield(stock1, 10.0), DELTA);
    Stock stock2 = new Stock("GIN", Type.PREFERRED, 8.0, 0.02, 100.0);
    assertEquals(0.2, StockFormulas.ComputeDividendYield(stock2, 10.0), DELTA);
  }

  @Test
  public void testComputePERatio()
  {
    Stock stock1 = new Stock("GIN", Type.COMMON, 8.0, 0.02, 100.0);
    assertEquals(1.25, StockFormulas.ComputePERatio(stock1, 10.0), DELTA);
    Stock stock2 = new Stock("GIN", Type.PREFERRED, 8.0, 0.02, 100.0);
    assertEquals(5.0, StockFormulas.ComputePERatio(stock2, 10.0), DELTA);
  }

  @Test
  public void testComputeGeometricMean()
  {
    double[] prices = { 10.0, 9.0, 8.0, 9.0, 10.0, 11.0, 12.0 };
    assertEquals(9.779, StockFormulas.ComputeGeometricMean(prices), DELTA);
  }

  @Test
  public void testComputeVolumeWeightedStockPrice()
  {
    Stock stock = new Stock("TEA", org.benhur.jpmorgan.supersimplestocks.data.Stock.Type.COMMON, 0.0, 0.0, 100.0);
    Trade[] trade = { new Trade(stock, Indicator.BUY, 10.0, 10.0, System.currentTimeMillis()),
        new Trade(stock, Indicator.BUY, 9.0, 10.0, System.currentTimeMillis()),
        new Trade(stock, Indicator.BUY, 8.0, 10.0, System.currentTimeMillis()),
        new Trade(stock, Indicator.BUY, 9.0, 10.0, System.currentTimeMillis()),
        new Trade(stock, Indicator.BUY, 10.0, 10.0, System.currentTimeMillis()),
        new Trade(stock, Indicator.BUY, 11.0, 10.0, System.currentTimeMillis()),
        new Trade(stock, Indicator.BUY, 12.0, 10.0, System.currentTimeMillis()) };
    assertEquals(9.857, StockFormulas.ComputeVolumeWeightedStockPrice(trade), DELTA);
  }
}
