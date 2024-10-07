package controller.commands;

import java.util.List;

import controller.StockCommand;
import model.addedfeatures.BetterStockModel;
import view.StockView;

/**
 * This class represents the command AddToPortfolio. It takes the inputs and sets them as fields
 * and performs the go method to execute the command.
 */
public class AddPortfolio implements StockCommand {
  String portfolioName;

  /**
   * Constructor to parse the inputs.
   *
   * @param inputs the user inputs.
   */
  public AddPortfolio(List<String> inputs) {
    this.portfolioName = inputs.get(1);
  }


  @Override
  public void performCommand(BetterStockModel model, StockView view) throws Exception {
    try {
      model.addPortfolio(portfolioName);
      view.writeMessage("Added Portfolio: " + portfolioName);
    } catch (Exception e) {
      view.writeMessage(e.getMessage());
    }
  }
}
