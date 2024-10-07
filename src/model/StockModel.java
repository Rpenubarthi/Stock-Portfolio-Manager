package model;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;

/**
 * This is an interface to represent a StockModel. A StockModel supports handling a portfolio,
 * finding x day moving averages/crossovers, the total value, and allows to set the stock file it
 * gets its data from.
 */
public interface StockModel {

  /**
   * The net gain of a stock over a given interval.
   *
   * @param dateStart The beginning of the interval
   * @param dateEnd   The end of the interval
   * @param stock     The stock to evaluate
   * @return The net gain as a double.
   * @throws IOException When the input file does not exist.
   */
  double netGain(LocalDate dateStart, LocalDate dateEnd, Stock stock) throws IOException;

  /**
   * Finds the x day moving average for a given stock over a given date.
   *
   * @param stock    The stock
   * @param x        The x of x day moving average (for a 5-day moving average, x would be 5)
   * @param currDate The date to start evaluate at.
   * @return The xDayMovingAverage as a double
   * @throws IOException When the input file does not exist.
   */
  double xDayMovingAverage(Stock stock, int x, LocalDate currDate) throws IOException;

  /**
   * Finds the x day crossovers given a specific stock and start date.
   *
   * @param x         The x of x day moving average (for a 5-day moving average, x would be 5)
   * @param startDate The beginning of the date interval.
   * @param endDate   The end of the date interval.
   * @param stock     The stock.
   * @return A list of the dates which are crossovers.
   * @throws IOException When the input file does not exist.
   * @throws IllegalArgumentException When the start date is after or equal to the end date
   */
  ArrayList<LocalDate> xDayCrossover(int x, LocalDate startDate, LocalDate endDate, Stock stock)
          throws IOException;

  /**
   * Adds stocks to a portfolio and also adds portfolios to a list of portfolios.
   *
   * @param stock The stock to add.
   * @param index The current index of the portfolio you want to add the stock to.
   */
  void updatePortfolio(Stock stock, int index);

  /**
   * Finds the total value of a portfolio on a given day.
   *
   * @param date  The date to evaluate the stock prices at.
   * @param index The index of portfolio to evaluate.
   * @return the total value of the portfolio as a double.
   * @throws IOException When the input file does not exist.
   */
  double totalAssetValue(LocalDate date, int index) throws IOException;

  /**
   * Sets the file for reading the stock data from.
   *
   * @param cachedStockValues The file url.
   * @throws IOException When the file path is invalid
   */
  void setDataFile(String cachedStockValues) throws IOException;
}
