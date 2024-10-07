package controller.commands;

import java.util.List;

import controller.StockCommand;
import model.addedfeatures.BetterStockModel;
import view.StockView;

/**
 * This class represents the command LoadPortfolio. It takes the inputs and sets them as fields
 * and performs the go method to execute the command.
 */
public class LoadPortfolio implements StockCommand {
  String portfolioName;

  /**
   * Constructor to parse the inputs.
   *
   * @param inputs User inputs
   */
  public LoadPortfolio(List<String> inputs) {
    portfolioName = inputs.get(1);
  }

  @Override
  public void performCommand(BetterStockModel model, StockView view) throws Exception {
    try {
      model.loadPortfolio(portfolioName);
      view.writeMessage("Retrieved Portfolio: " + portfolioName);
    } catch (Exception e) {
      view.writeMessage(e.getMessage());
    }
  }
}
