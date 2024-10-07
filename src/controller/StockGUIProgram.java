package controller;

import java.io.IOException;

import model.addedfeatures.BetterBasicStockModel;
import model.addedfeatures.BetterStockModel;
import view.JFrameView;

/**
 * This is class StockGUIProgram to start the visual program.
 */
public class StockGUIProgram {
  /**
   * The entry point of the visual stock management application.
   * This method initializes the model and controller and sets the view to the controller.
   *
   * @param args args command-line arguments passed to the application.
   * @throws IOException IOException if an I/O error occurs during program execution.
   */
  public static void main(String[] args) throws IOException {
    BetterStockModel model = new BetterBasicStockModel();
    Features controller = new FeaturesController(model);
    controller.setView(new JFrameView());
  }
}
