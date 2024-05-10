package com.akatsuki.pioms.category.vo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RequestCategorySecondUpdate {
    private int category_second_code;
    private String category_second_name;

    public RequestCategorySecondUpdate(int category_second_code, String category_second_name) {
        this.category_second_code = category_second_code;
        this.category_second_name = category_second_name;
    }
}
