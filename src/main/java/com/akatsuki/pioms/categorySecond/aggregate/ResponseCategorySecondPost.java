package com.akatsuki.pioms.categorySecond.aggregate;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ResponseCategorySecondPost {
    private int categorySecondCode;
    private String categorySecondName;
    private String categorySecondEnrollDate;

    public ResponseCategorySecondPost(int categorySecondCode, String categorySecondName, String categorySecondEnrollDate) {
        this.categorySecondCode = categorySecondCode;
        this.categorySecondName = categorySecondName;
        this.categorySecondEnrollDate = categorySecondEnrollDate;
    }
}
