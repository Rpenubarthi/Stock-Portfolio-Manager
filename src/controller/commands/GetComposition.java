package controller.commands;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import controller.StockCommand;
import model.addedfeatures.BetterStockModel;
import view.StockView;

/**
 * This class represents the command GetComposition. It takes the inputs and sets them as fields
 * and performs the go method to execute the command.
 */
public class GetComposition implements StockCommand {
  String portfolioName;
  LocalDate date;

  /**
   * Constructor to parse the inputs.
   *
   * @param inputs User inputs
   */
  public GetComposition(List<String> inputs) {
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
  public GetComposition(String input, LocalDate date) {
    this.portfolioName = input;
    this.date = date;
  }

  @Override
  public void performCommand(BetterStockModel model, StockView view) throws Exception {
    try {
      Map<String, Double> composition = model.getComposition(portfolioName, date);
      view.writeMessage("Composition of " + portfolioName + " is " + composition.toString());
    } catch (Exception e) {
      view.writeMessage(e.getMessage());
    }
  }
}
