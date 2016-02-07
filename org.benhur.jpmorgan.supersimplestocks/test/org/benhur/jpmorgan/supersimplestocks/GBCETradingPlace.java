package org.benhur.jpmorgan.supersimplestocks;

import java.util.ArrayList;
import java.util.List;

import org.benhur.jpmorgan.supersimplestocks.data.Stock;
import org.benhur.jpmorgan.supersimplestocks.data.Stock.Type;
import org.benhur.jpmorgan.supersimplestocks.data.Trade;
import org.benhur.jpmorgan.supersimplestocks.data.Trade.Indicator;

/**
 * Trading place.
 *
 * @author gbesancon
 */
public class GBCETradingPlace
{
  public final long initialTimestamp;
  public final List<Stock> stocks = new ArrayList<>();
  public final List<Trade> trades = new ArrayList<>();

  /**
   * Constructor.
   */
  public GBCETradingPlace(long initialTimestamp)
  {
    this.initialTimestamp = initialTimestamp;
    initializeStocks();
    initializeTrades();
  }

  /**
   * Initialize stocks according to Sample Data.
   */
  protected void initializeStocks()
  {
    // Sample Data from the GBCE
    stocks.add(new Stock("TEA", Type.COMMON, 0.0, 0.0, 0.1));
    stocks.add(new Stock("POP", Type.COMMON, 0.08, 0.0, 0.1));
    stocks.add(new Stock("ALE", Type.COMMON, 0.23, 0.0, 0.06));
    stocks.add(new Stock("GIN", Type.PREFERRED, 0.08, 0.2, 0.1));
    stocks.add(new Stock("JOE", Type.COMMON, 0.13, 0.0, 0.25));
  }

  /**
   * Initialize trades for Sample Data.
   */
  protected void initializeTrades()
  {
    // Generate 30 minutes of Data not so random to be able to check values
    // Would need better test data !!!
    boolean buyIndicator = false; // if true -> BUY, if false -> SELL
    for (long deltaTime = 0; deltaTime < 30 * 60 * 1000; deltaTime += 10 * 1000)
    {
      for (Stock stock : stocks)
      {
        trades.add(new Trade(stock, buyIndicator ? Indicator.BUY : Indicator.SELL, 0.1 + deltaTime / 1000000.0, 10,
            initialTimestamp + deltaTime));
      }
      buyIndicator = !buyIndicator;
    }
  }
}
