package com.akatsuki.pioms.product.entity;

import com.akatsuki.pioms.category.entity.CategoryThird;
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
public class Product {

    @Id
    @Column(name = "product_code")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int productCode;

    @Column(name = "product_name")
    private String productName;

    @Column(name = "product_price")
    private int product_price;

    @Column(name = "product_enroll_date")
    private Date product_enroll_date;

    @Column(name = "product_update_date")
    private Date product_update_date;

    @Column(name = "product_content")
    private String product_content;

    @Column(name = "product_color")
    @Enumerated(EnumType.STRING)
    private PRODUCT_COLOR product_color;

    @Column(name = "product_size")
    private int product_size;

    @Column(name = "product_gender")
    @Enumerated(EnumType.STRING)
    private PRODUCT_GENDER product_gender;

    @Column(name = "product_total_count")
    private int product_total_count;

    @Column(name = "product_status")
    @Enumerated(EnumType.STRING)
    private PRODUCT_STATUS product_status;

    @Column(name = "product_exposure_status")
    private boolean product_exposure_status;

    @Column(name = "product_notice_count")
    private int product_notice_count;

    @Column(name = "product_dis_count")
    private int product_dis_count;

    @Column(name = "product_count")
    private int product_count;

    @ManyToOne
    @JoinColumn(name="category_third_code")
    private CategoryThird category_third_code;

}
