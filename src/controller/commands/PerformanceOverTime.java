package controller.commands;

import java.time.LocalDate;
import java.util.List;

import controller.StockCommand;
import model.addedfeatures.BetterStockModel;
import view.StockView;

/**
 * This class represents the command PerformanceOverTime.
 * It takes the inputs and sets them as fields
 * and performs the go method to execute the command.
 */
public class PerformanceOverTime implements StockCommand {
  Boolean isStock;
  String portfolioName;
  LocalDate startDate;
  LocalDate endDate;

  /**
   * Constructor to parse the inputs.
   *
   * @param inputs User inputs
   */
  public PerformanceOverTime(List<String> inputs) {
    if (!inputs.get(1).equals("stock") && !inputs.get(1).equals("portfolio")) {
      throw new IllegalArgumentException("The first input should be 'stock' or 'portfolio'.");
    }
    this.isStock = inputs.get(1).equals("stock");
    this.portfolioName = inputs.get(1);
    String year = inputs.get(2);
    String month = inputs.get(3);
    String day = inputs.get(4);
    try {
      this.startDate = LocalDate.of(Integer.parseInt(year), Integer.parseInt(month),
              Integer.parseInt(day));
    } catch (Exception e) {
      throw new IllegalArgumentException("Invalid date");
    }
    String endYear = inputs.get(5);
    String endMonth = inputs.get(6);
    String endDay = inputs.get(7);
    try {
      this.endDate = LocalDate.of(Integer.parseInt(endYear), Integer.parseInt(endMonth),
              Integer.parseInt(endDay));
    } catch (Exception e) {
      throw new IllegalArgumentException("Invalid date");
    }
  }

  @Override
  public void performCommand(BetterStockModel model, StockView view) throws Exception {
    try {
      model.performanceOverTime(isStock, portfolioName, startDate, endDate);
    } catch (Exception e) {
      view.writeMessage(e.getMessage());
    }
  }

}
