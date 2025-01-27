HOW TO RUN THE PROGRAM:
1. Go to src/model/AlphaVantageDemo.java and change the variable apiKey to the new apiKey 
2. In the terminal/command line, change directory to the res folder of the project
3. If the text-based interface is wanted, run java -jar Stock_Portfolio_Text_View.jar
4. If the graphical user interface is wanted, run java -jar Stock_Portfolio_GUI_View.jar


To create a portfolio with 3 different stocks on the text-based view, do these commands:
add-portfolio portfolio-name
buy-stock ticker shares portfolio-name date

For example: to create a portfolio called "myPortfolio" with shares of AAPL, NVDA, and AMZN
add-portfolio
myPortfolio
buy-stock
AAPL
10
myPortfolio
2024
05
01
buy-stock
NVDA
15
myPortfolio
2024
05
13
buy-stock
AMZN
5
myPortfolio
2024
05
13

To query their values you can use the command:
get-asset-value portfolio-name date

For example I could do: for 2024-05-13
get-asset-value
myPortfolio
2024
05
13

For another date I could do: for 2024-06-05
get-asset-value
myPortfolio
2024
06
05

The GUI-based view has options at the top of the view that allows for the same functionality as the text-based 
interface above.

Our program supports a list of active or delisted US stocks and ETFs,
either as of the latest trading day or at a specific time in history.



