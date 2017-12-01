// Copyright (C) 2017 GBesancon

package org.benhur.jpmorgan.supersimplestocks;

import java.util.List;
import java.util.stream.Collectors;

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
  public static Trade buyStock(Stock stock, double price, int quantity, long timestamp)
  {
    return new Trade(stock, org.benhur.jpmorgan.supersimplestocks.data.Trade.Indicator.BUY, price, quantity, timestamp);
  }

  /**
   * Sell stock
   * 
   * @param stock
   * @param price
   * @param quantity
   * @param timestamp
   * @return
   */
  public static Trade sellStock(Stock stock, double price, int quantity, long timestamp)
  {
    return new Trade(stock, org.benhur.jpmorgan.supersimplestocks.data.Trade.Indicator.SELL, price, quantity,
        timestamp);
  }

  /**
   * Filter trades by stock.
   *
   * @param trades
   * @param stock
   * @return
   */
  public static List<Trade> filterTradesByStock(List<Trade> trades, Stock stock)
  {
    return trades.stream().filter(trade -> trade.stock == stock).collect(Collectors.toList());
  }

  /**
   * Filter trades by timestamp.
   *
   * @param trades
   * @param time
   * @param deltaTimeInThePast
   * @return
   */
  public static List<Trade> filterTradesByTimestamp(List<Trade> trades, long time, long deltaTimeInThePast)
  {
    return trades.stream().filter(trade -> (time - deltaTimeInThePast <= trade.timestamp && trade.timestamp <= time))
        .collect(Collectors.toList());
  }
}
