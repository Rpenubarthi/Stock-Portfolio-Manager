import org.junit.Before;
import org.junit.Test;

import java.io.FileWriter;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

import controller.Features;
import controller.FeaturesController;
import model.addedfeatures.BetterBasicStockModel;
import model.addedfeatures.BetterStockModel;
import view.GraphicalUserView;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

/**
 * Tests the features that the GUI has.
 */
public class TestFeatures {
  BetterStockModel model;
  GraphicalUserView view;
  FileWriter portfolioInfo;

  @Before
  public void setUp() throws Exception {
    model = new BetterBasicStockModel();
    view = new MockGUIView();
    portfolioInfo = new FileWriter("src/model/PortfolioInfo.csv", false);
    portfolioInfo.write("");
    portfolioInfo.close();
  }

  @Test
  public void testAddPortfolioOutput() throws Exception {
    Features controller = new FeaturesController(model);
    controller.setView(view);
    controller.addPortfolioOutput("Hello");
    assertEquals("Added Portfolio: Hello", view.readInput());
  }

  @Test
  public void testAddPortfolioTwice() throws Exception {
    Features controller = new FeaturesController(model);
    controller.setView(view);
    controller.addPortfolioOutput("Hello");
    assertEquals("Added Portfolio: Hello", view.readInput());

    controller.addPortfolioOutput("Hello");
    assertEquals("A portfolio with that name already exists.", view.readInput());
  }

  @Test
  public void testBuyStockOutput() throws Exception {
    Features controller = new FeaturesController(model);
    controller.setView(view);
    controller.addPortfolioOutput("Hello");
    List<String> inputs = new ArrayList<>();
    inputs.add("AAPL");
    inputs.add("10");
    inputs.add("Hello");
    controller.buyStockOutput(inputs, LocalDate.parse("2024-06-01"));
    assertEquals("Bought: 10 shares of AAPL", view.readInput());
  }

  @Test
  public void testBuyStockOutputInvalidShares() throws Exception {
    Features controller = new FeaturesController(model);
    controller.setView(view);
    controller.addPortfolioOutput("Hello");
    List<String> inputs = new ArrayList<>();
    inputs.add("AAPL");
    inputs.add("-10");
    inputs.add("Hello");
    controller.buyStockOutput(inputs, LocalDate.parse("2024-06-01"));
    assertEquals("Shares cannot be negative", view.readInput());
  }

  @Test
  public void testBuyStockOutputFutureDate() throws Exception {
    Features controller = new FeaturesController(model);
    controller.setView(view);
    controller.addPortfolioOutput("Hello");
    List<String> inputs = new ArrayList<>();
    inputs.add("AAPL");
    inputs.add("10");
    inputs.add("Hello");
    controller.buyStockOutput(inputs, LocalDate.parse("2024-06-30"));
    assertEquals("Bought: 10 shares of AAPL", view.readInput());
  }

  @Test
  public void testBuyStockOutputInvalidPortfolioName() throws Exception {
    Features controller = new FeaturesController(model);
    controller.setView(view);
    List<String> inputs = new ArrayList<>();
    inputs.add("AAPL");
    inputs.add("10");
    inputs.add("Lol");
    controller.buyStockOutput(inputs, LocalDate.parse("2024-06-01"));
    assertEquals("No portfolio with that name exists.", view.readInput());
  }

  @Test
  public void testBuyStockOutputInvalidTicker() throws Exception {
    Features controller = new FeaturesController(model);
    controller.setView(view);
    List<String> inputs = new ArrayList<>();
    controller.addPortfolioOutput("Hello");
    inputs.add("NVVA");
    inputs.add("10");
    inputs.add("Hello");
    controller.buyStockOutput(inputs, LocalDate.parse("2024-06-01"));
    assertEquals("Invalid stock ticker: NVVA", view.readInput());
  }

  @Test
  public void testBuyStockOutputInvalidDate() throws Exception {
    Features controller = new FeaturesController(model);
    controller.setView(view);
    List<String> inputs = new ArrayList<>();
    controller.addPortfolioOutput("Hello");
    inputs.add("NVVA");
    inputs.add("10");
    inputs.add("Hello");
    assertThrows(DateTimeParseException.class, () ->
            controller.buyStockOutput(inputs, LocalDate.parse("2024-06-31")));
  }

  @Test
  public void testSellStockOutput() throws Exception {
    Features controller = new FeaturesController(model);
    controller.setView(view);
    List<String> inputs = new ArrayList<>();
    controller.addPortfolioOutput("Hello1");
    inputs.add("NVDA");
    inputs.add("10");
    inputs.add("Hello1");
    controller.buyStockOutput(inputs, LocalDate.parse("2024-06-01"));

    List<String> inputs1 = new ArrayList<>();
    inputs1.add("NVDA");
    inputs1.add("5");
    inputs1.add("Hello1");
    controller.sellStockOutput(inputs1, LocalDate.parse("2024-06-05"));
    assertEquals("Sold: 5 shares of NVDA", view.readInput());
  }

  @Test
  public void testSellStockOutputMoreStockThanOwned() throws Exception {
    Features controller = new FeaturesController(model);
    controller.setView(view);
    List<String> inputs = new ArrayList<>();
    controller.addPortfolioOutput("Hello1");
    inputs.add("NVDA");
    inputs.add("10");
    inputs.add("Hello1");
    controller.buyStockOutput(inputs, LocalDate.parse("2024-06-01"));

    List<String> inputs1 = new ArrayList<>();
    inputs1.add("NVDA");
    inputs1.add("5");
    inputs1.add("Hello1");
    controller.sellStockOutput(inputs1, LocalDate.parse("2024-05-31"));
    assertEquals("Cannot sell more stocks than owned.", view.readInput());
  }

  @Test
  public void testSellStockInvalidDate() throws Exception {
    Features controller = new FeaturesController(model);
    controller.setView(view);
    List<String> inputs = new ArrayList<>();
    controller.addPortfolioOutput("Hello1");
    inputs.add("NVDA");
    inputs.add("10");
    inputs.add("Hello1");
    controller.buyStockOutput(inputs, LocalDate.parse("2024-06-01"));

    List<String> inputs1 = new ArrayList<>();
    inputs1.add("NVDA");
    inputs1.add("10");
    inputs1.add("Hello1");
    assertThrows(DateTimeParseException.class, () ->
            controller.sellStockOutput(inputs1, LocalDate.parse("2024-06-31")));
  }

  @Test
  public void testSellStockInvalidPortfolio() throws Exception {
    Features controller = new FeaturesController(model);
    controller.setView(view);
    List<String> inputs = new ArrayList<>();
    controller.addPortfolioOutput("Hello1");
    inputs.add("NVDA");
    inputs.add("10");
    inputs.add("Hello1");
    controller.buyStockOutput(inputs, LocalDate.parse("2024-06-01"));

    List<String> inputs1 = new ArrayList<>();
    inputs1.add("NVDA");
    inputs1.add("5");
    inputs1.add("Hello3");
    controller.sellStockOutput(inputs1, LocalDate.parse("2024-06-02"));
    assertEquals("No portfolio with that name exists.", view.readInput());
  }

  @Test
  public void testSellStockInvalidTicker() throws Exception {
    Features controller = new FeaturesController(model);
    controller.setView(view);
    List<String> inputs = new ArrayList<>();
    controller.addPortfolioOutput("Hello1");
    inputs.add("NVDA");
    inputs.add("10");
    inputs.add("Hello1");
    controller.buyStockOutput(inputs, LocalDate.parse("2024-06-01"));

    List<String> inputs1 = new ArrayList<>();
    inputs1.add("NVVA");
    inputs1.add("5");
    inputs1.add("Hello1");
    controller.sellStockOutput(inputs1, LocalDate.parse("2024-06-01"));
    assertEquals("Cannot sell more stocks than owned.", view.readInput());
  }

  @Test
  public void testTotalAssetValueEmpty() throws Exception {
    Features controller = new FeaturesController(model);
    controller.setView(view);
    controller.addPortfolioOutput("Portfolio1");

    controller.totalAssetValueOutput("Portfolio1", LocalDate.parse("2024-06-01"));
    assertEquals("Asset value is $0.0 as of 2024-06-01 for Portfolio1", view.readInput());
  }

  @Test
  public void testTotalAssetValue() throws Exception {
    Features controller = new FeaturesController(model);
    controller.setView(view);
    controller.addPortfolioOutput("Portfolio2");
    List<String> inputs = new ArrayList<>();
    controller.addPortfolioOutput("Portfolio2");
    inputs.add("NVDA");
    inputs.add("10");
    inputs.add("Portfolio2");
    controller.buyStockOutput(inputs, LocalDate.parse("2024-05-20"));

    List<String> inputs1 = new ArrayList<>();
    controller.addPortfolioOutput("Portfolio2");
    inputs1.add("NFLX");
    inputs1.add("5");
    inputs1.add("Portfolio2");
    controller.buyStockOutput(inputs1, LocalDate.parse("2024-06-02"));

    controller.totalAssetValueOutput("Portfolio2", LocalDate.parse("2024-05-31"));
    assertEquals("Asset value is $10963.3 as of 2024-05-31 for Portfolio2",
            view.readInput());

    controller.totalAssetValueOutput("Portfolio2", LocalDate.parse("2024-06-03"));
    assertEquals("Asset value is $14668.95 as of 2024-06-03 for Portfolio2",
            view.readInput());
  }

  @Test
  public void testGetComposition() throws Exception {
    Features controller = new FeaturesController(model);
    controller.setView(view);
    controller.addPortfolioOutput("Portfolio3");
    List<String> inputs = new ArrayList<>();
    controller.addPortfolioOutput("Portfolio3");
    inputs.add("NVDA");
    inputs.add("10");
    inputs.add("Portfolio3");
    controller.buyStockOutput(inputs, LocalDate.parse("2024-05-20"));

    List<String> inputs1 = new ArrayList<>();
    controller.addPortfolioOutput("Portfolio3");
    inputs1.add("NFLX");
    inputs1.add("5");
    inputs1.add("Portfolio3");
    controller.buyStockOutput(inputs1, LocalDate.parse("2024-06-02"));

    controller.getCompositionOutput("Portfolio3", LocalDate.parse("2024-06-03"));
    assertEquals("Composition of Portfolio3 is {NFLX=5.0, NVDA=10.0}", view.readInput());
  }

  @Test
  public void testLoadPortfolio() throws Exception {
    Features controller = new FeaturesController(model);
    controller.setView(view);
    controller.addPortfolioOutput("Hello");
    List<String> inputs = new ArrayList<>();
    inputs.add("AAPL");
    inputs.add("10");
    inputs.add("Hello");
    controller.buyStockOutput(inputs, LocalDate.parse("2024-06-01"));
    controller.addLoadPortfolioOutput("Hello");
    assertEquals("Retrieved Portfolio: Hello", view.readInput());
  }

  @Test
  public void testLoadPortfolioNeverExisted() throws Exception {
    Features controller = new FeaturesController(model);
    controller.setView(view);
    controller.addLoadPortfolioOutput("djekwndmw");
    assertEquals("No portfolio to load", view.readInput());
  }

}
