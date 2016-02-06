package org.benhur.jpmorgan.supersimplestocks;

import java.util.ArrayList;
import java.util.List;

import org.benhur.jpmorgan.supersimplestocks.data.Stock;
import org.benhur.jpmorgan.supersimplestocks.data.Stock.Type;
import org.benhur.jpmorgan.supersimplestocks.data.Trade;

/**
 * Trading place.
 *
 * @author gbesancon
 */
public class TradingPlace
{
  public final List<Stock> stocks = new ArrayList<>();
  public final List<Trade> trades = new ArrayList<>();

  /**
   * Constructor.
   */
  public TradingPlace()
  {
    initializeStocks();
  }

  protected void initializeStocks()
  {
    stocks.add(new Stock("TEA", Type.COMMON, 0.0, 0.0, 100.0));
    stocks.add(new Stock("POP", Type.COMMON, 8.0, 0.0, 100.0));
    stocks.add(new Stock("ALE", Type.COMMON, 23.0, 0.0, 100.0));
    stocks.add(new Stock("GIN", Type.PREFERRED, 8.0, 0.2, 100.0));
    stocks.add(new Stock("JOE", Type.COMMON, 13.0, 0.0, 100.0));
  }
}
