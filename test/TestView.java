
import org.junit.Test;

import java.io.StringReader;

import view.StockViewImpl;

import static org.junit.Assert.assertEquals;

/**
 * Tests for the view.
 */
public class TestView {

  @Test
  public void testWriteMessage() {
    StringBuilder output = new StringBuilder();
    StockViewImpl stockView = new StockViewImpl(new StringReader(""), output);

    String message = "Hello, World!";
    stockView.writeMessage(message);

    assertEquals("Hello, World!\n", output.toString());
  }

  @Test
  public void testReadInput() {
    String input = "Sample Input";
    StringReader inputReader = new StringReader(input);
    StringBuilder output = new StringBuilder();
    StockViewImpl stockView = new StockViewImpl(inputReader, output);

    assertEquals("Sample Input", stockView.readInput());
  }

  @Test
  public void testWriteAndRead() {
    String input = "First Input\nSecond Input";
    StringReader inputReader = new StringReader(input);
    StringBuilder output = new StringBuilder();
    StockViewImpl stockView = new StockViewImpl(inputReader, output);

    assertEquals("First Input", stockView.readInput());

    String message = "Message after first input";
    stockView.writeMessage(message);
    assertEquals("Message after first input\n", output.toString());

    assertEquals("Second Input", stockView.readInput());
  }
}

