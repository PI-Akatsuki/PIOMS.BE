package com.akatsuki.pioms.product.vo;

import com.akatsuki.pioms.product.etc.PRODUCT_COLOR;
import com.akatsuki.pioms.product.etc.PRODUCT_GENDER;
import com.akatsuki.pioms.product.etc.PRODUCT_STATUS;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Data
@Setter
@Getter
public class ResponseProductPost {

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
    public ResponseProductPost(int product_code, String product_name, int product_price, Date product_enroll_date, String product_content, PRODUCT_COLOR product_color, int product_size, PRODUCT_GENDER product_gender, int product_total_count, PRODUCT_STATUS product_status, boolean product_exposure_status, int product_notice_count, int product_dis_count, int product_count) {
        this.product_code = product_code;
        this.product_name = product_name;
        this.product_price = product_price;
        this.product_enroll_date = product_enroll_date;
        this.product_content = product_content;
        this.product_color = product_color;
        this.product_size = product_size;
        this.product_gender = product_gender;
        this.product_total_count = product_total_count;
        this.product_status = product_status;
        this.product_exposure_status = product_exposure_status;
        this.product_notice_count = product_notice_count;
        this.product_dis_count = product_dis_count;
        this.product_count = product_count;
    }
}
