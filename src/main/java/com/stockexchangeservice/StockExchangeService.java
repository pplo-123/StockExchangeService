package com.stockexchangeservice;

import static com.stockexchangeservice.constants.StockExchangeServiceConstants.SPACE_LITERAL;

import com.stockexchangeservice.executor.impl.FIFOOrderMatchingExecutor;
import com.stockexchangeservice.model.enums.OrderType;
import com.stockexchangeservice.model.pojos.OrderDetails;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;
import lombok.extern.log4j.Log4j2;

/**
 * Activity layer for Stock Exchange Service App
 */
@Log4j2
public class StockExchangeService {

    public static void main(String[] args) {
        log.info("Stock Exchange Service has started!!!");
        final int argumentSize = args.length;
        if(argumentSize<=0){
            log.info("Invalid command line input for file name. Please enter a valid file name.");
            System.exit(0);
        }
        final String textFilePath = args[0];
        List<OrderDetails> orderDetailsList = new ArrayList<>();
        try (Stream<String> stream = Files.lines(Paths.get(textFilePath))) {
            stream.forEachOrdered(line -> orderDetailsList.add(buildOrderObject(line)));
        } catch (FileNotFoundException ex) {
            log.info("The file: {} is not present at the given location.", textFilePath,ex);
            System.exit(0);
        } catch (IOException ex) {
            log.info("Encountered unknown exception for file: {}", textFilePath, ex);
            System.exit(0);
        }

        FIFOOrderMatchingExecutor fifoOrderMatchingExecutor = new FIFOOrderMatchingExecutor();
        for(OrderDetails orderDetails : orderDetailsList) {
            fifoOrderMatchingExecutor.orderMatchingSystem(orderDetails);
        }
    }

    /**
     * Method to parse order details (String) to Order {@link OrderDetails} POJO.
     *
     * @param orderDetails String
     * @return  Order POJO {@link OrderDetails}
     */
    private static OrderDetails buildOrderObject(final String orderDetails) {
        String[] splitOrderDetails = orderDetails.split(SPACE_LITERAL);
        return OrderDetails.builder()
            .orderId(splitOrderDetails[0])
            .orderPlacedTime(LocalTime.parse(splitOrderDetails[1]))
            .stockName(splitOrderDetails[2])
            .orderType(OrderType.valueOf(splitOrderDetails[3].toUpperCase()))
            .stockPrice(Double.valueOf(splitOrderDetails[4]))
            .quantity(Long.valueOf(splitOrderDetails[5]))
            .build();
    }
}
