package com.akatsuki.pioms.product.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Table(name = "product")
public class ProductEntity {
    @Id
    @Column(name = "product_code")
    private int productCode;
    @Column(name = "product_name")
    private String productName;
    @Column(name = "product_count")
    private int productCount;
    @Column(name = "product_dis_count")
    private int productDiscount;
    @Column(name = "product_exposure_status")
    private boolean productExposureStatus;


}
