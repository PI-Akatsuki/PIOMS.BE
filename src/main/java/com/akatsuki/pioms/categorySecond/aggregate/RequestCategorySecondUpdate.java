package com.akatsuki.pioms.categorySecond.aggregate;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RequestCategorySecondUpdate {
    private int categorySecondCode;
    private String categorySecondName;

    public RequestCategorySecondUpdate(int categorySecondCode, String categorySecondName) {
        this.categorySecondCode = categorySecondCode;
        this.categorySecondName = categorySecondName;
    }
}
