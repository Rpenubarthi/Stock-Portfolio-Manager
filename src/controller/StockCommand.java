package controller;

import model.addedfeatures.BetterStockModel;
import view.StockView;

/**
 * This interface represents a stock command. A stock command can be various supported by the model.
 * It promises a method that will execute specific stock-related commands
 * within a given stock model and view context.
 */
public interface StockCommand {

  /**
   * This method executes the specific commands.
   *
   * @param model an object of BetterStockModel.
   * @param view  an object of StockView.
   * @throws Exception for an invalid input.
   */
  void performCommand(BetterStockModel model, StockView view) throws Exception;
}

