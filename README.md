# Overview
This service holds implementation of a FIFO order matching system which says *The first order in the order-book at a price level is the first order matched. All orders at the same price level are filled according to time priority." The exchange works like a market where lower selling prices and higher buying prices get priority.*

*A trade is executed when a buy price is greater than or equal to a sell price. The trade is recorded at the price of the sell order regardless of the price of the buy order.*

### Input Format
1. Order ID.
2. Time.
3. Stock.
4. Buy/Sell.
5. Price.
6. Quantity. 

### Output Format
1. Buy Order ID
2. Sell Price
3. Quantity
4. Sell Order ID

### Sample Input 
1. #1 09:45 BAC sell 240.12 100
2. #2 09:46 BAC sell 237.45 90
3. #3 09:47 BAC buy 238.10 110
4. #4 09:48 BAC buy 237.80 10
5. #5 09:49 BAC buy 237.80 40
6. #6 09:50 BAC sell 236.00 50

### Sample Output 
1. #3 237.45 90 #2
2. #3 236.00 20 #6
3. #4 236.00 10 #6
4. #5 236.00 20 #6

## Run Locally
1. Pull the code in your workspace. 
2. Compile and run it with the file name (here it is ``input.txt``) as command line argument.
3. The output for that particular file will be printed to console.
