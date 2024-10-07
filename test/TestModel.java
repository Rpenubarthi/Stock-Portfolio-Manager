import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;

import model.BasicStock;
import model.BasicStockModel;
import model.Stock;
import model.StockModel;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

/**
 * Tests for the BasicStockModel class.
 */
public class TestModel {

  StockModel model;
  LocalDate startDate;
  LocalDate endDate;
  Stock stock;

  @Before
  public void setUp() throws IOException {
    model = new BasicStockModel();
  }

  @Test
  public void testNetGain() throws IOException {
    stock = new BasicStock("GOOG");
    startDate = LocalDate.parse("2024-04-05");
    endDate = LocalDate.parse("2024-06-05");
    assertEquals(23.13, model.netGain(startDate, endDate, stock), 0.01);

    stock = new BasicStock("AAPL");
    startDate = LocalDate.parse("2024-03-05");
    endDate = LocalDate.parse("2024-04-01");
    assertEquals(-0.09, model.netGain(startDate, endDate, stock), 0.001);

    stock = new BasicStock("MSFT");
    startDate = LocalDate.parse("2024-06-05");
    endDate = LocalDate.parse("2024-06-01");
    assertThrows(IllegalArgumentException.class, () -> model.netGain(startDate, endDate, stock));

    stock = new BasicStock("INVALID_TICKER");
    startDate = LocalDate.parse("2024-04-01");
    endDate = LocalDate.parse("2024-05-05");
    assertThrows(IllegalArgumentException.class, () -> model.netGain(startDate, endDate, stock));

    stock = new BasicStock("AAPL");
    startDate = LocalDate.parse("-99999-07-05");
    endDate = LocalDate.parse("2024-01-01");
    assertThrows(IllegalArgumentException.class, () -> model.netGain(startDate, endDate, stock));
  }

  @Test
  public void testUpdatePortfolio() throws IOException {
    stock = new BasicStock("H", 50);
    startDate = LocalDate.parse("2024-02-17");
    endDate = LocalDate.parse("2024-06-05");
    assertEquals(model.totalAssetValue(startDate, 0), 0, 0.01);
    model.updatePortfolio(stock, 0);
    assertEquals(model.totalAssetValue(startDate, 0), 6641.5, 0.01);
    stock = new BasicStock("GOOG", 22);
    model.updatePortfolio(stock, 1);
    assertEquals(model.totalAssetValue(startDate, 0), 6641.5, 0.01);
    assertEquals(model.totalAssetValue(endDate, 1), 3895.54, 0.01);
    assertThrows(IllegalArgumentException.class, () -> model.updatePortfolio(stock, 37));
  }

  @Test
  public void testTotalAssetValue() throws IOException {
    stock = new BasicStock("A", 18);
    startDate = LocalDate.parse("2024-05-17");
    assertEquals(model.totalAssetValue(startDate, 0), 0, 0.01);
    model.updatePortfolio(stock, 0);
    model.updatePortfolio(stock, 0);
    stock = new BasicStock("AAPL", 10);
    model.updatePortfolio(stock, 0);
    assertEquals(model.totalAssetValue(startDate, 0), 7450.98, 0.01);
  }

  @Test
  public void testSetDataFile() throws IOException {
    model.setDataFile("Stocks/src/model/cachedStockValues.csv");
    stock = new BasicStock("NVDA");
    startDate = LocalDate.parse("2024-03-05");
    endDate = LocalDate.parse("2024-04-01");
    assertEquals(43.99, model.netGain(startDate, endDate, stock), 0.001);
  }

  @Test
  public void testXDayCrossovers() throws IOException {
    stock = new BasicStock("NVDA");
    startDate = LocalDate.parse("2024-03-05");
    endDate = LocalDate.parse("2024-04-01");
    ArrayList<LocalDate> dates = new ArrayList<>();
    dates.add(LocalDate.parse("2024-03-25"));
    dates.add(LocalDate.parse("2024-03-07"));
    dates.add(LocalDate.parse("2024-03-21"));
    dates.add(LocalDate.parse("2024-03-20"));
    dates.add(LocalDate.parse("2024-03-19"));
    assertEquals(model.xDayCrossover(5, startDate, endDate, stock), dates);
  }

  @Test
  public void testXDayMovingAverage() throws IOException {
    stock = new BasicStock("NVDA");
    startDate = LocalDate.parse("2024-03-05");
    endDate = LocalDate.parse("2024-04-01");
    assertEquals(model.xDayMovingAverage(stock, 10, startDate), 792.88, 0.01);
  }


}
