package com.akatsuki.pioms.product.aggregate;

import com.akatsuki.pioms.categoryThird.aggregate.CategoryThird;
import com.akatsuki.pioms.product.etc.PRODUCT_COLOR;
import com.akatsuki.pioms.product.etc.PRODUCT_GENDER;
import com.akatsuki.pioms.product.etc.PRODUCT_STATUS;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Table(name="product")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Product {

    @Id
    @Column(name = "product_code")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int productCode;

    @Column(name = "product_name")
    private String productName;

    @Column(name = "product_price")
    private int productPrice;

    @Column(name = "product_enroll_date")
    private String productEnrollDate;

    @Column(name = "product_update_date")
    private String productUpdateDate;

    @Column(name = "product_content")
    private String productContent;

    @Column(name = "product_color")
    @Enumerated(EnumType.STRING)
    private PRODUCT_COLOR productColor;

    @Column(name = "product_size")
    private int productSize;

    @Column(name = "product_gender")
    @Enumerated(EnumType.STRING)
    private PRODUCT_GENDER productGender;

    @Column(name = "product_total_count")
    private int productTotalCount;

    @Column(name = "product_status")
    @Enumerated(EnumType.STRING)
    private PRODUCT_STATUS productStatus;

    @Column(name = "product_exposure_status", columnDefinition = "boolean default false")
    private boolean productExposureStatus;

    @Column(name = "product_notice_count")
    private int productNoticeCount;

    @Column(name = "product_dis_count")
    private int productDiscount;

    @Column(name = "product_count")
    private int productCount;

    @ManyToOne
    @JoinColumn(name="category_third_code")
    private CategoryThird categoryThird;
}
