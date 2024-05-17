package com.akatsuki.pioms.categorySecond.aggregate;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Setter
@Getter
public class ResponseCategorySecondUpdate {
    private int categorySecondCode;
    private String categorySecondName;
    private String categorySecondUpdateDate;

    public ResponseCategorySecondUpdate(int categorySecondCode, String categorySecondName, String categorySecondUpdateDate) {
        this.categorySecondCode = categorySecondCode;
        this.categorySecondName = categorySecondName;
        this.categorySecondUpdateDate = categorySecondUpdateDate;
    }
}
