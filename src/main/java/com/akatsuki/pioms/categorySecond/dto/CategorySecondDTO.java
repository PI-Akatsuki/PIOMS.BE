package com.akatsuki.pioms.categorySecond.dto;

import com.akatsuki.pioms.categorySecond.aggregate.CategorySecond;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategorySecondDTO {
    private int categorySecondCode;
    private String categorySecondName;
    private String categorySecondEnrollDate;
    private String categorySecondUpdateDate;
    private int categoryFirstCode;

    public CategorySecondDTO(CategorySecond categorySecond) {
        System.out.println("categorySecond = " + categorySecond);
        this.categorySecondCode = categorySecond.getCategorySecondCode();
        this.categorySecondName = categorySecond.getCategorySecondName();
        this.categorySecondEnrollDate = categorySecond.getCategorySecondEnrollDate();
        this.categorySecondUpdateDate = categorySecond.getCategorySecondUpdateDate();
        this.categoryFirstCode = categorySecond.getCategoryFirst().getCategoryFirstCode();
    }
}
