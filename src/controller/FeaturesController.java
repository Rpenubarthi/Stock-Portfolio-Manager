package controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import controller.commands.AddPortfolio;
import controller.commands.BuyStock;
import controller.commands.GetAssetValue;
import controller.commands.GetComposition;
import controller.commands.LoadPortfolio;
import controller.commands.SellStock;
import model.addedfeatures.BetterStockModel;
import view.GraphicalUserView;
import view.StockView;

/**
 * This is an implementation of the Features interface.
 * This controller controls the flow of inputs from the GraphicalUserView and the Stock Commands
 * and the view.
 */
public class FeaturesController implements Features {
  BetterStockModel model;
  StockView view;

  /**
   * Constructor to take in just the model.
   *
   * @param model A BetterStockModel object
   */
  public FeaturesController(BetterStockModel model) {
    this.model = model;
  }

  @Override
  public void setView(GraphicalUserView view) {
    this.view = view;
    view.addFeatures(this);
  }

  @Override
  public void addPortfolioOutput(String typed) throws Exception {
    List<String> portfolioName = new ArrayList<>();
    portfolioName.add("");
    portfolioName.add(typed);

    StockCommand addPortfolio = new AddPortfolio(portfolioName);
    addPortfolio.performCommand(model, view);
  }

  @Override
  public void buyStockOutput(List<String> list, LocalDate date) throws Exception {
    StockCommand buyStock = new BuyStock(list, date);
    try {
      buyStock.performCommand(model, view);
    } catch (Exception e) {
      view.writeMessage(e.getMessage());
    }
  }

  @Override
  public void sellStockOutput(List<String> list, LocalDate date) throws Exception {
    StockCommand sellStock = new SellStock(list, date);
    try {
      sellStock.performCommand(model, view);
    } catch (Exception e) {
      view.writeMessage(e.getMessage());
    }
  }

  @Override
  public void addLoadPortfolioOutput(String typed) throws Exception {
    List<String> list = new ArrayList<>();
    list.add("");
    list.add(typed);

    StockCommand loadPortfolio = new LoadPortfolio(list);
    loadPortfolio.performCommand(model, view);
  }

  @Override
  public void totalAssetValueOutput(String typed, LocalDate date) throws Exception {
    StockCommand av = new GetAssetValue(typed, date);
    try {
      av.performCommand(model, view);
    } catch (Exception e) {
      view.writeMessage(e.getMessage());
    }
  }

  @Override
  public void getCompositionOutput(String typed, LocalDate date) throws Exception {
    StockCommand composition = new GetComposition(typed, date);
    try {
      composition.performCommand(model, view);
    } catch (Exception e) {
      view.writeMessage(e.getMessage());
    }
  }
}
