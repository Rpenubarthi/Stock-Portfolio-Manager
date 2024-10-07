package controller.commands;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import controller.StockCommand;
import model.addedfeatures.BetterStockModel;
import view.StockView;

/**
 * This class represents the command RebalancePortfolio. It takes the inputs and sets them as fields
 * and performs the go method to execute the command.
 */
public class RebalancePortfolio implements StockCommand {
  String portfolioName;
  int weightSize;
  Map<String, Double> weights;
  LocalDate date;

  /**
   * Constructor to parse the inputs.
   *
   * @param inputs User inputs
   */
  public RebalancePortfolio(List<String> inputs) {
    this.portfolioName = inputs.get(1);
    this.weightSize = Integer.parseInt(inputs.get(2));
    this.weights = new HashMap<>();
    int index = 3;
    for (int i = 0; i < weightSize; i++) {
      String ticker = inputs.get(index);
      double weight = Double.parseDouble(inputs.get(index + 1));
      this.weights.put(ticker, weight);
      index += 2;
    }

    int year = Integer.parseInt(inputs.get(index));
    int month = Integer.parseInt(inputs.get(index + 1));
    int day = Integer.parseInt(inputs.get(index + 2));
    try {
      this.date = LocalDate.of(year, month, day);
    } catch (Exception e) {
      throw new IllegalArgumentException("Invalid date");
    }
  }

  @Override
  public void performCommand(BetterStockModel model, StockView view) throws Exception {
    try {
      model.rebalance(portfolioName, weights, date);
      view.writeMessage("Rebalance success!");
    } catch (Exception e) {
      view.writeMessage(e.getMessage());
    }
  }
}
