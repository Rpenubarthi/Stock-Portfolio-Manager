package view;

/**
 * This interface represents the StockView.
 */
public interface StockView {

  /**
   * This method displays the message based on what the view receives.
   *
   * @param message the output of the command or an error.
   */
  public void writeMessage(String message);

  /**
   * This method takes input from the scanner and reads it.
   *
   * @return the input from the scanner
   */
  public String readInput();
}
