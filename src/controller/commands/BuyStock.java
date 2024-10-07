package controller.commands;

import java.time.LocalDate;
import java.util.List;

import controller.StockCommand;
import model.addedfeatures.BetterStockModel;
import view.StockView;

/**
 * This class represents the command BuyStock. It takes the inputs and sets them as fields
 * and performs the go method to execute the command.
 */
public class BuyStock implements StockCommand {
  String ticker;
  int shares;
  String portfolioName;
  LocalDate dateBought;

  /**
   * Constructor to parse the inputs.
   *
   * @param inputs User inputs
   */
  public BuyStock(List<String> inputs) {
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
   * @param inputs     user inputs
   * @param dateBought date inputted
   */
  public BuyStock(List<String> inputs, LocalDate dateBought) {
    this.ticker = inputs.get(0);
    this.shares = Integer.parseInt(inputs.get(1));
    this.portfolioName = inputs.get(2);
    this.dateBought = dateBought;
  }

  @Override
  public void performCommand(BetterStockModel model, StockView view) throws Exception {
    try {
      model.buyStock(ticker, shares, portfolioName, dateBought);
      view.writeMessage("Bought: " + shares + " shares of " + ticker);
    } catch (Exception e) {
      view.writeMessage(e.getMessage());
    }
  }
}
