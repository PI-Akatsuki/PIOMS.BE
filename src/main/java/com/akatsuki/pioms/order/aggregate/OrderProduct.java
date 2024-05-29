package com.akatsuki.pioms.order.aggregate;

import com.akatsuki.pioms.order.aggregate.Order;
import com.akatsuki.pioms.product.aggregate.Product;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "request_product")
public class OrderProduct {
    @Id
    @Column(name = "request_product_code")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int requestProductCode;
    @Column(name = "request_product_count")
    private int requestProductCount;
    @Column(name = "request_product_get_count")
    private int requestProductGetCount;

    @JoinColumn(name = "request_code")
    @ManyToOne
    @ToString.Exclude
    private Order order;

    @JoinColumn(name = "product_code")
    @ManyToOne
    private Product product;

    public OrderProduct(int count, int findCount, Order order, int productCode) {
        this.requestProductCount = count;
        this.requestProductGetCount = findCount;
        this.order = order;
        Product product= new Product();
        product.setProductCode(productCode);
        this.product = product;
    }


}
