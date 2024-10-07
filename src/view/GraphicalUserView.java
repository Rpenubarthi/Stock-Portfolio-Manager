package view;

import controller.Features;

/**
 * This view interface is for a visual representation of the view using a GUI.
 */
public interface GraphicalUserView extends StockView {

  /**
   * This method handles the action listeners of the various buttons within the GUI.
   *
   * @param features An object of the Features interface
   */
  void addFeatures(Features features);
}
