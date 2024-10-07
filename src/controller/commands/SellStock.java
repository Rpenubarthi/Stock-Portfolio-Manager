package controller.commands;

import java.time.LocalDate;
import java.util.List;

import controller.StockCommand;
import model.addedfeatures.BetterStockModel;
import view.StockView;

/**
 * This class represents the command SellStock. It takes the inputs and sets them as fields
 * and performs the go method to execute the command.
 */
public class SellStock implements StockCommand {
  String ticker;
  int shares;
  String portfolioName;
  LocalDate dateBought;

  /**
   * Constructor to parse the inputs.
   *
   * @param inputs User inputs
   */
  public SellStock(List<String> inputs) {
    this.ticker = inputs.get(1);
    this.shares = Integer.parseInt(inputs.get(2));
    this.portfolioName = inputs.get(3);
    String year = inputs.get(4);
    String month = inputs.get(5);
    String day = inputs.get(6);
    try {
      this.dateBought = LocalDate.of(Integer.parseInt(year), Integer.parseInt(month),
              Integer.parseInt(day));
    } catch (Exception e) {
      throw new IllegalArgumentException("Invalid date");
    }
  }

  /**
   * Constructor to parse the inputs for the GUI controller.
   *
   * @param inputs user inputs
   * @param date   date inputted
   */
  public SellStock(List<String> inputs, LocalDate date) {
    this.ticker = inputs.get(0);
    this.shares = Integer.parseInt(inputs.get(1));
    this.portfolioName = inputs.get(2);
    this.dateBought = date;
  }

  @Override
  public void performCommand(BetterStockModel model, StockView view) throws Exception {
    try {
      model.sellStock(ticker, shares, portfolioName, dateBought);
      view.writeMessage("Sold: " + shares + " shares of " + ticker);
    } catch (Exception e) {
      view.writeMessage(e.getMessage());
    }
  }
}
