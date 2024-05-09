package com.akatsuki.pioms.order.entity;

import com.akatsuki.pioms.product.entity.ProductEntity;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "request_product")
public class OrderProductEntity {
    @Id
    @Column(name = "request_product_code")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int requestProductCode;
    @Column(name = "request_product_count")
    private int requestProductCount;
    @Column(name = "request_product_get_count")
    private int requestProductGetCount;
    @JoinColumn(name = "request_code")
    @ManyToOne(cascade = CascadeType.PERSIST)
    private OrderEntity order;

    @JoinColumn(name = "product_code")
    @ManyToOne(cascade = CascadeType.PERSIST)
    private ProductEntity product;

    public OrderProductEntity(Integer count, int findCount, OrderEntity order, ProductEntity product) {
        this.requestProductCount = count;
        this.requestProductGetCount = findCount;
        this.order = order;
        this.product = product;
    }

    public OrderProductEntity(OrderEntity order1, int productCode, int count) {
        ProductEntity product1 = new ProductEntity();
        product1.setProductCode(productCode);
        this.product = product1;
        this.requestProductCount = count;
        this.requestProductGetCount = 0;
        this.order = order1;

    }
}
