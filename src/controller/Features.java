package controller;

import java.time.LocalDate;
import java.util.List;

import view.GraphicalUserView;

/**
 * This is the Features interface. It acts as a controller for the GraphicalUserView.
 * It defines the promises for controlling the flow of the stock management application.
 * Implementations of this interface handles the interactions between the model and GUI
 * view components.
 */
public interface Features {
  /**
   * Sets the GUI view into the controller.
   *
   * @param view an object of GraphicalUserView.
   */
  void setView(GraphicalUserView view);

  /**
   * Method to pass the user inputs from the GUI view to an AddPortfolio StockCommand.
   *
   * @param typed User input
   * @throws Exception Invalid input
   */
  void addPortfolioOutput(String typed) throws Exception;

  /**
   * Method to pass the user inputs from the GUI view to a BuyStock StockCommand.
   *
   * @param list User input
   * @param date User inputted date
   * @throws Exception Invalid input
   */
  void buyStockOutput(List<String> list, LocalDate date) throws Exception;

  /**
   * Method to pass the user inputs from the GUI view to a SellStock StockCommand.
   *
   * @param list User input
   * @param date User inputted date
   * @throws Exception Invalid input
   */
  void sellStockOutput(List<String> list, LocalDate date) throws Exception;

  /**
   * Method to pass the user inputs from the GUI view to a GetAssetValue StockCommand.
   *
   * @param input User input
   * @param date  User inputted date
   * @throws Exception Invalid input
   */
  void totalAssetValueOutput(String input, LocalDate date) throws Exception;

  /**
   * Method to pass the user inputs from the GUI view to a GetComposition StockCommand.
   *
   * @param input User input
   * @param date  User inputted date
   * @throws Exception Invalid input
   */
  void getCompositionOutput(String input, LocalDate date) throws Exception;

  /**
   * Method to pass the user inputs from the GUI view to a LoadPortfolio StockCommand.
   *
   * @param typed User input
   * @throws Exception Invalid input
   */
  void addLoadPortfolioOutput(String typed) throws Exception;
}
