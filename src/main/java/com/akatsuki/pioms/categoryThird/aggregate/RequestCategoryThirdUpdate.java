package com.akatsuki.pioms.categoryThird.aggregate;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class RequestCategoryThirdUpdate {
    private int categoryThirdCode;
    private String categoryThirdName;

    public RequestCategoryThirdUpdate(int category_third_code, String category_third_name) {
        this.categoryThirdCode = category_third_code;
        this.categoryThirdName = category_third_name;
    }
}
