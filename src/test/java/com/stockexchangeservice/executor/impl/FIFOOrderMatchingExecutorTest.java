package com.stockexchangeservice.executor.impl;

import com.stockexchangeservice.model.enums.OrderType;
import com.stockexchangeservice.model.pojos.OrderDetails;
import java.time.LocalTime;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import org.junit.Before;
import org.junit.Test;

/**
 * Test class for {@link FIFOOrderMatchingExecutor}
 */
public class FIFOOrderMatchingExecutorTest {
    OrderDetails sellOrder = OrderDetails.builder()
        .stockPrice(237.45)
        .stockName("BAC")
        .quantity(90L)
        .orderType(OrderType.SELL)
        .orderPlacedTime(LocalTime.MIN)
        .orderId("#1")
        .build();

    OrderDetails buyOrder = OrderDetails.builder()
        .stockPrice(238.12)
        .stockName("BAC")
        .quantity(110L)
        .orderType(OrderType.BUY)
        .orderPlacedTime(LocalTime.MIN)
        .orderId("#2")
        .build();
    private FIFOOrderMatchingExecutor fifoOrderMatchingExecutor;
    @Before
    public void setup() {
        Map<String, Set<OrderDetails>> buyOrderMap = new ConcurrentHashMap<>();
        Set<OrderDetails> buyOrders = new LinkedHashSet<>();
        buyOrders.add(buyOrder);
        buyOrderMap.put("BAC",buyOrders);
        Map<String, Set<OrderDetails>> sellOrderMap = new ConcurrentHashMap<>();
        Set<OrderDetails> sellOrders = new LinkedHashSet<>();
        sellOrders.add(sellOrder);
        sellOrderMap.put("BAC",sellOrders);
        fifoOrderMatchingExecutor = new FIFOOrderMatchingExecutor();
    }
    @Test
    public void testOrderMatcherForBuyOrder() {
        fifoOrderMatchingExecutor.orderMatchingSystem(buyOrder);
    }

    @Test
    public void testOrderMatcherForSellOrder() {
        fifoOrderMatchingExecutor.orderMatchingSystem(sellOrder);
    }
}