package leetcode;

/**
 * DESCRIPTION :
 *Say you have an array for which the ith element is the price of a given stock on day i.
 *
 * If you were only permitted to complete at most one transaction (i.e., buy one and sell one share of the stock), design an algorithm to find the maximum profit.
 *
 * Note that you cannot sell a stock before you buy one.
 *
 * Example 1:
 *
 * Input: [7,1,5,3,6,4]
 * Output: 5
 * Explanation: Buy on day 2 (price = 1) and sell on day 5 (price = 6), profit = 6-1 = 5.
 *              Not 7-1 = 6, as selling price needs to be larger than buying price.
 * Example 2:
 *
 * Input: [7,6,4,3,1]
 * Output: 0
 * Explanation: In this case, no transaction is done, i.e. max profit = 0.

 * @author tym
 * @ceeate 2020/1/21
 **/
public class MaxProfit {

    public int maxProfit(int[] prices) {
        int max = 0;
        for (int i=0; i<prices.length; i++) {
            int price1 = prices[i];
            for(int j=i+1;j<prices.length; j++) {
                int price2 = prices[j];
                int value = price2 - price1;
                if(value > 0 && value > max) {
                    max = value;
                }
            }
        }
        return  max;
    }

    public int maxProfit2(int[] prices) {
        int minprice = Integer.MAX_VALUE;
        int maxprofit = 0;
        for(int i =0 ;i<prices.length;i++) {
            if(prices[i] < minprice) {
                minprice = prices[i];
            }
            if(prices[i] - minprice > maxprofit) {
                maxprofit = prices[i] - minprice;
            }
        }
        return  maxprofit;
    }

}
