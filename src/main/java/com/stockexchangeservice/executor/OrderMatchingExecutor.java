package com.stockexchangeservice.executor;

import com.stockexchangeservice.model.pojos.OrderDetails;

/**
 * Interface for various order matching systems. (FIFO, Pro-Rata and ...)
 */
public interface OrderMatchingExecutor {

    /**
     * Method to perform order matching on given order.
     *
     * @param orderDetails {@link OrderDetails}
     *
     */
    void orderMatchingSystem(OrderDetails orderDetails);
}
