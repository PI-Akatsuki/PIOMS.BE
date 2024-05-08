package com.akatsuki.pioms.category.vo;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class RequestCategoryPost {
    private int category_second_code;
    private String category_third_name;

    public RequestCategoryPost(int category_second_code, String category_third_name) {
        this.category_second_code = category_second_code;
        this.category_third_name = category_third_name;
    }
}
