package com.akatsuki.pioms.invoice.aggregate;

import com.akatsuki.pioms.franchise.aggregate.Franchise;
import com.akatsuki.pioms.order.aggregate.Order;
import com.akatsuki.pioms.order.aggregate.OrderProduct;
import com.akatsuki.pioms.order.dto.OrderProductDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ResponseInvoiceDetail {

    private Invoice invoice;
    private Order order;
    private Franchise franchise;
    private List<OrderProduct> orderProductDTOS;

}
