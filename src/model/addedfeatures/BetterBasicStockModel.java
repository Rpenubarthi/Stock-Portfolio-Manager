package model.addedfeatures;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;

import model.BasicStock;
import model.BasicStockModel;
import model.Stock;

/**
 * An improvised version of the stock model that can hold portfolios, load them and do operations
 * on them such as getting their values on a specific date.
 */
public class BetterBasicStockModel extends BasicStockModel implements BetterStockModel {
  //CHANGED
  ArrayList<StockPortfolio> portfolios;

  /**
   * Default constructor for the BetterBasicStockModel.
   *
   * @throws IOException When the file does not exist.
   */
  //CHANGED
  public BetterBasicStockModel() throws IOException {
    super();
    portfolios = new ArrayList<>();
  }

  @Override
  public void addPortfolio(String portfolioName) throws IOException {
    StockPortfolio portfolio = new BasicStockPortfolio(portfolioName);
    boolean exists = true;
    try {
      this.getPortfolio(portfolio.getPortfolioName());
    } catch (IllegalArgumentException e) {
      exists = false;
    }
    if (exists) {
      throw new IllegalArgumentException("A portfolio with that name already exists.");
    } else {
      portfolios.add(portfolio);
    }
  }


  private StockPortfolio getPortfolio(String portfolioName) {
    for (StockPortfolio currPortfolio : portfolios) {
      if (currPortfolio.getPortfolioName().equals(portfolioName)) {
        return currPortfolio;
      }
    }
    throw new IllegalArgumentException("No portfolio with that name exists.");
  }


  @Override
  public void buyStock(String ticker, int shares, String portfolioName, LocalDate dateBought)
          throws IOException {
    if (shares < 0) {
      throw new IllegalArgumentException("Shares cannot be negative");
    }
    try {
      StockPortfolio portfolio = getPortfolio(portfolioName);
      portfolio.saveData(dateBought, shares, ticker);
    } catch (IllegalArgumentException e) {
      throw new IllegalArgumentException(e.getMessage());
    }
  }

  @Override
  public void sellStock(String ticker, double shares, String portfolioName, LocalDate dateSold)
          throws IOException {
    StockPortfolio portfolio;
    if (shares < 0) {
      throw new IllegalArgumentException("Shares cannot be negative");
    }
    try {
      portfolio = getPortfolio(portfolioName);
    } catch (IllegalArgumentException e) {
      throw new IllegalArgumentException(e.getMessage());
    }
    double sharesAtDate = portfolio.getCurrentShares(ticker, dateSold);
    if (sharesAtDate >= shares) {
      portfolio.saveData(dateSold, shares * -1, ticker);
    } else {
      throw new IllegalArgumentException("Cannot sell more stocks than owned.");
    }

  }

  @Override
  public double totalAssetValue(String portfolioName, LocalDate date) throws IOException {
    double totalValue = 0;
    StockPortfolio portfolio = this.getPortfolio(portfolioName);
    if (portfolio.getStocks().isEmpty()) {
      return totalValue;
    }
    Map<String, Double> currStocks = portfolio.getPortfolioSnapshot(date);
    for (String ticker : currStocks.keySet()) {
      totalValue += this.getStockClosingValue(new BasicStock(ticker), date)
              * currStocks.get(ticker);
    }
    return totalValue;
  }

  @Override
  public Map<String, Double> getComposition(String portfolioName, LocalDate date)
          throws FileNotFoundException {
    StockPortfolio portfolio = this.getPortfolio(portfolioName);
    return portfolio.getPortfolioSnapshot(date);
  }

  @Override
  public Map<String, Double> getDistribution(String portfolioName, LocalDate date)
          throws IOException {
    Map<String, Double> distribution = this.getComposition(portfolioName, date);
    for (String ticker : distribution.keySet()) {
      double newValue = distribution.get(ticker)
              * this.getStockClosingValue(new BasicStock(ticker), date);
      newValue = Double.parseDouble(String.format("%.2f", newValue));
      distribution.put(ticker, newValue);
    }
    return distribution;
  }

  @Override
  public void rebalance(String portfolioName, Map<String, Double> weights, LocalDate date)
          throws IOException {
    double sumOfWeights = 0;
    ArrayList<String> currentTickers = new ArrayList<>();
    StockPortfolio portfolio = this.getPortfolio(portfolioName);
    double totalAssetValue = this.totalAssetValue(portfolioName, date);
    Map<String, Double> stockComposition = this.getComposition(portfolioName, date);

    for (String ticker : weights.keySet()) {
      if (currentTickers.contains(ticker)) {
        throw new IllegalArgumentException("The same stock ticker cannot be inputted twice.");
      }
      if (!portfolio.getStocks().contains(ticker)) {
        throw new IllegalArgumentException("Stock does not exist in this portfolio");
      }
      sumOfWeights += weights.get(ticker);
      currentTickers.add(ticker);
    }

    if (sumOfWeights != 1.00) {
      throw new IllegalArgumentException("Weights do not add up to 1.");
    }

    for (String ticker : weights.keySet()) {
      double expectedNumStocks = (totalAssetValue * weights.get(ticker))
              / this.getStockClosingValue(new BasicStock(ticker), date);
      double netDifference = expectedNumStocks - stockComposition.get(ticker);
      portfolio.saveData(date, netDifference, ticker);
    }
  }

  @Override
  public String performanceOverTime(boolean isStock, String name, LocalDate startDate,
                                    LocalDate endDate) throws IOException {
    if (startDate.equals(endDate) || startDate.isAfter(endDate)) {
      throw new IllegalArgumentException("Start date cannot be equal to or after end date.");
    }
    StringBuilder barChart = new StringBuilder();
    String type;
    if (isStock) {
      type = "stock";
    } else {
      type = "portfolio";
    }
    String headline = "Performance of " + type + ": " + name + " from " + startDate.toString()
            + " to " + endDate.toString() + "\n\n";
    barChart.append(headline);
    long difference = startDate.until(endDate, ChronoUnit.DAYS);
    long interval;
    LocalDate currDate = startDate;
    Map<String, Double> portfolioValueOverTime = new TreeMap<>();
    if (difference < 5) {
      interval = 1;
    } else if (difference < 30) {
      interval = 2;
    } else {
      interval = difference / 15;
    }

    if (isStock) {
      Stock stock = new BasicStock(name);
      while (currDate.isBefore(endDate)) {
        portfolioValueOverTime.put(currDate.toString(), this.getStockClosingValue(stock, currDate));
        currDate = currDate.plusDays(interval);
      }
      portfolioValueOverTime.put(endDate.toString(), this.getStockClosingValue(stock, currDate));
    } else {
      while (currDate.isBefore(endDate)) {
        portfolioValueOverTime.put(currDate.toString(), this.totalAssetValue(name, currDate));
        currDate = currDate.plusDays(interval);
      }
      portfolioValueOverTime.put(endDate.toString(), this.totalAssetValue(name, currDate));
    }

    double largestAssetValue = Collections.max(portfolioValueOverTime.values());
    int roundToNearestXPlace = (int) Math.log10(largestAssetValue);
    if (roundToNearestXPlace > 1) {
      roundToNearestXPlace -= 1;
    }
    int scale = (int) Math.pow(10, roundToNearestXPlace);
    for (String date : portfolioValueOverTime.keySet()) {
      int numAsterisks = (int) (portfolioValueOverTime.get(date) / scale);
      String formattedDate = LocalDate.parse(date).getMonth().toString().substring(0, 3)
              + " " + LocalDate.parse(date).getDayOfMonth() + ", "
              + LocalDate.parse(date).getYear();
      barChart.append(formattedDate).append(": ");
      for (int i = 0; i < numAsterisks; i += 1) {
        barChart.append("*");
      }
      barChart.append("\n");
    }
    barChart.append("\n");
    barChart.append(String.format("Scale: * = %d", scale));
    return barChart.toString();
  }

  @Override
  public void loadPortfolio(String portfolioName) throws IOException {
    StockPortfolio portfolio;
    try {
      this.getPortfolio(portfolioName);
      throw new IllegalArgumentException("Portfolio is already loaded");
    } catch (IllegalArgumentException e) {
      portfolio = new BasicStockPortfolio(portfolioName);
      portfolio.loadData();
      portfolios.add(portfolio);

    }
  }


}
