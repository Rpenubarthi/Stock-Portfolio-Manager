package controller.commands;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import controller.StockCommand;
import model.addedfeatures.BetterStockModel;
import view.StockView;

/**
 * This class represents the command GetDistribution. It takes the inputs and sets them as fields
 * and performs the go method to execute the command.
 */
public class GetDistribution implements StockCommand {
  String portfolioName;
  LocalDate date;

  /**
   * Constructor to parse the inputs.
   *
   * @param inputs User inputs
   */
  public GetDistribution(List<String> inputs) {
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

  @Override
  public void performCommand(BetterStockModel model, StockView view) throws Exception {
    try {
      Map<String, Double> distribution = model.getDistribution(portfolioName, date);
      view.writeMessage("Distribution of " + portfolioName + " is " + distribution.toString());
    } catch (Exception e) {
      view.writeMessage(e.getMessage());
    }
  }
}
