package view;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JSpinner;
import javax.swing.SpinnerDateModel;
import javax.swing.JOptionPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import controller.Features;

/**
 * This is an implementation of the GraphicalUserView.
 * It supports displaying features like add portfolio, buy stock, sell stock,
 * get total asset value, get composition of a portfolio and load a portfolio.
 */
public class JFrameView extends JFrame implements GraphicalUserView {

  // for addPortfolio
  JPanel portfolioPanel;
  private JTextField portfolioNameText;
  private JButton addPortfolioButton;

  // for buy stock
  JPanel buyOrSellPanel;
  private JTextField tickerText;
  private JTextField sharesText;
  private JTextField portfolioText;
  JButton buyStockButton;

  // for a date in general
  private LocalDate localDate;

  private LocalDate localDateAC;

  // for sell stock
  JButton sellStockButton;

  // for load portfolio
  JPanel loadPortfolioPanel;
  JButton loadPortfolioButton;
  JTextField loadPortfolioText;

  // for total Asset Value
  JPanel assetOrCompositionPanel;
  private JTextField acPortfolio;
  JButton totalAssetValueButton;

  // for get Composition
  JButton getCompositionButton;

  /**
   * Constructor that sets all the JMenu Items, main JButtons, and the overall JLabel display.
   */
  public JFrameView() {
    JLabel display = new JLabel();
    setSize(700, 700);
    setLocation(200, 200);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    JMenuBar menuBar = new JMenuBar();

    // Create the main menu
    JMenu menu = new JMenu("Options");
    menuBar.add(menu);
    JMenuItem addPortfolio = new JMenuItem("Add Portfolio");
    JMenuItem buyStock = new JMenuItem("Buy Stock");
    JMenuItem sellStock = new JMenuItem("Sell Stock");
    JMenuItem totalAssetValue = new JMenuItem("Get Total Asset Value");
    JMenuItem getComposition = new JMenuItem("Get Composition");
    JMenuItem loadPortfolio = new JMenuItem("Load a Portfolio");

    // Add action listeners to the menu items
    addPortfolio.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        addPortfolioFeatures();
      }
    });

    buyStock.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        buyStock();
      }
    });

    sellStock.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        sellStock();
      }
    });

    totalAssetValue.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        totalAssetValue();
      }
    });

    getComposition.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        getComposition();
      }
    });

    loadPortfolio.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        loadPortfolioFeatures();
      }
    });

    addPortfolioButton = new JButton("Add Portfolio");
    buyStockButton = new JButton("Buy Stock");
    sellStockButton = new JButton("Sell Stock");
    totalAssetValueButton = new JButton("Get Total Asset Value");
    getCompositionButton = new JButton("Get Composition");
    loadPortfolioButton = new JButton("Load a Portfolio");

    menu.add(addPortfolio);
    menu.add(buyStock);
    menu.add(sellStock);
    menu.add(totalAssetValue);
    menu.add(getComposition);
    menu.add(loadPortfolio);
    this.add(display);

    setJMenuBar(menuBar);

    this.setLayout(new FlowLayout());
    setVisible(true);
  }

  private void addPortfolioFeatures() {
    removePanel();
    portfolioPanel = new JPanel();
    portfolioPanel.setLayout(new FlowLayout());

    portfolioNameText = new JTextField(10);

    portfolioPanel.add(new JLabel("Enter Portfolio Name:"));
    portfolioPanel.add(portfolioNameText);
    portfolioPanel.add(addPortfolioButton);

    this.add(portfolioPanel);
    this.revalidate();
    this.repaint();
  }

  private void loadPortfolioFeatures() {
    removePanel();
    loadPortfolioPanel = new JPanel();
    loadPortfolioPanel.setLayout(new FlowLayout());

    loadPortfolioText = new JTextField(10);

    loadPortfolioPanel.add(new JLabel("Enter Portfolio Name:"));
    loadPortfolioPanel.add(loadPortfolioText);
    loadPortfolioPanel.add(loadPortfolioButton);

    this.add(loadPortfolioPanel);
    this.revalidate();
    this.repaint();
  }

  private GridBagConstraints buyOrSellFeatures() {
    removePanel();
    buyOrSellPanel = new JPanel();
    buyOrSellPanel.setLayout(new GridBagLayout());
    GridBagConstraints gbc = new GridBagConstraints();

    gbc.insets = new Insets(5, 5, 5, 5);

    // First row: Ticker label and text field
    gbc.gridx = 0;
    gbc.gridy = 0;
    gbc.anchor = GridBagConstraints.EAST;
    buyOrSellPanel.add(new JLabel("Enter Ticker:"), gbc);

    gbc.gridx = 1;
    gbc.gridy = 0;
    gbc.anchor = GridBagConstraints.WEST;
    tickerText = new JTextField(10);
    buyOrSellPanel.add(tickerText, gbc);

    // Second row: Shares label and text field
    gbc.gridx = 0;
    gbc.gridy = 1;
    gbc.anchor = GridBagConstraints.EAST;
    buyOrSellPanel.add(new JLabel("Enter Number of Shares:"), gbc);

    gbc.gridx = 1;
    gbc.gridy = 1;
    gbc.anchor = GridBagConstraints.WEST;
    sharesText = new JTextField(5);
    buyOrSellPanel.add(sharesText, gbc);

    // Portfolio text field and button
    gbc.gridx = 0;
    gbc.gridy = 2;
    gbc.anchor = GridBagConstraints.EAST;
    buyOrSellPanel.add(new JLabel("Enter Portfolio:"), gbc);

    gbc.gridx = 1;
    gbc.gridy = 2;
    gbc.anchor = GridBagConstraints.WEST;
    portfolioText = new JTextField(10);
    buyOrSellPanel.add(portfolioText, gbc);

    return gbc;
  }

  private void removePanel() {
    if (portfolioPanel != null) {
      this.remove(portfolioPanel);
    }
    if (buyOrSellPanel != null) {
      this.remove(buyOrSellPanel);
    }
    if (loadPortfolioPanel != null) {
      this.remove(loadPortfolioPanel);
    }
    if (assetOrCompositionPanel != null) {
      this.remove(assetOrCompositionPanel);
    }
  }

  private void buyStock() {
    GridBagConstraints gbc = buyOrSellFeatures();

    // Add date fields
    addDateFields(gbc);

    gbc.gridx = 1;
    gbc.gridy = 4;
    buyOrSellPanel.add(buyStockButton, gbc);

    this.add(buyOrSellPanel);
    this.revalidate();
    this.repaint();
  }

  private void sellStock() {
    GridBagConstraints gbc = buyOrSellFeatures();

    // Add date fields
    addDateFields(gbc);

    gbc.gridx = 1;
    gbc.gridy = 4;
    buyOrSellPanel.add(sellStockButton, gbc);

    this.add(buyOrSellPanel);
    this.revalidate();
    this.repaint();
  }

  private GridBagConstraints totalAssetsOrComposition() {
    removePanel();
    assetOrCompositionPanel = new JPanel();
    assetOrCompositionPanel.setLayout(new GridBagLayout());
    GridBagConstraints gbc = new GridBagConstraints();

    gbc.insets = new Insets(5, 5, 5, 5);

    // First row: Portfolio name label and text field
    gbc.gridx = 0;
    gbc.gridy = 0;
    gbc.anchor = GridBagConstraints.EAST;
    assetOrCompositionPanel.add(new JLabel("Enter Portfolio Name:"), gbc);

    gbc.gridx = 1;
    gbc.gridy = 0;
    gbc.anchor = GridBagConstraints.WEST;
    acPortfolio = new JTextField(10);
    assetOrCompositionPanel.add(acPortfolio, gbc);

    return gbc;
  }

  private void totalAssetValue() {
    GridBagConstraints gbc = totalAssetsOrComposition();

    // Add date fields
    addDateFieldsAC(gbc);

    gbc.gridx = 1;
    gbc.gridy = 4;
    assetOrCompositionPanel.add(totalAssetValueButton, gbc);

    this.add(assetOrCompositionPanel);
    this.revalidate();
    this.repaint();
  }

  private void getComposition() {
    GridBagConstraints gbc = totalAssetsOrComposition();

    // Add date fields
    addDateFieldsAC(gbc);

    gbc.gridx = 1;
    gbc.gridy = 4;
    assetOrCompositionPanel.add(getCompositionButton, gbc);

    this.add(assetOrCompositionPanel);
    this.revalidate();
    this.repaint();
  }

  private void addDateFields(GridBagConstraints gbc) {
    gbc.gridx = 0;
    gbc.gridy = 3;
    gbc.anchor = GridBagConstraints.EAST;
    buyOrSellPanel.add(new JLabel("Enter Date:"), gbc);

    gbc.gridx = 1;
    gbc.gridy = 3;
    gbc.anchor = GridBagConstraints.WEST;

    JSpinner datePicker = new JSpinner(new SpinnerDateModel());
    JSpinner.DateEditor dateEditor = new JSpinner.DateEditor(datePicker, "yyyy-MM-dd");
    datePicker.setEditor(dateEditor);

    datePicker.addChangeListener(new ChangeListener() {
      public void stateChanged(ChangeEvent e) {
        Date selectedDate = (Date) datePicker.getValue();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        JTextField date = new JTextField(dateFormat.format(selectedDate));
        localDate = LocalDate.parse(date.getText());
      }
    });

    buyOrSellPanel.add(datePicker, gbc);
  }

  private void addDateFieldsAC(GridBagConstraints gbc) {
    gbc.gridx = 0;
    gbc.gridy = 2;
    gbc.anchor = GridBagConstraints.EAST;
    assetOrCompositionPanel.add(new JLabel("Enter Date:"), gbc);

    gbc.gridx = 1;
    gbc.gridy = 2;
    gbc.anchor = GridBagConstraints.WEST;

    JSpinner datePicker = new JSpinner(new SpinnerDateModel());
    JSpinner.DateEditor dateEditor = new JSpinner.DateEditor(datePicker, "yyyy-MM-dd");
    datePicker.setEditor(dateEditor);

    datePicker.addChangeListener(new ChangeListener() {
      public void stateChanged(ChangeEvent e) {
        Date selectedDate = (Date) datePicker.getValue();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        JTextField dateAC = new JTextField(dateFormat.format(selectedDate));
        localDateAC = LocalDate.parse(dateAC.getText());
      }
    });

    assetOrCompositionPanel.add(datePicker, gbc);
  }

  @Override
  public void addFeatures(Features features) {
    addPortfolioButton.addActionListener(evt -> {
      try {
        features.addPortfolioOutput(portfolioNameText.getText());
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
    });

    buyStockButton.addActionListener(evt -> {
      List<String> list = new ArrayList<>();
      list.add(tickerText.getText());
      list.add(sharesText.getText());
      list.add(portfolioText.getText());
      try {
        features.buyStockOutput(list, localDate);
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
    });

    sellStockButton.addActionListener(evt -> {
      List<String> list = new ArrayList<>();
      list.add(tickerText.getText());
      list.add(sharesText.getText());
      list.add(portfolioText.getText());
      try {
        features.sellStockOutput(list, localDate);
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
    });

    loadPortfolioButton.addActionListener(evt -> {
      try {
        features.addLoadPortfolioOutput(loadPortfolioText.getText());
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
    });

    totalAssetValueButton.addActionListener(evt -> {
      try {
        features.totalAssetValueOutput(acPortfolio.getText(), localDateAC);
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
    });

    getCompositionButton.addActionListener(evt -> {
      try {
        features.getCompositionOutput(acPortfolio.getText(), localDateAC);
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
    });
  }

  @Override
  public void writeMessage(String message) {
    JLabel result = new JLabel(message);
    JOptionPane.showMessageDialog(null, result);
    revalidate();
    repaint();
  }

  @Override
  public String readInput() {
    return "";
  }
}
