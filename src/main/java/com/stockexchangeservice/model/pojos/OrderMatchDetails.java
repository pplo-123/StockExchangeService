package com.stockexchangeservice.model.pojos;

import java.math.BigDecimal;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

/**
 * Class to hold details matched order.
 */
@Builder
public class OrderMatchDetails {

    @NonNull
    private String buyOrderId;

    @NonNull
    private Double sellPrice;

    @NonNull
    private Long quantity;

    @NonNull
    private String sellOrderId;

    @Override
    public String toString() {
        return this.buyOrderId + " " + this.sellPrice + " " + this.quantity + " " + this.sellOrderId;
    }
}
