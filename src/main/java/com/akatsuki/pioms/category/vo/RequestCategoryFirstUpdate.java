package com.akatsuki.pioms.category.vo;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Setter
@Getter
public class RequestCategoryFirstUpdate {
    private int category_first_code;
    private String category_first_name;

    public RequestCategoryFirstUpdate(int category_first_code, String category_first_name) {
        this.category_first_code = category_first_code;
        this.category_first_name = category_first_name;
    }
}
