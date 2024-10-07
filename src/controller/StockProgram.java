package controller;

import java.io.IOException;
import java.io.InputStreamReader;

import model.addedfeatures.BetterBasicStockModel;
import model.addedfeatures.BetterStockModel;

/**
 * This is class StockProgram to start the program.
 */
public class StockProgram {

  /**
   * The entry point of the stock management application.
   * This method initializes the model and controller, then starts the program.
   *
   * @param args args command-line arguments passed to the application.
   * @throws IOException IOException if an I/O error occurs during program execution.
   */
  public static void main(String[] args) throws IOException {
    BetterStockModel model = new BetterBasicStockModel();
    Controller controller = new StockController(System.out, new InputStreamReader(System.in),
            model);
    controller.startProgram();
  }
}
