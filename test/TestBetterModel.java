import org.junit.Before;
import org.junit.Test;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import model.addedfeatures.BetterBasicStockModel;
import model.addedfeatures.BetterStockModel;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

/**
 * The tests for the BetterStockModel.
 */
public class TestBetterModel {

  BetterStockModel model;
  FileWriter portfolioInfo;

  @Before
  public void setUp() throws Exception {
    portfolioInfo = new FileWriter("src/model/PortfolioInfo.csv", false);
    portfolioInfo.write("");
    model = new BetterBasicStockModel();
    model.setDataFile("src/model/cachedStockValues.csv");
  }

  @Test
  public void testAddPortfolio() throws Exception {
    LocalDate date = LocalDate.parse("2024-05-01");
    model.addPortfolio("retirement");
    assertEquals(0, model.totalAssetValue("retirement", date), 0.0001);
    portfolioInfo = new FileWriter("src/model/PortfolioInfo.csv", false);
    portfolioInfo.write("");
  }

  @Test
  public void testAddDuplicatePortfolio() throws Exception {
    model.addPortfolio("retirement");
    assertThrows(IllegalArgumentException.class, () -> model.addPortfolio("retirement"));
    portfolioInfo = new FileWriter("src/model/PortfolioInfo.csv", false);
    portfolioInfo.write("");
  }

  @Test
  public void testAddMultiplePortfolios() throws Exception {
    LocalDate date = LocalDate.parse("2024-05-01");
    model.addPortfolio("retirement");
    model.addPortfolio("collegeSavings");
    model.buyStock("AAPL", 10, "collegeSavings", date);
    assertEquals(0, model.totalAssetValue("retirement", date), 0.0001);
    assertEquals(1693.0, model.totalAssetValue("collegeSavings", date), 0.0001);
    portfolioInfo = new FileWriter("src/model/PortfolioInfo.csv", false);
    portfolioInfo.write("");
  }

  @Test
  public void testBuyStock() throws IOException {
    LocalDate dateStockBought = LocalDate.parse("2024-05-10");
    model.addPortfolio("retirement");
    model.buyStock("AAPL", 10, "retirement", dateStockBought);
    assertEquals(1830.5, model.totalAssetValue("retirement", dateStockBought), 0.0001);
    portfolioInfo = new FileWriter("src/model/PortfolioInfo.csv", false);
    portfolioInfo.write("");
  }

  @Test
  public void testBuyStockPortfolioDoesNotExist() throws IOException {
    LocalDate dateStockBought = LocalDate.parse("2024-05-10");
    model.addPortfolio("collegeSavings");
    assertThrows(IllegalArgumentException.class, () -> model.buyStock("AAPL", 10,
            "retirement", dateStockBought));
    portfolioInfo = new FileWriter("src/model/PortfolioInfo.csv", false);
    portfolioInfo.write("");
  }

  @Test
  public void testBuyMoreStock() throws IOException {
    LocalDate dateStockBought = LocalDate.parse("2024-05-10");
    LocalDate dateMoreStockBought = LocalDate.parse("2024-05-14");
    model.addPortfolio("retirement");
    model.buyStock("AAPL", 10, "retirement", dateStockBought);
    model.buyStock("AAPL", 10, "retirement", dateMoreStockBought);
    assertEquals(1830.5, model.totalAssetValue("retirement", dateStockBought), 0.0001);
    assertEquals(3748.6, model.totalAssetValue("retirement", dateMoreStockBought), 0.0001);
    portfolioInfo = new FileWriter("src/model/PortfolioInfo.csv", false);
    portfolioInfo.write("");
  }

  @Test
  public void testSellStock() throws IOException {
    LocalDate dateAppleStockBought = LocalDate.parse("2024-05-10");
    LocalDate dateNvidiaStockBought = LocalDate.parse("2024-05-13");
    LocalDate dateNvidiaStockSold = LocalDate.parse("2024-06-01");

    model.addPortfolio("retirement");
    model.buyStock("AAPL", 10, "retirement", dateAppleStockBought);
    model.buyStock("NVDA", 5, "retirement", dateNvidiaStockBought);

    assertEquals(model.totalAssetValue("retirement", dateNvidiaStockBought),
            6382.75, 0.0001);
    model.sellStock("NVDA", 5, "retirement", dateNvidiaStockSold);
    assertEquals(1922.5, model.totalAssetValue("retirement",
            dateNvidiaStockSold), 0.0001);
    portfolioInfo = new FileWriter("src/model/PortfolioInfo.csv", false);
    portfolioInfo.write("");
  }

  @Test
  public void testSellSomeSharesOfStock() throws IOException {

    LocalDate dateAppleStockBought = LocalDate.parse("2024-05-10");
    LocalDate dateNvidiaStockBought = LocalDate.parse("2024-05-13");
    LocalDate dateNvidiaStockSold = LocalDate.parse("2024-06-01");

    model.addPortfolio("retirement");
    model.buyStock("AAPL", 10, "retirement", dateAppleStockBought);
    model.buyStock("NVDA", 5, "retirement", dateNvidiaStockBought);

    assertEquals(model.totalAssetValue("retirement", dateNvidiaStockBought),
            6382.75, 0.0001);

    model.sellStock("NVDA", 2, "retirement", dateNvidiaStockSold);
    assertEquals(5211.49, model.totalAssetValue("retirement",
            dateNvidiaStockSold), 0.0001);
    portfolioInfo = new FileWriter("src/model/PortfolioInfo.csv", false);
    portfolioInfo.write("");
  }

  @Test
  public void testSellMoreThanAvailableStock() throws IOException {

    LocalDate dateAppleStockBought = LocalDate.parse("2024-05-10");
    LocalDate dateNvidiaStockBought = LocalDate.parse("2024-05-13");
    LocalDate dateNvidiaStockSold = LocalDate.parse("2024-06-01");

    model.addPortfolio("retirement");
    model.buyStock("AAPL", 10, "retirement", dateAppleStockBought);
    model.buyStock("NVDA", 5, "retirement", dateNvidiaStockBought);

    assertEquals(6382.75, model.totalAssetValue("retirement",
            dateNvidiaStockBought), 0.0001);

    assertThrows(IllegalArgumentException.class, () -> model.sellStock("NVDA", 10,
            "retirement", dateNvidiaStockSold));
    portfolioInfo = new FileWriter("src/model/PortfolioInfo.csv", false);
    portfolioInfo.write("");
  }


  @Test
  public void testGetCompositionEmpty() throws IOException {
    LocalDate dateNvidiaStockBought = LocalDate.parse("2024-05-13");

    model.addPortfolio("retirement");

    Map<String, Double> actualComposition = new HashMap<>();

    assertEquals(actualComposition, model.getComposition("retirement", dateNvidiaStockBought));
  }

  @Test
  public void testGetComposition() throws IOException {
    LocalDate dateAppleStockBought = LocalDate.parse("2024-05-10");
    LocalDate dateNvidiaStockBought = LocalDate.parse("2024-05-13");

    model.addPortfolio("retirement");
    model.buyStock("AAPL", 10, "retirement", dateAppleStockBought);
    model.buyStock("NVDA", 5, "retirement", dateNvidiaStockBought);

    Map<String, Double> actualComposition = new HashMap<>();
    actualComposition.put("AAPL", 10.0);
    actualComposition.put("NVDA", 5.0);

    assertEquals(actualComposition, model.getComposition("retirement", dateNvidiaStockBought));
    portfolioInfo = new FileWriter("src/model/PortfolioInfo.csv", false);
    portfolioInfo.write("");
  }

  @Test
  public void testGetCompositionModified() throws IOException {
    LocalDate dateAppleStockBought = LocalDate.parse("2024-05-10");
    LocalDate dateNvidiaStockBought = LocalDate.parse("2024-05-13");

    model.addPortfolio("retirement");
    model.buyStock("AAPL", 10, "retirement", dateAppleStockBought);
    model.buyStock("NVDA", 5, "retirement", dateNvidiaStockBought);
    model.sellStock("AAPL", 5, "retirement", dateNvidiaStockBought);

    Map<String, Double> actualComposition = new HashMap<>();
    actualComposition.put("AAPL", 5.0);
    actualComposition.put("NVDA", 5.0);

    assertEquals(actualComposition, model.getComposition("retirement", dateNvidiaStockBought));
    portfolioInfo = new FileWriter("src/model/PortfolioInfo.csv", false);
    portfolioInfo.write("");
  }

  @Test
  public void testTotalAssetValue() throws IOException {
    LocalDate datePortfolioCreated = LocalDate.parse("2024-05-01");
    LocalDate dateStockBought = LocalDate.parse("2024-05-10");
    model.addPortfolio("retirement");

    try {
      assertEquals(0, model.totalAssetValue("retirement", datePortfolioCreated), 0.001);
    } catch (Exception e) {
      e.printStackTrace();
    }

    model.buyStock("AAPL", 10, "retirement", dateStockBought);
    assertEquals(1830.5, model.totalAssetValue("retirement", dateStockBought), 0.0001);

    model.buyStock("NFLX", 10, "retirement",
            LocalDate.parse("2024-05-15"));
    assertEquals(8032.4, model.totalAssetValue("retirement",
            LocalDate.parse("2024-05-15")), 0.0001);
    portfolioInfo = new FileWriter("src/model/PortfolioInfo.csv", false);
    portfolioInfo.write("");
  }

  @Test
  public void testBuy3Stocks() throws IOException {
    LocalDate dateStockBought = LocalDate.parse("2024-05-10");
    LocalDate dateMoreStockBought = LocalDate.parse("2024-05-14");
    model.addPortfolio("retirement");
    model.buyStock("AAPL", 10, "retirement", dateStockBought);
    model.buyStock("AAPL", 10, "retirement", dateMoreStockBought);
    assertEquals(1830.5, model.totalAssetValue("retirement", dateStockBought), 0.0001);
    assertEquals(3748.6, model.totalAssetValue("retirement", dateMoreStockBought), 0.0001);
    model.buyStock("NVDA", 5, "retirement", dateMoreStockBought);
    model.buyStock("AMZN", 6, "retirement", dateMoreStockBought);
    assertEquals(9438.82, model.totalAssetValue("retirement", dateMoreStockBought), 0.0001);
    portfolioInfo = new FileWriter("src/model/PortfolioInfo.csv", false);
    portfolioInfo.write("");
  }

  @Test
  public void testGetDistribution() throws IOException {
    LocalDate dateAppleStockBought = LocalDate.parse("2024-05-10");
    LocalDate dateNvidiaStockBought = LocalDate.parse("2024-05-13");

    model.addPortfolio("retirement");
    model.buyStock("AAPL", 10, "retirement", dateAppleStockBought);
    model.buyStock("NVDA", 5, "retirement", dateNvidiaStockBought);

    Map<String, Double> actualDistribution = new HashMap<>();
    actualDistribution.put("AAPL", 1862.8);
    actualDistribution.put("NVDA", 4519.95);

    assertEquals(actualDistribution, model.getDistribution("retirement",
            dateNvidiaStockBought));
    portfolioInfo = new FileWriter("src/model/PortfolioInfo.csv", false);
    portfolioInfo.write("");
  }

  @Test
  public void testGetDistributionModified() throws IOException {
    LocalDate dateAppleStockBought = LocalDate.parse("2024-05-10");
    LocalDate dateNvidiaStockBought = LocalDate.parse("2024-05-13");

    model.addPortfolio("retirement");
    model.buyStock("AAPL", 10, "retirement", dateAppleStockBought);
    model.buyStock("NVDA", 5, "retirement", dateNvidiaStockBought);
    model.sellStock("NVDA", 3, "retirement", dateNvidiaStockBought);

    Map<String, Double> actualDistribution = new HashMap<>();
    actualDistribution.put("AAPL", 1862.8);
    actualDistribution.put("NVDA", 1807.98);

    assertEquals(actualDistribution, model.getDistribution("retirement",
            dateNvidiaStockBought));
    portfolioInfo = new FileWriter("src/model/PortfolioInfo.csv", false);
    portfolioInfo.write("");
  }

  @Test
  public void testRebalanceEmptyPortfolio() throws IOException {
    LocalDate date = LocalDate.parse("2024-05-10");

    model.addPortfolio("retirement");
    Map<String, Double> weights = new HashMap<>();
    weights.put("AAPL", 50.0);
    weights.put("NVDA", 50.0);

    assertThrows(IllegalArgumentException.class, () -> model.rebalance("retirement",
            weights, date));
    portfolioInfo = new FileWriter("src/model/PortfolioInfo.csv", false);
    portfolioInfo.write("");
  }

  @Test
  public void testRebalancePortfolio() throws IOException {
    LocalDate date = LocalDate.parse("2024-05-10");
    LocalDate dateAppleStockBought = LocalDate.parse("2024-05-10");
    LocalDate dateNvidiaStockBought = LocalDate.parse("2024-05-13");

    model.addPortfolio("retirement");
    model.buyStock("AAPL", 10, "retirement", dateAppleStockBought);
    model.buyStock("NVDA", 5, "retirement", dateNvidiaStockBought);

    Map<String, Double> weights = new HashMap<>();
    weights.put("AAPL", 0.5);
    weights.put("NVDA", 0.5);

    Map<String, Double> actualDistribution = new HashMap<>();
    actualDistribution.put("AAPL", 915.25);
    actualDistribution.put("NVDA", 915.25);

    model.rebalance("retirement", weights, date);
    assertEquals(actualDistribution, model.getDistribution("retirement", date));
    portfolioInfo = new FileWriter("src/model/PortfolioInfo.csv", false);
    portfolioInfo.write("");
  }

  @Test
  public void testRebalanceWithOneStock() throws IOException {
    LocalDate date = LocalDate.parse("2024-05-10");
    LocalDate dateAppleStockBought = LocalDate.parse("2024-05-10");
    model.addPortfolio("retirement");
    model.buyStock("AAPL", 10, "retirement", dateAppleStockBought);

    Map<String, Double> weights = new HashMap<>();
    weights.put("AAPL", 0.5);
    weights.put("NVDA", 0.5);

    assertThrows(IllegalArgumentException.class, () -> model.rebalance("retirement",
            weights, date));
    portfolioInfo = new FileWriter("src/model/PortfolioInfo.csv", false);
    portfolioInfo.write("");
  }

  @Test
  public void testRebalanceLargerPortfolio() throws IOException {
    model.setDataFile("src/model/cachedStockValues.csv");
    LocalDate date = LocalDate.parse("2024-05-10");

    model.addPortfolio("retirement");
    model.buyStock("AAPL", 10, "retirement", date);
    model.buyStock("NVDA", 5, "retirement", date);
    model.buyStock("GOOG", 10, "retirement", date);
    model.buyStock("TSLA", 13, "retirement", date);

    Map<String, Double> weights = new HashMap<>();
    weights.put("AAPL", 0.25);
    weights.put("NVDA", 0.25);
    weights.put("GOOG", 0.25);
    weights.put("TSLA", 0.25);

    Map<String, Double> actualDistribution = new HashMap<>();
    actualDistribution.put("AAPL", 2554.35);
    actualDistribution.put("NVDA", 2554.35);
    actualDistribution.put("GOOG", 2554.35);
    actualDistribution.put("TSLA", 2554.35);

    model.rebalance("retirement", weights, date);
    assertEquals(actualDistribution, model.getDistribution("retirement", date));
    portfolioInfo = new FileWriter("src/model/PortfolioInfo.csv", false);
    portfolioInfo.write("");
  }

  @Test
  public void testRebalanceOnADifferentDate() throws IOException {
    LocalDate date = LocalDate.parse("2024-05-10");

    model.addPortfolio("retirement");
    model.buyStock("AAPL", 10, "retirement", date);
    model.buyStock("NVDA", 5, "retirement", date);
    model.buyStock("GOOG", 10, "retirement", date);
    model.buyStock("TSLA", 13, "retirement", date);

    Map<String, Double> weights = new HashMap<>();
    weights.put("AAPL", 0.25);
    weights.put("NVDA", 0.25);
    weights.put("GOOG", 0.25);
    weights.put("TSLA", 0.25);

    Map<String, Double> actualDistribution = new HashMap<>();
    actualDistribution.put("AAPL", 2682.73);
    actualDistribution.put("NVDA", 3115.79);
    actualDistribution.put("GOOG", 2609.4);
    actualDistribution.put("TSLA", 2700.06);

    LocalDate dateRebalance = LocalDate.parse("2024-05-31");

    model.rebalance("retirement", weights, date);
    assertEquals(actualDistribution, model.getDistribution("retirement",
            dateRebalance));
    portfolioInfo = new FileWriter("src/model/PortfolioInfo.csv", false);
    portfolioInfo.write("");
  }

  @Test
  public void testRebalanceOnADifferentDateUneven() throws IOException {
    LocalDate date = LocalDate.parse("2024-05-10");

    model.addPortfolio("retirement");
    model.buyStock("AAPL", 10, "retirement", date);
    model.buyStock("NVDA", 5, "retirement", date);
    model.buyStock("GOOG", 10, "retirement", date);
    model.buyStock("TSLA", 13, "retirement", date);

    Map<String, Double> weights = new HashMap<>();
    weights.put("AAPL", 0.60);
    weights.put("NVDA", 0.10);
    weights.put("GOOG", 0.15);
    weights.put("TSLA", 0.15);

    Map<String, Double> actualDistribution = new HashMap<>();
    actualDistribution.put("AAPL", 6438.56);
    actualDistribution.put("NVDA", 1246.32);
    actualDistribution.put("GOOG", 1565.64);
    actualDistribution.put("TSLA", 1620.04);

    LocalDate dateRebalance = LocalDate.parse("2024-05-31");

    model.rebalance("retirement", weights, date);
    assertEquals(actualDistribution, model.getDistribution("retirement",
            dateRebalance));
    portfolioInfo = new FileWriter("src/model/PortfolioInfo.csv", false);
    portfolioInfo.write("");
  }

  @Test
  public void testRebalanceStockDoesNotExist() throws IOException {
    LocalDate date = LocalDate.parse("2024-05-10");

    model.addPortfolio("retirement");
    model.buyStock("AAPL", 10, "retirement", date);
    model.buyStock("NVDA", 5, "retirement", date);
    model.buyStock("GOOG", 10, "retirement", date);
    model.buyStock("TSLA", 13, "retirement", date);

    Map<String, Double> weights = new HashMap<>();
    weights.put("AAPL", 0.25);
    weights.put("NVDA", 0.25);
    weights.put("GOOG", 0.25);
    weights.put("AMZN", 0.25);

    assertThrows(IllegalArgumentException.class, () -> model.rebalance("retirement",
            weights, date));
    portfolioInfo = new FileWriter("src/model/PortfolioInfo.csv", false);
    portfolioInfo.write("");
  }

  @Test
  public void testRebalanceStockExistsTwice() throws IOException {
    LocalDate date = LocalDate.parse("2024-05-10");

    model.addPortfolio("retirement");
    model.buyStock("AAPL", 10, "retirement", date);
    model.buyStock("NVDA", 5, "retirement", date);
    model.buyStock("GOOG", 10, "retirement", date);
    model.buyStock("TSLA", 13, "retirement", date);

    Map<String, Double> weights = new HashMap<>();
    weights.put("AAPL", 0.25);
    weights.put("NVDA", 0.25);
    weights.put("AAPL", 0.25);
    weights.put("AMZN", 0.25);

    assertThrows(IllegalArgumentException.class, () -> model.rebalance("retirement",
            weights, date));
    portfolioInfo = new FileWriter("src/model/PortfolioInfo.csv", false);
    portfolioInfo.write("");
  }

  @Test
  public void testRebalanceWeightsDoNotAddTo1() throws IOException {
    LocalDate date = LocalDate.parse("2024-05-10");

    model.addPortfolio("retirement");
    model.buyStock("AAPL", 10, "retirement", date);
    model.buyStock("NVDA", 5, "retirement", date);
    model.buyStock("GOOG", 10, "retirement", date);
    model.buyStock("TSLA", 13, "retirement", date);

    Map<String, Double> weights = new HashMap<>();
    weights.put("AAPL", 0.10);
    weights.put("NVDA", 0.25);
    weights.put("AAPL", 0.25);
    weights.put("AMZN", 0.25);

    assertThrows(IllegalArgumentException.class, () -> model.rebalance("retirement",
            weights, date));
    portfolioInfo = new FileWriter("src/model/PortfolioInfo.csv", false);
    portfolioInfo.write("");
  }

  @Test
  public void testPortfolioOverTime() throws IOException {
    LocalDate date = LocalDate.parse("2024-05-10");
    StringBuilder actual = new StringBuilder();
    model.addPortfolio("retirement");
    model.buyStock("AAPL", 10, "retirement", date);
    model.buyStock("NVDA", 5, "retirement", date);
    model.buyStock("GOOG", 10, "retirement", date);
    model.buyStock("TSLA", 13, "retirement", date);
    assertEquals(10217.41, model.totalAssetValue("retirement", date), 0.01);

    model.buyStock("AAPL", 15, "retirement", date.plusDays(20));
    model.buyStock("NVDA", 5, "retirement", date.plusDays(25));
    assertEquals(20525.76, model.totalAssetValue("retirement"
            , date.plusDays(25)), 0.01);

    assertEquals("Performance of portfolio: retirement from 2024-05-10 to 2024-06-04\n"
                    + "\n"
                    + "MAY 10, 2024: **********\n"
                    + "MAY 12, 2024: **********\n"
                    + "MAY 14, 2024: **********\n"
                    + "MAY 16, 2024: **********\n"
                    + "MAY 18, 2024: **********\n"
                    + "MAY 20, 2024: **********\n"
                    + "MAY 22, 2024: **********\n"
                    + "MAY 24, 2024: ***********\n"
                    + "MAY 26, 2024: ***********\n"
                    + "MAY 28, 2024: ***********\n"
                    + "MAY 30, 2024: **************\n"
                    + "JUN 1, 2024: **************\n"
                    + "JUN 3, 2024: **************\n"
                    + "JUN 4, 2024: *********************" + "\n"
                    + "\nScale: * = 1000"
            , model.performanceOverTime(false, "retirement",
                    date, date.plusDays(25)));
    portfolioInfo = new FileWriter("src/model/PortfolioInfo.csv", false);
    portfolioInfo.write("");
  }

  @Test
  public void testPortfolioOverTimeInvalidDate() throws IOException {
    LocalDate date = LocalDate.parse("2024-05-10");
    model.addPortfolio("retirement");
    model.buyStock("AAPL", 10, "retirement", date);
    model.buyStock("NVDA", 5, "retirement", date);
    model.buyStock("GOOG", 10, "retirement", date);
    model.buyStock("TSLA", 13, "retirement", date);
    assertEquals(10217.41, model.totalAssetValue("retirement", date),
            0.01);
    assertThrows(IllegalArgumentException.class, () -> model.performanceOverTime(false,
            "AAPL", date.plusDays(30), date));
    portfolioInfo = new FileWriter("src/model/PortfolioInfo.csv", false);
    portfolioInfo.write("");
  }

  @Test
  public void testPortfolioOverTimeStock() throws IOException {
    LocalDate date = LocalDate.parse("2024-05-10");
    model.addPortfolio("retirement");
    model.buyStock("AAPL", 10, "retirement", date);
    model.buyStock("NVDA", 5, "retirement", date);
    model.buyStock("GOOG", 10, "retirement", date);
    model.buyStock("TSLA", 13, "retirement", date);
    assertEquals(10217.41, model.totalAssetValue("retirement",
            date), 0.01);
    assertThrows(IllegalArgumentException.class, () -> model.performanceOverTime(true,
            "AAPL", date.plusDays(30), date));
    portfolioInfo = new FileWriter("src/model/PortfolioInfo.csv", false);
    portfolioInfo.write("");
  }
}
