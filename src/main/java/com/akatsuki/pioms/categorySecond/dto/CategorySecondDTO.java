package com.akatsuki.pioms.categorySecond.dto;

import com.akatsuki.pioms.categorySecond.aggregate.CategorySecond;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CategorySecondDTO {
    private int categorySecondCode;
    private String categorySecondName;
    private String categorySecondEnrollDate;
    private String categorySecondUpdateDate;
    private int categoryFirstCode;

    public CategorySecondDTO(CategorySecond categorySecond) {
        System.out.println("categorySecond = " + categorySecond);
        this.categorySecondCode = categorySecondCode;
        this.categorySecondName = categorySecondName;
        this.categorySecondEnrollDate = categorySecondEnrollDate;
        this.categorySecondUpdateDate = categorySecondUpdateDate;
        this.categoryFirstCode = categoryFirstCode;
    }
}
