package controller;

import java.util.ArrayList;
import java.util.List;

import controller.commands.AddPortfolio;
import controller.commands.BuyStock;
import controller.commands.GetAssetValue;
import controller.commands.GetComposition;
import controller.commands.GetDistribution;
import controller.commands.InputFile;
import controller.commands.LoadPortfolio;
import controller.commands.NetGain;
import controller.commands.PerformanceOverTime;
import controller.commands.RebalancePortfolio;
import controller.commands.SellStock;
import controller.commands.XDayCrossover;
import controller.commands.XDayMovingAverage;
import model.addedfeatures.BetterStockModel;
import view.StockView;
import view.StockViewImpl;

/**
 * This class is an implementation of stockController.
 * It coordinates the interactions between the stock model and the stock view, managing the
 * overall flow of the application.
 */
public class StockController implements Controller {
  private final BetterStockModel model;
  private final StockView view;

  /**
   * Constructor that takes in the input, output, and model for a constructor.
   * @param out   the output.
   * @param in    the user input.
   * @param model an object of StockModel
   */
  public StockController(Appendable out, Readable in, BetterStockModel model) {
    this.model = model;
    this.view = new StockViewImpl(in, out);
  }

  /**
   * This method starts the controller.
   */
  public void startProgram() {
    view.writeMessage("Welcome to Stock Manager!");
    printMenu();
    List<String> inputs = new ArrayList<>();
    while (true) {
      String input = view.readInput();
      inputs.add(input);
      StockCommand cmd = null;
      try {
        switch (inputs.get(0)) {
          case "net-gain":
            if (inputs.size() < 8) {
              continue;
            }
            if (inputs.size() > 8) {
              view.writeMessage("Usage: net-gain ticker start-date end-date");
              inputs = new ArrayList<>();
              return;
            }
            cmd = new NetGain(inputs);
            break;
          case "x-day-moving-average":
            if (inputs.size() < 6) {
              continue;
            }
            if (inputs.size() > 6) {
              view.writeMessage("Usage: x-day-moving-average ticker x start-date");
              inputs = new ArrayList<>();
              break;
            }
            cmd = new XDayMovingAverage(inputs);
            break;
          case "x-day-crossover":
            if (inputs.size() < 9) {
              continue;
            }
            if (inputs.size() > 9) {
              view.writeMessage("Usage: x-day-crossover ticker x start-date end-date");
              inputs = new ArrayList<>();
              break;
            }
            cmd = new XDayCrossover(inputs);
            break;
          case "add-portfolio":
            if (inputs.size() < 2) {
              view.writeMessage("\n");
              continue;
            }
            if (inputs.size() > 2) {
              view.writeMessage("Usage: add-portfolio portfolio-name");
              inputs = new ArrayList<>();
              break;
            }
            cmd = new AddPortfolio(inputs);
            break;
          case "get-asset-value":
            if (inputs.size() < 5) {
              continue;
            }
            if (inputs.size() > 5) {
              view.writeMessage("Usage: get-asset-value portfolio-name date");
              inputs = new ArrayList<>();
              break;
            }
            cmd = new GetAssetValue(inputs);
            break;
          case "input-file":
            if (inputs.size() < 2) {
              continue;
            }
            cmd = new InputFile(inputs);
            break;
          case "menu":
            printMenu();
            inputs = new ArrayList<>();
            continue;
          case "q":
          case "quit":
            view.writeMessage("Thank you for using stock program!");
            return;
          case "buy-stock":
            if (inputs.size() < 7) {
              continue;
            }
            if (inputs.size() > 7) {
              view.writeMessage("Usage: buy-stock ticker shares portfolio-name date");
              inputs = new ArrayList<>();
            }
            cmd = new BuyStock(inputs);
            break;
          case "sell-stock":
            if (inputs.size() < 7) {
              continue;
            }
            if (inputs.size() > 7) {
              view.writeMessage("Usage: sell-stock ticker shares portfolio-name date");
              inputs = new ArrayList<>();
              break;
            }
            cmd = new SellStock(inputs);
            break;
          case "rebalance":
            if (inputs.size() < 3) {
              continue;
            }
            if (inputs.size() < 6 + 2 * Integer.parseInt(inputs.get(2))) {
              continue;
            }
            cmd = new RebalancePortfolio(inputs);
            break;
          case "get-distribution":
            if (inputs.size() < 5) {
              continue;
            }
            if (inputs.size() > 5) {
              view.writeMessage("Usage: get-distribution portfolio date");
              inputs = new ArrayList<>();
              break;
            }
            cmd = new GetDistribution(inputs);
            break;
          case "get-performance-over-time":
            if (inputs.size() < 8) {
              continue;
            }
            if (inputs.size() > 8) {
              view.writeMessage("Usage: get-performance-over-time portfolio-name start-date "
                     + "end-date");
              inputs = new ArrayList<>();
              break;
            }
            cmd = new PerformanceOverTime(inputs);
            break;
          case "get-composition":
            if (inputs.size() < 5) {
              continue;
            }
            if (inputs.size() > 5) {
              view.writeMessage("Usage: get-composition portfolio date");
              inputs = new ArrayList<>();
              break;
            }
            cmd = new GetComposition(inputs);
            break;
          case "load-portfolio":
            if (inputs.size() < 2) {
              continue;
            }
            if (inputs.size() > 2) {
              view.writeMessage("Usage: load-portfolio portfolio-name");
              inputs = new ArrayList<>();
              break;
            }
            cmd = new LoadPortfolio(inputs);
            break;
          default:
            view.writeMessage("Invalid command. Please try again.");
            inputs = new ArrayList<>();
        }
        inputs = new ArrayList<>();
        cmd.performCommand(model, view);
        printMenu();
      } catch (Exception e) {
        if (e.toString().contains("string")) {
          view.writeMessage("Fractional Shares are not allowed");
        } else {
          view.writeMessage(e.getMessage());
        }
      }
    }
  }

  /**
   * This method prints a simple menu.
   */
  public void printMenu() {
    view.writeMessage("Supported user instructions are:");
    view.writeMessage("Separate each entry value by a new line");
    view.writeMessage("Enter x as an integer, and enter date by inputting the year, "
            + "month, date on separate lines in that order.");
    view.writeMessage("add-portfolio ticker");
    view.writeMessage("buy-stock ticker shares portfolio-name date");
    view.writeMessage("sell-stock ticker shares portfolio-name date");
    view.writeMessage("net-gain ticker start-date end-date");
    view.writeMessage("x-day-moving-average ticker x start-date");
    view.writeMessage("x-day-crossover ticker x start-date end-date");
    view.writeMessage("get-asset-value portfolio-name date");
    view.writeMessage("get-composition portfolio date");
    view.writeMessage("get-distribution portfolio date");
    view.writeMessage("get-composition portfolio date");
    view.writeMessage("rebalance portfolio-name num-of-weights weights "
            + "(ticker, new line, weight as a decimal) date");
    view.writeMessage("get-performance-over-time portfolio-name start-date end-date");
    view.writeMessage("load-portfolio portfolio-name");
    view.writeMessage("menu (Print supported instruction list)");
    view.writeMessage("q or quit (quit the program)");
  }
}
