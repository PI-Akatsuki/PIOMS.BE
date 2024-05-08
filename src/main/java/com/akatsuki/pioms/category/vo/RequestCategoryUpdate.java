package com.akatsuki.pioms.category.vo;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class RequestCategoryUpdate {
    private int category_third_code;
    private String category_third_name;

    public RequestCategoryUpdate(int category_third_code, String category_third_name) {
        this.category_third_code = category_third_code;
        this.category_third_name = category_third_name;
    }
}
