package model.addedfeatures;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Map;

import model.StockModel;

/**
 * Represents a BetterStockModel which can hold and do various operations on a portfolio of stocks
 * such as adding/removing a stock to a portfolio, getting the value of the portfolio,
 * getting the distribution/composition, rebalancing and getting performance over time.
 */
public interface BetterStockModel extends StockModel {

  /**
   * Default constructor for the BetterBasicStockModel.
   *
   * @throws IOException When the file does not exist.
   */

  void addPortfolio(String portfolioName) throws IOException;

  /**
   * Buys a stock and puts it in the portfolio.
   *
   * @param ticker        The stock ticker
   * @param shares        the shares
   * @param portfolioName the portfolio name to put it in
   * @param dateBought    The date bought
   * @throws IOException When the input file is not there.
   **/
  void buyStock(String ticker, int shares, String portfolioName, LocalDate dateBought)
          throws IOException;

  /**
   * Sells a stock and puts it in the portfolio.
   *
   * @param ticker        The stock ticker
   * @param shares        the shares
   * @param portfolioName the portfolio name to put it in
   * @param dateSold      The date sold
   * @throws IOException When the input file is not there.
   **/
  void sellStock(String ticker, double shares, String portfolioName, LocalDate dateSold)
          throws IOException;

  /**
   * Total value of a portfolio on a given date.
   *
   * @param portfolioName Name of portfolio
   * @param date          The date
   * @return The value of the portfolio
   * @throws IOException When the input file is not there.
   */
  double totalAssetValue(String portfolioName, LocalDate date) throws IOException;

  /**
   * Composiiton of a portfolio.
   *
   * @param portfolioName The portfolio name
   * @param date          The date to evaluate
   * @return The shares of each stock
   * @throws FileNotFoundException When the file is not there
   */
  Map<String, Double> getComposition(String portfolioName, LocalDate date)
          throws FileNotFoundException;

  /**
   * Distribution of a portfolio.
   *
   * @param portfolioName The portfolio name
   * @param date          The date to evaluate
   * @return The total value of each stock
   * @throws FileNotFoundException When the file is not there
   */
  Map<String, Double> getDistribution(String portfolioName, LocalDate date) throws IOException;

  /**
   * Rebalances a portfolio on a given date.
   *
   * @param portfolioName The portfolio name
   * @param weights       The weights of each stock
   * @param date          The date to evaluate
   * @throws IOException When there is no input file
   */
  void rebalance(String portfolioName, Map<String, Double> weights, LocalDate date)
          throws IOException;

  /**
   * Loads a portfolio from the cache.
   *
   * @param portfolioName The portfolio name
   * @throws IOException When there is no input file
   */
  void loadPortfolio(String portfolioName) throws IOException;

  /**
   * Calculates the performance over time of the portfolio/stock.
   *
   * @param isStock       If it is a stock or portfolio
   * @param portfolioName Name of stock/portfolio
   * @param startDate     Start date
   * @param endDate       End date
   * @return A bar chart with the values at the time intervals
   * @throws IOException When the input file is not there
   * @throws IllegalArgumentException When the start date is after or equal to the end date
   */
  String performanceOverTime(boolean isStock, String portfolioName, LocalDate startDate,
                             LocalDate endDate) throws IOException;
}
