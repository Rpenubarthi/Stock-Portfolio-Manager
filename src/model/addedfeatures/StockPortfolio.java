package model.addedfeatures;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * Represents a Stock Portfolio which can hold stock tickers. It can also add/read from a csv file
 * where it stores all the portfolio information.
 */
public interface StockPortfolio {

  /**
   * Gets stock and shares of the portfolio.
   *
   * @return The stocks and shares as a Map.
   */
  List<String> getStocks();


  /**
   * Saves the current stock transaction into a csv file.
   *
   * @param date   The date bought/sold
   * @param shares The shares
   * @param ticker The stock ticker
   * @throws IOException When the input file is not there.
   */
  void saveData(LocalDate date, double shares, String ticker)
          throws IOException;

  /**
   * Gets the current shares of the given stock in the portfolio.
   *
   * @param ticker   The stock ticker
   * @param dateSold The date to find the shares
   * @return The current shares as a double.
   * @throws FileNotFoundException If the input file is not there.
   */
  double getCurrentShares(String ticker, LocalDate dateSold) throws FileNotFoundException;

  /**
   * gets a snapshot of the portfolio on a specific date.
   *
   * @param date The date to get the snapshot
   * @return The snapshot as a Map of ticker to shares
   * @throws FileNotFoundException If the input file is not there.
   */
  Map<String, Double> getPortfolioSnapshot(LocalDate date) throws FileNotFoundException;

  /**
   * Gets the name of the portfolio.
   *
   * @return The name as a string
   */
  String getPortfolioName();

  /**
   * Loads the portfolio from the csv file into the current memory of the program.
   *
   * @throws FileNotFoundException If the input file is not there.
   */
  void loadData() throws FileNotFoundException;
}
