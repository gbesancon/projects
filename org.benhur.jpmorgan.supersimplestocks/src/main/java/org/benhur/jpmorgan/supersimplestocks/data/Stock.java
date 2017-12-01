package org.benhur.jpmorgan.supersimplestocks.data;

/**
 * Stock information.
 *
 * @author GBesancon
 */
public class Stock
{
  public final String symbol;

  public enum Type
  {
    COMMON, PREFERRED
  }

  public final Type type;
  public final double lastDividend;
  public final double fixedDividend;
  public final double parValue;

  /**
   * Constructor.
   * 
   * @param symbol
   */
  public Stock(String symbol, Type type, double lastDividend, double fixedDividend, double parValue)
  {
    this.symbol = symbol;
    this.type = type;
    this.lastDividend = lastDividend;
    this.fixedDividend = fixedDividend;
    this.parValue = parValue;
  }
}
