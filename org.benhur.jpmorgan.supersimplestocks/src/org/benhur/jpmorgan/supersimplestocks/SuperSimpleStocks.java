package org.benhur.jpmorgan.supersimplestocks;

import java.util.ArrayList;
import java.util.List;

import org.benhur.jpmorgan.supersimplestocks.data.Stock;
import org.benhur.jpmorgan.supersimplestocks.data.Stock.Type;
import org.benhur.jpmorgan.supersimplestocks.data.Trade;

/**
 * TODO comment.
 *
 * @author GBesancon
 */
public class SuperSimpleStocks
{

  protected final static List<Stock> GetSampleDataFromGBCE()
  {
    List<Stock> sampleData = new ArrayList<Stock>();
    sampleData.add(new Stock("TEA", Type.COMMON, 0.0, 0.0, 100));
    sampleData.add(new Stock("POP", Type.COMMON, 8.0, 0.0, 100));
    sampleData.add(new Stock("ALE", Type.COMMON, 23.0, 0.0, 60));
    sampleData.add(new Stock("GIN", Type.PREFERRED, 8.0, 2.0, 100));
    sampleData.add(new Stock("JOE", Type.COMMON, 13.0, 0.0, 250));
    return sampleData;
  }

  protected Trade buyStock(Stock stock, double price, double quantity)
  {
    return new Trade(stock, org.benhur.jpmorgan.supersimplestocks.data.Trade.Indicator.BUY, price, quantity,
        System.currentTimeMillis());
  }

  protected Trade sellStock(Stock stock, double price, double quantity)
  {
    return new Trade(stock, org.benhur.jpmorgan.supersimplestocks.data.Trade.Indicator.SELL, price, quantity,
        System.currentTimeMillis());
  }

  public static void main(String[] args)
  {

  }
}
