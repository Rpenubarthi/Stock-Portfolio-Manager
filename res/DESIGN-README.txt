Assignment 6:
Created an interface for the new view called GraphicalUserView. This view extends the old
view interface. This is to implement a new functionality to the view and adhere to interface
segregation. This new view should mainly work with the controller using addFeatures method.

Created a new interface called Features to act as the controller between the model and the new view.
Created an implementation of this interface called FeaturesController. This controller
controls the flow of inputs from the GraphicalUserView and the Stock Commands. This still adheres
to solid principles because the Features interface and FeaturesController
have distinct, single responsibilities: defining interactions and handling input flow, respectively.
The Features interface provides only relevant functionality to the GraphicalUserView
and stock commands. It is also open to extension. If more features were to be added only the
addFeatures method would change along with the implementation of the view (ex. more buttons
and more action listeners).

Edited the existing Stock Command classes to overload constructors to make the process of passing
inputs from the GUI view easier.

As the jar files were previously not runnable since there was always a "No file at location" error,
we modified the constructor for the BasicStockModel and BasicStockPortfolio to create a csv file
at a relative location instead of having a definitive path.

Assignment 5:
Created an interface called BetterStockModel which extends StockModel. This interface now promises
methods for a portfolio such as addPortfolio, buyStock, sellStock, totalAssetValue, getComposition,
getDistribution, rebalance, loadFile, and getPerformanceOverTime. We extended the old interface
so that the new implementation will still have the same promises as before.

The new implementation is called BetterBasicStockModel. This implements new interface and extends
BasicStockModel. This is useful as it allows for the new methods to be created while keeping
the promises of the old model. It now has a list of StockPortfolio.

StockPortfolio is a new interface that represents a singular portfolio. It promises functionality
that will get info about a portfolio.

Its implementation is BasicStockPortfolio. This class represents a portfolio by its name (ex.
retirement or savings), list of tickers of the stock in that portfolio, and a file called
portfolioInfo to act as a version history for the state of the portfolio.

Changed some access modifiers in the old model to package-private.

The controller was modified to account for the new commands and now takes in
BetterStockModel. Also adjusted the menu and the way the commands parse inputs to allow
for checking for invalid user inputs and making the user interactivity smoother. Ex. inputting
dates one part at a time.

Assigment 4:
Created an interface called StockModel to show all the methods a user can use. This interface
double totalAssetValue(LocalDate date, int index) throws IOException;
promises netGain, xDayMovingAverage, xDayCrossover, updatePortfolio, totalAssetValue, setDataFile.

The implementation of this interface is BasicStockModel has two files which act as a cahce to store
inputted stock values and their tickers. It also has a list of list of stocks to act as a list of
portfolios.

Created an interface called Stock to represent a singular stock. It promises the methods getSymbol,
getShares, and addShares. These are helpful to use in the methods in the model.

The implementation of this is BasicStock. BasicStock has a ticker and number of shares
to represent stocks of a company.

Created an interface called StockView to act as a view for the program. It will output the
data representation. It promises the functionality to take in user input and print the data.

The implementation of this is StockView. It uses readable for the input and appendable for output.

Created an interface called Controller. The controller will receives input from the view, parses
it, tells the model what to do with the given data, and tells the view when to print what. It
promises a method called startProgram which will start the program in the main method.

Its implementation is StockController which uses the command design pattern to make start program
 more simple. This class takes in model and the user input and output from the view.

 The command design pattern also uses an interface called StockCommand. This promises the go method
 which takes in the model and the view.

 Its implementations are the different function objects for the commands. They parse the data, tell
 the model what to do with the inputted data, and tell the view what to output.