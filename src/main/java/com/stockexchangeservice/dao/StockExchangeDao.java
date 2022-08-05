package com.stockexchangeservice.dao;

import com.stockexchangeservice.model.pojos.OrderDetails;
import com.stockexchangeservice.model.pojos.OrderMatchDetails;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * In memory DAO for the exchange service
 */
public class StockExchangeDao {

    private Map<String, Set<OrderDetails>> buyOrdersMap;
    private Map<String, Set<OrderDetails>> sellOrdersMap;

    private Set<OrderMatchDetails> orderMatchDetails;

    public StockExchangeDao() {
        this.buyOrdersMap = new ConcurrentHashMap<>();
        this.sellOrdersMap = new ConcurrentHashMap<>();
        this.orderMatchDetails = new LinkedHashSet<>();
    }

    public Map<String, Set<OrderDetails>> getBuyOrdersMap() {
        return buyOrdersMap;
    }

    public Map<String, Set<OrderDetails>> getSellOrdersMap() {
        return sellOrdersMap;
    }

    public Set<OrderMatchDetails> getOrderMatchDetails() {
        return orderMatchDetails;
    }
}
