package labshopeventsourcing.query;


import labshopeventsourcing.event.*;

import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@ProcessingGroup("orderStatus")
public class OrderStatusQueryHandler {

    private final Map<Long, OrderStatus> data = new HashMap<>();

    @QueryHandler
    public List<OrderStatus> handle(OrderStatusQuery query) {
        return new ArrayList<>(data.values());
    }

    @EventHandler
    public void on(OrderPlacedEvent orderPlaced) {

    OrderStatus orderStatus = new OrderStatus();

        orderStatus.setId(orderPlaced.getId());
        orderStatus.setStatus(orderPlaced.getStatus());
        orderStatus.setAmount(Long.valueOf(orderPlaced.getAmount()));
        orderStatus.setQty(orderPlaced.getQty());

        data.put(orderStatus.getId(), orderStatus);
    }


    @EventHandler
    public void on(OrderDeliveryStartedEvent orderDeliveryStarted) {
            // view 객체 조회
        OrderStatus orderStatus = data.getOrDefault(orderDeliveryStarted.getId(), null);

        if( orderStatus != null) {
                
            orderStatus.setStatus("DeliveryStarted");    
            // view 레파지 토리에 save
            data.put(orderStatus.getId(), orderStatus);
        }


    }
    @EventHandler
    public void on(OrderCancelledEvent orderCancelled) {
            // view 객체 조회
        OrderStatus orderStatus = data.getOrDefault(orderCancelled.getId(), null);

        if( orderStatus != null) {
                
            orderStatus.setStatus("Cancelled");    
            // view 레파지 토리에 save
            data.put(orderStatus.getId(), orderStatus);
        }


    }

}

