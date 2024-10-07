package controller.commands;

import java.time.LocalDate;
import java.util.List;

import controller.StockCommand;
import model.addedfeatures.BetterStockModel;
import view.StockView;

/**
 * This class represents the command GetAssetvalue. It takes the inputs and sets them as fields
 * and performs the go method to execute the command.
 */
public class GetAssetValue implements StockCommand {
  LocalDate date;
  String portfolioName;

  /**
   * Constructor to parse the inputs.
   *
   * @param inputs the user inputs.
   */
  public GetAssetValue(List<String> inputs) {
    this.portfolioName = inputs.get(1);
    String year = inputs.get(2);
    String month = inputs.get(3);
    String day = inputs.get(4);
    try {
      this.date = LocalDate.of(Integer.parseInt(year), Integer.parseInt(month),
              Integer.parseInt(day));
    } catch (Exception e) {
      throw new IllegalArgumentException("Invalid date");
    }
  }

  /**
   * Constructor to parse the inputs for the GUI controller.
   *
   * @param input user inputs
   * @param date  date inputted
   */
  public GetAssetValue(String input, LocalDate date) {
    this.portfolioName = input;
    this.date = date;
  }

  @Override
  public void performCommand(BetterStockModel model, StockView view) throws Exception {
    try {
      double assetValue = model.totalAssetValue(portfolioName, date);
      view.writeMessage("Asset value is $" + assetValue + " as of " + date
              + " for " + portfolioName);
    } catch (Exception e) {
      view.writeMessage(e.getMessage());
    }
  }
}
