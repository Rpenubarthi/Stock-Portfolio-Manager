package view;

import java.io.IOException;
import java.util.Scanner;

/**
 * This class is an implementation of StockView.
 */
public class StockViewImpl implements StockView {
  private final Scanner sc;
  private final Appendable output;

  /**
   * The StockViewIMpl constructor.
   *
   * @param input  the user input as a readable.
   * @param output the output as an appendable.
   */
  public StockViewImpl(Readable input, Appendable output) {
    this.sc = new Scanner(input);
    this.output = output;
  }

  @Override
  public void writeMessage(String message) {
    try {
      output.append(message).append(System.lineSeparator());
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Override
  public String readInput() {
    return sc.nextLine();
  }
}