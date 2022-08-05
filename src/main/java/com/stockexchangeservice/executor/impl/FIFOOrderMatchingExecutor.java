package com.stockexchangeservice.executor.impl;

import com.stockexchangeservice.dao.StockExchangeDao;
import com.stockexchangeservice.executor.OrderMatchingExecutor;
import com.stockexchangeservice.model.enums.OrderType;
import com.stockexchangeservice.model.pojos.OrderDetails;
import com.stockexchangeservice.model.pojos.OrderMatchDetails;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.NonNull;

/**
 * Concrete implementation of {@link OrderMatchingExecutor} for FIFO technique.
 */
@AllArgsConstructor
public class FIFOOrderMatchingExecutor implements OrderMatchingExecutor{
    private StockExchangeDao stockExchangeDao;

    public FIFOOrderMatchingExecutor() {
        stockExchangeDao = new StockExchangeDao();
    }

    /**
     *{@inheritDoc}
     */
    public void orderMatchingSystem(@NonNull final OrderDetails orderDetails) {
        Map<String, Set<OrderDetails>> buyOrdersMap = stockExchangeDao.getBuyOrdersMap();
        Map<String, Set<OrderDetails>> sellOrdersMap = stockExchangeDao.getSellOrdersMap();
        if(OrderType.BUY.equals(orderDetails.getOrderType())) {
            processBuyOrder(orderDetails,buyOrdersMap,sellOrdersMap);
        } else {
            processSellOrder(orderDetails,buyOrdersMap,sellOrdersMap);
        }
    }

    /**
     * Method to process the sell order.
     *
     * @param sellOrderDetails
     */
    private void processSellOrder(OrderDetails sellOrderDetails, Map<String, Set<OrderDetails>> buyOrdersMap,
        Map<String, Set<OrderDetails>> sellOrdersMap) {
        addSellOrder(sellOrderDetails, sellOrdersMap);
        if(buyOrdersMap.containsKey(sellOrderDetails.getStockName())) {
            Set<OrderDetails> buyList = buyOrdersMap.get(sellOrderDetails.getStockName());
            for(Iterator iterator = buyList.iterator(); iterator.hasNext();) {
                OrderDetails buyOrderDetails = (OrderDetails) iterator.next();
                if(buyOrderDetails.getStockPrice().compareTo(sellOrderDetails.getStockPrice())>=0) {
                    executeSellOrder(buyOrderDetails, sellOrderDetails, iterator);
                }
            }
        }
    }

    /**
     * Method to process the buy order.
     * @param buyOrderDetails
     */
    private void processBuyOrder(OrderDetails buyOrderDetails,Map<String, Set<OrderDetails>> buyOrdersMap,
        Map<String, Set<OrderDetails>> sellOrdersMap) {
        addBuyOrder(buyOrderDetails, buyOrdersMap);
        if(sellOrdersMap.containsKey(buyOrderDetails.getStockName())) {
            Set<OrderDetails> sellList = sellOrdersMap.get(buyOrderDetails.getStockName());
            for(Iterator iterator = sellList.iterator(); iterator.hasNext();) {
                OrderDetails sellOrderDetails = (OrderDetails) iterator.next();
                if(buyOrderDetails.getQuantity()<=0) {
                    return;
                }
                if(buyOrderDetails.getStockPrice().compareTo(sellOrderDetails.getStockPrice())>=0) {
                    executeBuyOrder(buyOrderDetails, sellOrderDetails, iterator);
                }
            }
        }

    }

    /**
     * Method to add sell order in the trade book.
     *
     * @param orderDetails
     * @param sellOrdersMap
     */
    private void addSellOrder(OrderDetails orderDetails, Map<String, Set<OrderDetails>> sellOrdersMap) {
        if(sellOrdersMap.containsKey(orderDetails.getStockName())){
            sellOrdersMap.get(orderDetails.getStockName()).add(orderDetails);
        } else{
            Set<OrderDetails> sellList = new LinkedHashSet<>();
            sellList.add(orderDetails);
            sellOrdersMap.put(orderDetails.getStockName(),sellList);
        }
    }

    /**
     * Method to add buy order in trade book.
     *
     * @param orderDetails
     * @param buyOrdersMap
     */
    private void addBuyOrder(OrderDetails orderDetails, Map<String, Set<OrderDetails>> buyOrdersMap) {
        if(buyOrdersMap.containsKey(orderDetails.getStockName())){
            buyOrdersMap.get(orderDetails.getStockName()).add(orderDetails);
        } else{
            Set<OrderDetails> buyList = new LinkedHashSet<>();
            buyList.add(orderDetails);
            buyOrdersMap.put(orderDetails.getStockName(),buyList);
        }
    }

    /**
     * Method to execute the sell order.
     *
     * @param buyOrderDetails
     * @param sellOrderDetails
     * @param iterator
     */
    private void executeSellOrder(OrderDetails buyOrderDetails, OrderDetails sellOrderDetails, Iterator iterator) {

        Long quantityTraded;
        if(buyOrderDetails.getQuantity()<=sellOrderDetails.getQuantity()){
            if(buyOrderDetails.getQuantity().equals(sellOrderDetails.getQuantity())){
                stockExchangeDao.getSellOrdersMap().get(sellOrderDetails.getStockName()).remove(sellOrderDetails);
            } else{
                sellOrderDetails.setQuantity(sellOrderDetails.getQuantity()-buyOrderDetails.getQuantity());
            }
            quantityTraded = buyOrderDetails.getQuantity();
            iterator.remove();
        } else{
            quantityTraded = sellOrderDetails.getQuantity();
            buyOrderDetails.setQuantity(buyOrderDetails.getQuantity() - sellOrderDetails.getQuantity());
            stockExchangeDao.getSellOrdersMap().get(sellOrderDetails.getStockName()).remove(sellOrderDetails);
            sellOrderDetails.setQuantity(sellOrderDetails.getQuantity() - buyOrderDetails.getQuantity());
        }
        OrderMatchDetails orderMatchDetails = OrderMatchDetails.builder()
            .buyOrderId(buyOrderDetails.getOrderId()).sellPrice(sellOrderDetails.getStockPrice())
            .quantity(quantityTraded).sellOrderId(sellOrderDetails.getOrderId())
            .build();
        stockExchangeDao.getOrderMatchDetails().add(orderMatchDetails);
        System.out.println(orderMatchDetails.toString());
    }

    /**
     * Method to execute the buy order.
     *
     * @param buyOrderDetails
     * @param sellOrderDetails
     * @param iterator
     */
    private void executeBuyOrder(OrderDetails buyOrderDetails, OrderDetails sellOrderDetails, Iterator iterator) {
        Long quantityTraded;
        if(buyOrderDetails.getQuantity()<=sellOrderDetails.getQuantity()){
            if(buyOrderDetails.getQuantity().equals(sellOrderDetails.getQuantity())){
                stockExchangeDao.getSellOrdersMap().get(sellOrderDetails.getStockName()).remove(sellOrderDetails);
            } else{
                sellOrderDetails.setQuantity(sellOrderDetails.getQuantity()-buyOrderDetails.getQuantity());
            }
            quantityTraded = buyOrderDetails.getQuantity();
            stockExchangeDao.getBuyOrdersMap().get(buyOrderDetails.getStockName()).remove(buyOrderDetails);
        } else{
            quantityTraded = sellOrderDetails.getQuantity();
            buyOrderDetails.setQuantity(buyOrderDetails.getQuantity() - sellOrderDetails.getQuantity());
            iterator.remove();
        }
        OrderMatchDetails orderMatchDetails = OrderMatchDetails.builder()
            .buyOrderId(buyOrderDetails.getOrderId()).sellPrice(sellOrderDetails.getStockPrice())
            .quantity(quantityTraded).sellOrderId(sellOrderDetails.getOrderId())
            .build();
        stockExchangeDao.getOrderMatchDetails().add(orderMatchDetails);
        System.out.println(orderMatchDetails.toString());
    }
}
