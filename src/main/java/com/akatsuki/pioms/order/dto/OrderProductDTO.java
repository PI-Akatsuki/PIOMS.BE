package com.akatsuki.pioms.order.dto;

import com.akatsuki.pioms.order.aggregate.Order;
import com.akatsuki.pioms.order.aggregate.OrderProduct;
import com.akatsuki.pioms.product.aggregate.Product;
import com.akatsuki.pioms.product.dto.ProductDTO;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class OrderProductDTO {
    private int requestProductCode;
    private int requestProductCount;
    private int requestProductGetCount;
    private OrderDTO order;
    private ProductDTO product;

    public OrderProductDTO(OrderProduct orderProduct) {
        this.requestProductCode = orderProduct.getRequestProductCode();
        this.requestProductCount = orderProduct.getRequestProductCount();
        this.requestProductGetCount = orderProduct.getRequestProductGetCount();
        this.order = new OrderDTO(orderProduct.getOrder());
        this.product = new ProductDTO(orderProduct.getProduct());
    }
}
