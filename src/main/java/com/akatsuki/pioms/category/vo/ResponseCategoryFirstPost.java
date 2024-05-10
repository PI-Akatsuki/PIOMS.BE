package com.akatsuki.pioms.category.vo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class ResponseCategoryFirstPost {

    private int category_first_code;
    private String category_first_name;
    private String category_first_enroll_date;

    public ResponseCategoryFirstPost(int category_first_code, String category_first_name,String category_first_enroll_date) {
        this.category_first_code = category_first_code;
        this.category_first_name = category_first_name;
        this.category_first_enroll_date = category_first_enroll_date;
    }



}
