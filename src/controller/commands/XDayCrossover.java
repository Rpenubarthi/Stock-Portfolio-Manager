package controller.commands;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import controller.StockCommand;
import model.BasicStock;
import model.Stock;
import model.addedfeatures.BetterStockModel;
import view.StockView;

/**
 * This class represents the command XDayCrossover. It takes the inputs and sets them as fields
 * and performs the go method to execute the command.
 */
public class XDayCrossover implements StockCommand {
  int x;
  LocalDate startDate;
  LocalDate endDate;
  String ticker;

  /**
   * Constructor to parse the inputs.
   *
   * @param inputs the user inputs
   */
  public XDayCrossover(List<String> inputs) {
    this.ticker = inputs.get(1);
    this.x = Integer.parseInt(inputs.get(2));
    String year = inputs.get(3);
    String month = inputs.get(4);
    String day = inputs.get(5);
    try {
      this.startDate = LocalDate.of(Integer.parseInt(year),
              Integer.parseInt(month), Integer.parseInt(day));
    } catch (Exception e) {
      throw new IllegalArgumentException("Invalid date");
    }
    String endYear = inputs.get(6);
    String endMonth = inputs.get(7);
    String endDay = inputs.get(8);
    try {
      this.endDate = LocalDate.of(Integer.parseInt(endYear),
              Integer.parseInt(endMonth), Integer.parseInt(endDay));
    } catch (Exception e) {
      throw new IllegalArgumentException("Invalid date");
    }
  }

  @Override
  public void performCommand(BetterStockModel model, StockView view) throws Exception {
    try {
      Stock stock = new BasicStock(ticker);
      ArrayList<LocalDate> crossoverDates = model.xDayCrossover(x, startDate, endDate, stock);
      view.writeMessage(x + "-day crossover dates for " + ticker + " between "
              + startDate + " and " + endDate + " are: " + crossoverDates);
    } catch (Exception e) {
      view.writeMessage(e.getMessage());
    }
  }
}
