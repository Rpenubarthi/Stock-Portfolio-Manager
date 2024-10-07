package controller.commands;

import java.time.LocalDate;
import java.util.List;

import controller.StockCommand;
import model.BasicStock;
import model.Stock;
import model.addedfeatures.BetterStockModel;
import view.StockView;

/**
 * This class represents the command NetGain. It takes the inputs and sets them as fields
 * and performs the go method to execute the command.
 */
public class NetGain implements StockCommand {
  LocalDate startDate;
  LocalDate endDate;
  String ticker;

  /**
   * Constructor to parse the inputs.
   *
   * @param inputs the user inputs.
   */
  public NetGain(List<String> inputs) {
    this.ticker = inputs.get(1);
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
      Stock stock = new BasicStock(ticker);
      double netGain = model.netGain(startDate, endDate, stock);
      view.writeMessage("Net gain for " + ticker + " from " + startDate + " to " + endDate
              + " is: $" + netGain);
    } catch (Exception e) {
      view.writeMessage(e.getMessage());
    }
  }
}
