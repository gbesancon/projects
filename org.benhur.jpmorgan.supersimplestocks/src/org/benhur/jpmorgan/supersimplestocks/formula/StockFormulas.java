package org.benhur.jpmorgan.supersimplestocks.formula;

import org.benhur.jpmorgan.supersimplestocks.data.Stock;
import org.benhur.jpmorgan.supersimplestocks.data.Stock.Type;
import org.benhur.jpmorgan.supersimplestocks.data.Trade;

/**
 * Stoc Formulas.
 *
 * @author GBesancon
 */
public class StockFormulas
{

  /**
   * Compute dividend from last dividend.
   *
   * @param lastDividend
   * @return
   */
  protected static double ComputeDividendFromLastDividend(double lastDividend)
  {
    return lastDividend;
  }

  /**
   * Compute dividend from fixed dividend.
   * 
   * @param fixedDividend
   * @param parValue
   * @return
   */
  protected static double ComputeDividendFromFixedDividend(double fixedDividend, double parValue)
  {
    return fixedDividend * parValue;
  }

  /**
   * Compute dividend.
   *
   * @param stock
   * @return
   */
  protected static double CommputeDividend(Stock stock)
  {
    final double dividend;
    if (stock.type == Type.PREFERRED)
    {
      dividend = ComputeDividendFromFixedDividend(stock.fixedDividend, stock.parValue);
    }
    else
    {
      dividend = ComputeDividendFromLastDividend(stock.lastDividend);
    }
    return dividend;
  }

  /**
   * Compute Dividend Yield from dividend.
   *
   * @param dividend
   * @param marketPrice
   * @return
   */
  protected static double ComputeDividendYieldFromDividend(double dividend, double marketPrice)
  {
    return dividend / marketPrice;
  }

  /**
   * Get dividend yield.
   *
   * @param stock
   * @param marketPrice
   * @return
   */
  public static double ComputeDividendYield(Stock stock, double marketPrice)
  {
    return ComputeDividendYieldFromDividend(CommputeDividend(stock), marketPrice);
  }

  /**
   * Compute P/E Ratio.
   *
   * @param marketPrice
   * @param dividend
   * @return
   */
  protected static double ComputePERatio(double marketPrice, double dividend)
  {
    return marketPrice / dividend;
  }

  /**
   * Get P/E Ratio.
   *
   * @param stock
   * @param marketPrice
   * @return
   */
  public static double ComputePERatio(Stock stock, double marketPrice)
  {
    return ComputePERatio(marketPrice, CommputeDividend(stock));
  }

  /**
   * Compute Geometric Mean of prices.
   *
   * @param prices
   *          (positive values)
   * @return
   */
  public static double ComputeGeometricMean(double[] prices)
  {
    double productOfPrices = 1.0;
    for (double price : prices)
    {
      productOfPrices *= price;
    }
    // Prices are positive values so we can use n-root(x) = pow(x, 1/n)
    // Probably better use a 3rd party
    double nRootPower = 1.0 / prices.length;
    return Math.pow(productOfPrices, nRootPower);
  }

  /**
   * Compute the Volume Weighted Stock Price.
   *
   * @param trades
   * @return
   */
  public static double ComputeVolumeWeightedStockPrice(Trade[] trades)
  {
    double allTradesValue = 0.0;
    double totalQuantity = 0.0;
    for (Trade trade : trades)
    {
      allTradesValue += (trade.price * trade.quantity);
      totalQuantity += trade.quantity;
    }
    return allTradesValue / totalQuantity;
  }
}
