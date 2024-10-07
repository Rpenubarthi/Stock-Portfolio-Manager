package controller.commands;

import java.time.LocalDate;
import java.util.List;

import controller.StockCommand;
import model.BasicStock;
import model.Stock;
import model.addedfeatures.BetterStockModel;
import view.StockView;

/**
 * This class represents the command XDayMovingAverage. It takes the inputs and sets them as fields
 * and performs the go method to execute the command.
 */
public class XDayMovingAverage implements StockCommand {
  int x;
  LocalDate currDate;
  String ticker;

  /**
   * Constructor to parse the inputs.
   *
   * @param inputs the user inputs.
   */
  public XDayMovingAverage(List<String> inputs) {
    this.ticker = inputs.get(1);
    this.x = Integer.parseInt(inputs.get(2));
    String year = inputs.get(3);
    String month = inputs.get(4);
    String day = inputs.get(5);
    try {
      this.currDate = LocalDate.of(Integer.parseInt(year),
              Integer.parseInt(month), Integer.parseInt(day));
    } catch (Exception e) {
      throw new IllegalArgumentException("Invalid date");
    }
  }

  @Override
  public void performCommand(BetterStockModel model, StockView view) throws Exception {
    try {
      Stock stock = new BasicStock(ticker);
      double movingAverage = model.xDayMovingAverage(stock, x, currDate);
      view.writeMessage(x + "-day moving average for " + ticker + " on "
              + currDate + " is: $" + movingAverage);
    } catch (Exception e) {
      view.writeMessage(e.getMessage());
    }
  }
}
