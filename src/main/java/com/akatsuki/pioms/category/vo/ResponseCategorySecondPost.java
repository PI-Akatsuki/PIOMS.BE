package com.akatsuki.pioms.category.vo;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ResponseCategorySecondPost {
    private int category_second_code;
    private String category_second_name;
    private String category_second_enroll_date;

    public ResponseCategorySecondPost(int category_second_code, String category_second_name, String category_second_enroll_date) {
        this.category_second_code = category_second_code;
        this.category_second_name = category_second_name;
        this.category_second_enroll_date = category_second_enroll_date;
    }
}
