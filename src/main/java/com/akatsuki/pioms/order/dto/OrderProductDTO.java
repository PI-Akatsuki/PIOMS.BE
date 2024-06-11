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
import lombok.ToString;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class OrderProductDTO {
    private int requestProductCode;
    private int requestProductCount;
    private int requestProductGetCount;
    private int orderCode;
    private int productCode;
    private String productName;
    private int productSize;
    private String productColor;
    public OrderProductDTO(OrderProduct orderProduct) {
        this.requestProductCode= orderProduct.getRequestProductCode();
        this.requestProductCount= orderProduct.getRequestProductCount();
        this.requestProductGetCount= orderProduct.getRequestProductGetCount();
        this.orderCode= orderProduct.getOrder().getOrderCode();
        this.productCode= orderProduct.getProduct().getProductCode();
        this.productName = orderProduct.getProduct().getProductName();
        this.productSize = orderProduct.getProduct().getProductSize();
        this.productColor = orderProduct.getProduct().getProductColor().name();
    }
}
