package controller.commands;

import java.util.List;

import controller.StockCommand;
import model.addedfeatures.BetterStockModel;
import view.StockView;

/**
 * This class represents the command SetDataFile. It takes the inputs and sets them as fields
 * and performs the go method to execute the command.
 */
public class InputFile implements StockCommand {
  String file;

  /**
   * Constructor to parse the inputs.
   *
   * @param inputs User inputs
   */
  public InputFile(List<String> inputs) {
    this.file = inputs.get(1);
  }

  @Override
  public void performCommand(BetterStockModel model, StockView view) throws Exception {
    try {
      model.setDataFile(file);
      view.writeMessage("file inputted!");
    } catch (Exception e) {
      view.writeMessage(e.getMessage());
    }
  }
}
