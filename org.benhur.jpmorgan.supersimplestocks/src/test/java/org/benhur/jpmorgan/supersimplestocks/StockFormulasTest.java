// Copyright (C) 2017 GBesancon

package org.benhur.jpmorgan.supersimplestocks;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

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
    Stock stock1 = new Stock("GIN", Type.COMMON, 0.08, 0.02, 1);
    assertEquals(0.8, StockFormulas.ComputeDividendYield(stock1, 0.1), DELTA);
    Stock stock2 = new Stock("GIN", Type.PREFERRED, 0.08, 0.02, 1);
    assertEquals(0.2, StockFormulas.ComputeDividendYield(stock2, 0.1), DELTA);
  }

  @Test
  public void testComputePERatio()
  {
    Stock stock1 = new Stock("GIN", Type.COMMON, 0.08, 0.02, 1);
    assertEquals(1.25, StockFormulas.ComputePERatio(stock1, 0.1), DELTA);
    Stock stock2 = new Stock("GIN", Type.PREFERRED, 0.08, 0.02, 1);
    assertEquals(5.0, StockFormulas.ComputePERatio(stock2, 0.1), DELTA);
  }

  @Test
  public void testComputeGeometricMean()
  {
    Stock stock = new Stock("TEA", org.benhur.jpmorgan.supersimplestocks.data.Stock.Type.COMMON, 0.0, 0.0, 1.0);
    List<Trade> trades = new ArrayList<>();
    trades.add(new Trade(stock, Indicator.BUY, 0.10, 10, System.currentTimeMillis()));
    trades.add(new Trade(stock, Indicator.BUY, 0.09, 10, System.currentTimeMillis()));
    trades.add(new Trade(stock, Indicator.BUY, 0.08, 10, System.currentTimeMillis()));
    trades.add(new Trade(stock, Indicator.BUY, 0.09, 10, System.currentTimeMillis()));
    trades.add(new Trade(stock, Indicator.BUY, 0.1, 10, System.currentTimeMillis()));
    trades.add(new Trade(stock, Indicator.BUY, 0.11, 10, System.currentTimeMillis()));
    trades.add(new Trade(stock, Indicator.BUY, 0.12, 10, System.currentTimeMillis()));
    assertEquals(0.097, StockFormulas.ComputeGeometricMean(trades), DELTA);
  }

  @Test
  public void testComputeVolumeWeightedStockPrice()
  {
    Stock stock = new Stock("TEA", org.benhur.jpmorgan.supersimplestocks.data.Stock.Type.COMMON, 0.0, 0.0, 1.0);
    List<Trade> trades = new ArrayList<>();
    trades.add(new Trade(stock, Indicator.BUY, 0.10, 10, System.currentTimeMillis()));
    trades.add(new Trade(stock, Indicator.BUY, 0.09, 10, System.currentTimeMillis()));
    trades.add(new Trade(stock, Indicator.BUY, 0.08, 10, System.currentTimeMillis()));
    trades.add(new Trade(stock, Indicator.BUY, 0.09, 10, System.currentTimeMillis()));
    trades.add(new Trade(stock, Indicator.BUY, 0.1, 10, System.currentTimeMillis()));
    trades.add(new Trade(stock, Indicator.BUY, 0.11, 10, System.currentTimeMillis()));
    trades.add(new Trade(stock, Indicator.BUY, 0.12, 10, System.currentTimeMillis()));

    assertEquals(0.0985, StockFormulas.ComputeVolumeWeightedStockPrice(trades), DELTA);
  }
}
