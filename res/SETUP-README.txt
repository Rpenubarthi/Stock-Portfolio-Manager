HOW TO RUN THE PROGRAM:
1. In the terminal/command line, change directory to the res folder of the project
2. If the text-based interface is wanted, run Assignment6_Text_View.jar
3. If the graphical user interface is wanted, run Assignment6_GUI_View.jar

To create a portfolio with 3 different stocks, do these commands:
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

Our program supports a list of active or delisted US stocks and ETFs,
either as of the latest trading day or at a specific time in history.



