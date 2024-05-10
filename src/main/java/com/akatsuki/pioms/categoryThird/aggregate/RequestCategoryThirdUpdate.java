package com.akatsuki.pioms.categoryThird.aggregate;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class RequestCategoryThirdUpdate {
    private int category_third_code;
    private String category_third_name;

    public RequestCategoryThirdUpdate(int category_third_code, String category_third_name) {
        this.category_third_code = category_third_code;
        this.category_third_name = category_third_name;
    }
}
