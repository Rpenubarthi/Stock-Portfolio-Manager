package model.addedfeatures;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import controller.StockProgram;
import model.AlphaVantageDemo;

/**
 * A representation of a portfolio that can hold stocks, get a snapshot at specific date,
 * as well as get the shares of a specific stock owned on a given date. it also stores
 * its information into a csv file.
 */
public class BasicStockPortfolio implements StockPortfolio {
  private final String portfolioName;
  private final List<String> tickers;
  private final File portfolioInfo;

  /**
   * The default constructor for the portfolio.
   *
   * @param portfolioName The portfolio name
   * @throws IOException When the input file is not there
   */
  public BasicStockPortfolio(String portfolioName) throws IOException {
    this.tickers = new ArrayList<>();
    this.portfolioName = portfolioName;
    File file = new File("src/model/PortfolioInfo.csv");
    if (file.exists()) {
      this.portfolioInfo = new File("src/model/PortfolioInfo.csv");
    } else {
      String path = StockProgram.class.getProtectionDomain().getCodeSource()
              .getLocation().getPath();
      file = new File(path);
      file = file.getParentFile();
      this.portfolioInfo = new File(file.getAbsolutePath() + "/PortfolioInfo.csv");
    }
    this.portfolioInfo.createNewFile();
  }

  @Override
  public String getPortfolioName() {
    return this.portfolioName;
  }

  @Override
  public List<String> getStocks() {
    return tickers;
  }

  @Override
  public void saveData(LocalDate date, double shares, String ticker)
          throws IOException {
    try {
      AlphaVantageDemo.callAPI(ticker);
    } catch (Exception e) {
      System.out.println(e.getMessage());
      throw new IllegalArgumentException(e.getMessage());
    }
    boolean isHeader = (portfolioInfo.length() == 0);
    if (!tickers.contains(ticker)) {
      tickers.add(ticker);
    }
    try (FileWriter portfolioInfoWriter = new FileWriter(portfolioInfo, true)) {

      if (isHeader) {
        portfolioInfoWriter.write("Date,Shares,Ticker,Portfolio\n");
      }
      String info = date.toString() + "," + shares
              + "," + ticker + "," + portfolioName + "\n";
      portfolioInfoWriter.write(info);
    }
  }

  @Override
  public double getCurrentShares(String ticker, LocalDate dateSold)
          throws FileNotFoundException {
    double currentShares = 0;
    if (portfolioInfo.length() < 0) {
      return 0;
    }
    Scanner sc = new Scanner(portfolioInfo);
    sc.nextLine();
    String[] currentLine;
    while (sc.hasNextLine()) {
      currentLine = sc.nextLine().split(",");
      if (ticker.equals(currentLine[2])
              && LocalDate.parse(currentLine[0]).until(dateSold, ChronoUnit.DAYS) >= 0
              && portfolioName.equals(currentLine[3])) {
        currentShares += Double.parseDouble(currentLine[1]);
      }
    }
    return currentShares;
  }


  @Override
  public Map<String, Double> getPortfolioSnapshot(LocalDate date) throws FileNotFoundException {
    HashMap<String, Double> stockShares = new HashMap<>();
    for (String ticker : tickers) {
      double shares = this.getCurrentShares(ticker, date);
      stockShares.put(ticker, shares);
    }
    return stockShares;
  }

  @Override
  public void loadData() throws FileNotFoundException {
    Scanner sc = new Scanner(portfolioInfo);
    if (sc.hasNextLine()) {
      sc.nextLine();
    } else {
      throw new IllegalArgumentException("No portfolio to load");
    }
    String[] currentLine;
    while (sc.hasNextLine()) {
      currentLine = sc.nextLine().split(",");
      String ticker = currentLine[2];
      if (!tickers.contains(ticker) && currentLine[3].equals(portfolioName)) {
        tickers.add(ticker);
      }
    }
  }

  @Override
  public boolean equals(Object obj) {
    if (obj instanceof StockPortfolio) {
      return ((StockPortfolio) obj).getPortfolioName().equals(this.portfolioName);
    }
    return false;
  }

  @Override
  public int hashCode() {
    return this.getPortfolioName().hashCode();
  }
}
