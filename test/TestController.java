
import org.junit.Before;
import org.junit.Test;

import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;

import controller.Controller;
import controller.StockController;
import model.addedfeatures.BetterBasicStockModel;
import model.addedfeatures.BetterStockModel;

import static org.junit.Assert.assertEquals;

/**
 * This is testing for the Controller class.
 */
public class TestController {
  String menu;
  String menuBye;
  String menuMiddle;
  FileWriter portfolioInfo;

  @Before
  public void setUp() throws IOException {
    portfolioInfo = new FileWriter("src/model/PortfolioInfo.csv", false);
    portfolioInfo.write("");
    portfolioInfo.close();
    menu = "Welcome to Stock Manager!\n"
            + "Supported user instructions are:\n"
            + "Separate each entry value by a new line\n"
            + "Enter x as an integer, and enter date by inputting the year, month, date on " +
            "separate lines in that order.\n"
            + "add-portfolio ticker\n"
            + "buy-stock ticker shares portfolio-name date\n"
            + "sell-stock ticker shares portfolio-name date\n"
            + "net-gain ticker start-date end-date\n"
            + "x-day-moving-average ticker x start-date\n"
            + "x-day-crossover ticker x start-date end-date\n"
            + "get-asset-value portfolio-name date\n"
            + "get-composition portfolio date\n"
            + "get-distribution portfolio date\n"
            + "get-composition portfolio date\n"
            + "rebalance portfolio-name num-of-weights weights " +
            "(ticker, new line, weight as a decimal) date\n"
            + "get-performance-over-time portfolio-name start-date end-date\n"
            + "load-portfolio portfolio-name\n"
            + "menu (Print supported instruction list)\n"
            + "q or quit (quit the program)";
    menuBye = "Supported user instructions are:\n"
            + "Separate each entry value by a new line\n"
            + "Enter x as an integer, and enter date by inputting the year, month, date on " +
            "separate lines in that order.\n"
            + "add-portfolio ticker\n"
            + "buy-stock ticker shares portfolio-name date\n"
            + "sell-stock ticker shares portfolio-name date\n"
            + "net-gain ticker start-date end-date\n"
            + "x-day-moving-average ticker x start-date\n"
            + "x-day-crossover ticker x start-date end-date\n"
            + "get-asset-value portfolio-name date\n"
            + "get-composition portfolio date\n"
            + "get-distribution portfolio date\n"
            + "get-composition portfolio date\n"
            + "rebalance portfolio-name num-of-weights weights " +
            "(ticker, new line, weight as a decimal) date\n"
            + "get-performance-over-time portfolio-name start-date end-date\n"
            + "load-portfolio portfolio-name\n"
            + "menu (Print supported instruction list)\n"
            + "q or quit (quit the program)\n"
            + "Thank you for using stock program!\n";
    menuMiddle = "Supported user instructions are:\n"
            + "Separate each entry value by a new line\n"
            + "Enter x as an integer, and enter date by inputting the year, month, date " +
            "on separate lines in that order.\n"
            + "add-portfolio ticker\n"
            + "buy-stock ticker shares portfolio-name date\n"
            + "sell-stock ticker shares portfolio-name date\n"
            + "net-gain ticker start-date end-date\n"
            + "x-day-moving-average ticker x start-date\n"
            + "x-day-crossover ticker x start-date end-date\n"
            + "get-asset-value portfolio-name date\n"
            + "get-composition portfolio date\n"
            + "get-distribution portfolio date\n"
            + "get-composition portfolio date\n"
            + "rebalance portfolio-name num-of-weights weights " +
            "(ticker, new line, weight as a decimal) date\n"
            + "get-performance-over-time portfolio-name start-date end-date\n"
            + "load-portfolio portfolio-name\n"
            + "menu (Print supported instruction list)\n"
            + "q or quit (quit the program)\n";
  }

  @Test
  public void testAddPortfolio() throws IOException {
    StringBuilder out = new StringBuilder();
    StringReader input = new StringReader("add-portfolio\nsavings\nq");
    BetterStockModel model = new BetterBasicStockModel();
    Controller controller = new StockController(out, input, model);

    controller.startProgram();
    assertEquals(out.toString(), menu + "\n\n\nAdded Portfolio: savings\n"
            + menuBye);
  }

  @Test
  public void testBuyStock() throws IOException {
    StringBuilder out = new StringBuilder();
    StringReader input = new StringReader("add-portfolio\nsavings\nbuy-stock\nAAPL\n" +
            "10\nsavings\n2024\n05\n13\nq");
    BetterStockModel model = new BetterBasicStockModel();
    Controller controller = new StockController(out, input, model);
    controller.startProgram();
    assertEquals(out.toString(), menu + "\n\n\nAdded Portfolio: savings\n"
            + menuMiddle + "Bought: 10 shares of AAPL\n" + menuBye);
  }

  @Test
  public void testGetAssetValue() throws IOException {
    StringBuilder out = new StringBuilder();
    StringReader input = new StringReader("add-portfolio\nsavings\nbuy-stock\n" +
            "AAPL\n10\nsavings\n2024\n05\n13\nget-asset-value\nsavings\n2024\n05\n13\nq");
    BetterStockModel model = new BetterBasicStockModel();
    Controller controller = new StockController(out, input, model);
    controller.startProgram();
    assertEquals(out.toString(), menu + "\n\n\nAdded Portfolio: savings\n"
            + menuMiddle + "Bought: 10 shares of AAPL\n" + menuMiddle + "Asset value is " +
            "$1862.8 as of 2024-05-13 for savings\n" + menuBye);
  }

  @Test
  public void testSellStock() throws IOException {
    StringBuilder out = new StringBuilder();
    StringReader input = new StringReader("add-portfolio\nsavings\nbuy-stock\nAAPL\n" +
            "10\nsavings\n2024\n05\n13\nsell-stock\nAAPL\n10\nsavings\n2024\n05\n16\nq");
    BetterStockModel model = new BetterBasicStockModel();
    Controller controller = new StockController(out, input, model);
    controller.startProgram();
    assertEquals(out.toString(), menu + "\n\n\nAdded Portfolio: savings\n"
            + menuMiddle + "Bought: 10 shares of AAPL\n" + menuMiddle + "Sold: 10 " +
            "shares of AAPL\n" + menuBye);
  }

  @Test
  public void testNetGain() throws IOException {
    StringBuilder out = new StringBuilder();
    StringReader input = new StringReader("net-gain\nNVDA\n2024\n04\n" +
            "10\n2024\n05\n10\nq");
    BetterStockModel model = new BetterBasicStockModel();
    Controller controller = new StockController(out, input, model);
    controller.startProgram();
    assertEquals(out.toString(), menu + "\nNet gain for NVDA from 2024-04-10 to " +
            "2024-05-10 is: $28.39\n" + menuBye);
  }

  @Test
  public void XDayMovingAverage() throws IOException {
    StringBuilder out = new StringBuilder();
    StringReader input = new StringReader("x-day-moving-average\nNVDA\n3\n2024\n" +
            "05\n13\nq");
    BetterStockModel model = new BetterBasicStockModel();
    Controller controller = new StockController(out, input, model);
    controller.startProgram();
    assertEquals(out.toString(), menu + "\n3-day moving average for NVDA on " +
            "2024-05-13 is: $896.75\n" + menuBye);
  }

  @Test
  public void XDayCrossover() throws IOException {
    StringBuilder out = new StringBuilder();
    StringReader input = new StringReader("x-day-crossover\nNVDA\n3\n2024\n" +
            "05\n13\n2024\n05\n21\nq");
    BetterStockModel model = new BetterBasicStockModel();
    Controller controller = new StockController(out, input, model);
    controller.startProgram();
    assertEquals(out.toString(), menu + "\n3-day crossover dates for NVDA between " +
            "2024-05-13 and 2024-05-21 are: [2024-05-21, 2024-05-20]\n" + menuBye);
  }

  @Test
  public void testGetComposition() throws IOException {
    StringBuilder out = new StringBuilder();
    StringReader input = new StringReader("add-portfolio\nsavings\nbuy-stock\nAAPL\n" +
            "10\nsavings\n2024\n05\n13\nget-composition\nsavings\n2024\n05\n13\nq");
    BetterStockModel model = new BetterBasicStockModel();
    Controller controller = new StockController(out, input, model);
    controller.startProgram();
    assertEquals(out.toString(), menu + "\n\n\nAdded Portfolio: savings\n"
            + menuMiddle + "Bought: 10 shares of AAPL\n" + menuMiddle + "Composition of savings " +
            "is {AAPL=10.0}\n" + menuBye);
  }

  @Test
  public void testGetDistribution() throws IOException {
    StringBuilder out = new StringBuilder();
    StringReader input = new StringReader("add-portfolio\nsavings\nbuy-stock\nAAPL\n" +
            "10\nsavings\n2024\n05\n13\nget-distribution\nsavings\n2024\n05\n13\nq");
    BetterStockModel model = new BetterBasicStockModel();
    Controller controller = new StockController(out, input, model);
    controller.startProgram();
    assertEquals(out.toString(), menu + "\n\n\nAdded Portfolio: savings\n"
            + menuMiddle + "Bought: 10 shares of AAPL\n" + menuMiddle + "Distribution of " +
            "savings is {AAPL=1862.8}\n" + menuBye);
  }

  @Test
  public void testRebalance() throws IOException {
    StringBuilder out = new StringBuilder();
    StringReader input = new StringReader("add-portfolio\nsavings\nbuy-stock\nAAPL\n" +
            "10\nsavings\n2024\n05\n13\nbuy-stock\nNVDA\n5\nsavings\n2024\n05\n13\nrebalance\n" +
            "savings\n2\nAAPL\n0.5\nNVDA\n0.5\n2024\n05\n13\nq");
    BetterStockModel model = new BetterBasicStockModel();
    Controller controller = new StockController(out, input, model);
    controller.startProgram();
    assertEquals(out.toString(), menu + "\n\n\nAdded Portfolio: savings\n"
            + menuMiddle + "Bought: 10 shares of AAPL\n" + menuMiddle
            + "Bought: 5 shares of NVDA\n" + menuMiddle + "Rebalance success!\n" + menuBye);
  }

}
