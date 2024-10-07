package model;

/**
 * Represents a stock which can get its shares, its ticker, and add shares.
 */
public interface Stock {


  /**
   * Gets the number of shares of the stock.
   *
   * @return an integer amount of shares
   */
  int getShares();

  /**
   * Gets the stock ticker.
   *
   * @return a string of the stock ticker.
   */
  String getSymbol();

  /**
   * Adds shares to the current stock.
   *
   * @param x The number of shares to add
   */
  void addShares(int x);
}
