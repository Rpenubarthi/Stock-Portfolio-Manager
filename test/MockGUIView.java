import controller.Features;
import view.GraphicalUserView;

/**
 * A mock view to test the FeaturesController.
 * Stores the expected output in a string.
 */
public class MockGUIView implements GraphicalUserView {
  protected String output;

  @Override
  public void addFeatures(Features features) {
      // Placeholder method that would be there in real implementations of the GraphicalUserView.
  }

  @Override
  public void writeMessage(String message) {
    output = message;
  }

  @Override
  public String readInput() {
    return output;
  }
}
