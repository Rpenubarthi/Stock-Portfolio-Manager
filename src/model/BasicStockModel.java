package model;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Scanner;

import controller.StockProgram;

/**
 * A BasicStockModel is a StockModel that can hold portfolios of stocks and do simple
 * stock calculations such as finding the moving average and the crossovers.
 */
public class BasicStockModel implements StockModel {


  protected File cachedStockValues;
  protected File cachedStockNames;
  ArrayList<ArrayList<Stock>> portfolios;


  /**
   * Default constructor for the BasicStockModel.
   *
   * @throws IOException When the file does not exist.
   */
  public BasicStockModel() throws IOException {
    portfolios = new ArrayList<>();
    File file = new File("src/model/cachedStockValues.csv");
    if (file.exists()) {
      this.cachedStockValues = file;
      this.cachedStockNames = new File("src/model/cachedStockNames.csv");
    } else {
      String path = StockProgram.class.getProtectionDomain().getCodeSource()
              .getLocation().getPath();
      file = new File(path);
      file = file.getParentFile();
      this.cachedStockValues = new File(file.getAbsolutePath() + "/CachedStockValues.csv");
      this.cachedStockNames = new File(file.getAbsolutePath() + "/CachedStockNames.csv");

    }
    this.cachedStockNames.createNewFile();
    this.cachedStockValues.createNewFile();
  }

  @Override
  public void setDataFile(String cachedStockValues) throws IOException {
    this.cachedStockValues = new File(cachedStockValues);
    try {
      this.cachedStockValues.createNewFile();
    } catch (IOException e) {
      throw new IOException("Invalid file path");
    }
  }

  @Override
  public void updatePortfolio(Stock stock, int index) {
    ArrayList<Stock> portfolio = new ArrayList<>();
    if (portfolios.isEmpty() || portfolios.size() == index) {
      portfolio.add(stock);
      portfolios.add(portfolio);
      return;
    }

    if (portfolios.size() > index) {
      portfolio = portfolios.get(index);
      for (Stock value : portfolio) {
        if (stock.equals(value)) {
          value.addShares(stock.getShares());
          return;
        }
      }
      portfolio.add(stock);
      portfolios.add(portfolio);
      return;
    }
    throw new IllegalArgumentException("No portfolio at given index.");
  }


  @Override
  public double totalAssetValue(LocalDate date, int index) throws IOException {
    double totalAssetValue = 0;
    if (portfolios.isEmpty()) {
      return totalAssetValue;
    }
    ArrayList<Stock> portfolio = portfolios.get(index);
    for (Stock stock : portfolio) {
      totalAssetValue += (stock.getShares() * this.getStockClosingValue(stock, date));
    }
    return Double.parseDouble(String.format("%.2f", totalAssetValue));
  }


  @Override
  public double netGain(LocalDate dateStart, LocalDate dateEnd, Stock stock) throws IOException {
    try {
      this.checkValidStockIntervals(dateStart, dateEnd);
    } catch (IllegalArgumentException e) {
      throw new IllegalArgumentException(e.getMessage());
    }
    double startingValue = this.getStockClosingValue(stock, dateStart);
    double endingValue = this.getStockClosingValue(stock, dateEnd);
    return Double.parseDouble(String.format("%.2f", endingValue - startingValue));
  }

  @Override
  public double xDayMovingAverage(Stock stock, int x, LocalDate currDate) throws IOException {
    if (x < 0) {
      throw new IllegalArgumentException("X cannot be negative");
    }
    return Double.parseDouble(String.format("%.2f",
            this.getAverageStockValue(stock, currDate.minusDays(x), currDate)));
  }

  @Override
  public ArrayList<LocalDate> xDayCrossover(int x, LocalDate startDate, LocalDate endDate,
                                            Stock stock) throws IOException {
    if (x < 0) {
      throw new IllegalArgumentException("X cannot be negative");
    }
    try {
      this.checkValidStockIntervals(startDate, endDate);
    } catch (IllegalArgumentException e) {
      throw new IllegalArgumentException(e.getMessage());
    }
    ArrayList<LocalDate> crossoverDays = new ArrayList<>();
    for (int i = 0; i < this.totalStockDaysBetween(stock, startDate, endDate) - (x - 1); i += 1) {
      LocalDate currDate = endDate.minusDays(i);
      while (!this.hasDate(stock, currDate)) {
        currDate = currDate.minusDays(i);
        i += 1;
      }
      double currentMovingAverage = this.xDayMovingAverage(stock, x, currDate);

      if (getStockClosingValue(stock, currDate) > currentMovingAverage
              && this.hasDate(stock, currDate)) {
        crossoverDays.add(currDate);
      }
    }

    return crossoverDays;
  }

  private double getAverageStockValue(Stock stock, LocalDate startDate, LocalDate endDate)
          throws IOException {
    double totalValue = 0;
    String ticker = stock.getSymbol();
    Scanner sc = new Scanner(cachedStockValues);
    sc.nextLine();
    long daysBetween = startDate.until(endDate, ChronoUnit.DAYS);
    int currentNumDays = 0;
    String[] currentLine = sc.nextLine().split(",");
    if (endDate.isAfter(LocalDate.now())) {
      throw new IllegalArgumentException("Date entered cannot be after today.");
    }
    while (endDate.isBefore(LocalDate.parse(currentLine[0])) || !ticker.equals(currentLine[6])) {
      currentLine = sc.nextLine().split(",");
    }

    while (currentNumDays < daysBetween) {
      if (sc.hasNextLine()) {
        if (this.hasDate(stock, LocalDate.parse(currentLine[0]))) {
          totalValue += this.getStockClosingValue(stock, LocalDate.parse(currentLine[0]));
          currentNumDays += 1;
        }
        currentLine = sc.next().split(",");
      } else {
        totalValue += this.getStockClosingValue(stock, LocalDate.parse(currentLine[0]));
        break;
      }

    }
    return totalValue / currentNumDays;
  }

  private void checkValidStockIntervals(LocalDate startDate, LocalDate endDate)
          throws IllegalArgumentException {
    if (startDate.isAfter(endDate) || startDate.isEqual(endDate)) {
      throw new IllegalArgumentException("Start date cannot be after or equal to end date.");
    }
  }

  /**
   * Gets the closing value of a stock on a given date. If the date doesn't have a closing value,
   * looks at the closest date before that has a closing value.
   *
   * @param stock The stock to find the closing value of
   * @param date  The date to get the closing value
   * @return The closing value as a double
   * @throws IOException When the input files with the stock info does not exist
   */
  protected double getStockClosingValue(Stock stock, LocalDate date) throws IOException {
    if (!this.hasStock(stock)) {
      try {
        this.addToCache(stock);
      } catch (IllegalArgumentException e) {
        throw new IllegalArgumentException(e.getMessage());
      }
    }
    if (date.isBefore(this.getOldestStockDate(stock))
            || date.isAfter(this.getMostRecentStockDate(stock))) {
      throw new IllegalArgumentException("No data at the date: " + date.toString());
    }
    this.updateStock(stock, date);
    Scanner sc = new Scanner(cachedStockValues);
    sc.nextLine();
    while (sc.hasNextLine()) {
      String[] currentLine = sc.nextLine().split(",");
      if (stock.getSymbol().equals(currentLine[6])) {
        if (date.equals(LocalDate.parse(currentLine[0]))
                || date.isAfter(LocalDate.parse(currentLine[0]))) {
          return Double.parseDouble(currentLine[4]);
        }
      }
    }
    return -1;
  }

  private int totalStockDaysBetween(Stock stock, LocalDate startDate,
                                    LocalDate endDate) throws FileNotFoundException {

    int count = 0;
    Scanner sc = new Scanner(cachedStockValues);
    sc.nextLine();
    String[] currentLine = sc.nextLine().split(",");

    while (LocalDate.parse(currentLine[0]).isAfter(endDate)
            || !currentLine[6].equals(stock.getSymbol())) {
      currentLine = sc.nextLine().split(",");
    }
    while (LocalDate.parse(currentLine[0]).isAfter(startDate)
            && currentLine[6].equals(stock.getSymbol())) {
      count += 1;
      currentLine = sc.nextLine().split(",");
    }
    return count + 1;
  }

  private LocalDate getMostRecentStockDate(Stock stock) throws FileNotFoundException {
    String ticker = stock.getSymbol();
    if (this.hasStock(stock)) {
      Scanner sc = new Scanner(cachedStockValues);
      while (sc.hasNextLine()) {
        String[] currentLine = sc.nextLine().split(",");
        if (currentLine[6].equals(ticker)) {
          return LocalDate.parse(currentLine[0]);
        }
      }
    }
    return LocalDate.MIN;
  }

  private LocalDate getOldestStockDate(Stock stock) throws FileNotFoundException {
    String ticker = stock.getSymbol();
    LocalDate oldestDate = LocalDate.MAX;
    if (this.hasStock(stock)) {
      Scanner sc = new Scanner(cachedStockValues);
      String[] currentLine = {""};
      while (sc.hasNextLine()) {
        currentLine = sc.nextLine().split(",");
        if (currentLine[6].equals(ticker)) {
          oldestDate = LocalDate.parse(currentLine[0]);
          break;
        }
      }
      while (currentLine[6].equals(ticker)) {
        oldestDate = LocalDate.parse(currentLine[0]);
        if (sc.hasNextLine()) {
          currentLine = sc.nextLine().split(",");
        } else {
          break;
        }
      }
    }
    return oldestDate;
  }

  private void addToCache(Stock stock) throws IOException {
    String ticker = stock.getSymbol();
    // Allows for the configuration of the header of the csv file
    boolean isHeader = (cachedStockValues.length() == 0);
    Scanner sc;
    try {
      String data = AlphaVantageDemo.callAPI(ticker);
      sc = new Scanner(data);
    } catch (IllegalArgumentException e) {
      throw new IllegalArgumentException(e.getMessage());
    }

    if (!isHeader) {
      sc.nextLine();
    }
    try {
      FileWriter stockDataWriter = new FileWriter(cachedStockValues, true);
      FileWriter stockNameWriter = new FileWriter(cachedStockNames, true);
      while (sc.hasNextLine()) {
        String currentLine = sc.nextLine();
        if (isHeader) {
          stockDataWriter.write(currentLine + ",ticker\n");
          isHeader = false;
        } else {
          stockDataWriter.write(currentLine + "," + ticker + "\n");
        }
      }
      stockNameWriter.write(ticker + "\n");
      stockDataWriter.close();
      stockNameWriter.close();
    } catch (IOException e) {
      throw new IOException(e.getMessage());
    }
  }

  private boolean hasStock(Stock stock) throws FileNotFoundException {
    Scanner sc = new Scanner(cachedStockNames);
    while (sc.hasNextLine()) {
      String currentLine = sc.nextLine();
      if (currentLine.equals(stock.getSymbol())) {
        return true;
      }
    }
    return false;
  }

  private boolean hasDate(Stock stock, LocalDate date) throws FileNotFoundException {
    Scanner sc = new Scanner(cachedStockValues);
    sc.nextLine();
    while (sc.hasNextLine()) {
      String[] currentLine = sc.nextLine().split(",");
      if (stock.getSymbol().equals(currentLine[6])) {
        if (date.equals(LocalDate.parse(currentLine[0]))) {
          return true;
        } else if (date.isAfter(LocalDate.parse(currentLine[0]))) {
          return false;
        }
      }
    }
    return false;
  }

  private void updateStock(Stock stock, LocalDate recentDate) throws IOException {
    String ticker = stock.getSymbol();
    if (recentDate.isAfter(LocalDate.now())) {
      throw new IllegalArgumentException("Date can't be after today.");
    }

    if (this.hasStock(stock)) {
      if (this.getMostRecentStockDate(stock).isBefore(recentDate)
              && (!recentDate.isEqual(LocalDate.now())
              || this.getMostRecentStockDate(stock).until(recentDate, ChronoUnit.DAYS) > 1)) {
        Scanner existingData = new Scanner(cachedStockValues);
        existingData.nextLine();
        String currentLine;
        String data;
        try {
          data = AlphaVantageDemo.callAPI(ticker);
        } catch (IllegalArgumentException e) {
          throw new IllegalArgumentException(e.getMessage());
        }
        Scanner newData = new Scanner(data);

        try (FileWriter stockDataWriter = new FileWriter(cachedStockValues, false)) {
          currentLine = existingData.nextLine();
          while (existingData.hasNextLine()) {
            if (currentLine.split(",")[6].equals(ticker)) {
              while (newData.hasNextLine()) {
                stockDataWriter.write(newData.nextLine());
                existingData.nextLine();
              }
            } else {
              currentLine = existingData.nextLine();
              stockDataWriter.write(currentLine);
            }
          }
        }
      }
    } else {
      this.addToCache(stock);
    }
  }

}
