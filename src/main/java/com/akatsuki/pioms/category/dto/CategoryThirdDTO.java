package com.akatsuki.pioms.category.dto;

import com.akatsuki.pioms.product.etc.PRODUCT_COLOR;
import com.akatsuki.pioms.product.etc.PRODUCT_GENDER;
import com.akatsuki.pioms.product.etc.PRODUCT_STATUS;
import lombok.*;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString
public class CategoryThirdDTO {

    private int category_third_code;
    private String category_third_name;
    private int product_code;
    private String product_name;
    private int product_price;
    private Date product_enroll_date;
    private String product_content;
    private PRODUCT_COLOR product_color;
    private int product_size;
    private PRODUCT_GENDER product_gender;
    private int product_total_count;
    private PRODUCT_STATUS product_status;
    private boolean product_exposure_status;
    private int product_notice_count;
    private int product_dis_count;
    private int product_count;

}
