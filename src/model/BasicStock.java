package model;

/**
 * A basic representation of a stock which includes the ticker as well as the current shares.
 */
public class BasicStock implements Stock {
  protected final String ticker;
  protected int shares;

  /**
   * The constructor for a stock which takes in the ticker and shares.
   *
   * @param ticker The ticker of the stock
   * @param shares The number of shares the stock has
   */
  public BasicStock(String ticker, int shares) {
    this.ticker = ticker;
    this.shares = shares;
  }

  /**
   * The default constructor for a BasicStock.
   *
   * @param ticker The ticker of the stock
   */
  public BasicStock(String ticker) {
    this.ticker = ticker;
    this.shares = 0;
  }

  @Override
  public void addShares(int x) {
    shares += x;
  }

  @Override
  public String getSymbol() {
    return new StringBuilder(this.ticker).toString();
  }

  @Override
  public int getShares() {
    return this.shares;
  }

  @Override
  public boolean equals(Object o) {
    if (o instanceof Stock) {
      return ((Stock) o).getSymbol().equals(this.ticker);
    }
    return false;
  }

  @Override
  public int hashCode() {
    return this.ticker.hashCode();
  }

}
