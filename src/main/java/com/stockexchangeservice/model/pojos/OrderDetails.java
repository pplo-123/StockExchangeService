package com.stockexchangeservice.model.pojos;

import com.stockexchangeservice.model.enums.OrderType;
import java.math.BigDecimal;
import java.time.LocalTime;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

/**
 * Class to hold order details placed for a stock.
 */
@Getter
@Setter
@Builder
public class OrderDetails {
    @NonNull
    private String orderId;

    @NonNull
    private LocalTime orderPlacedTime;

    @NonNull
    private String stockName;

    @NonNull
    private OrderType orderType;

    @NonNull
    private Double stockPrice;

    @NonNull
    private Long quantity;

}
