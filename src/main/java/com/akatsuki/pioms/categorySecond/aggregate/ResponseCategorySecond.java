package com.akatsuki.pioms.categorySecond.aggregate;

import com.akatsuki.pioms.categorySecond.dto.CategorySecondDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ResponseCategorySecond {
    private int categorySecondCode;
    private String categorySecondName;
    private String categorySecondEnrollDate;
    private String categorySecondUpdateDate;

    public ResponseCategorySecond(CategorySecond categorySecond) {
        this.categorySecondCode = categorySecond.getCategorySecondCode();
        this.categorySecondName = categorySecond.getCategorySecondName();
        this.categorySecondEnrollDate = categorySecond.getCategorySecondEnrollDate();
        this.categorySecondUpdateDate = categorySecond.getCategorySecondUpdateDate();
    }

    public ResponseCategorySecond(CategorySecondDTO categorySecondDTO) {
        this.categorySecondCode = categorySecondDTO.getCategorySecondCode();
        this.categorySecondName = categorySecondDTO.getCategorySecondName();
        this.categorySecondEnrollDate = categorySecondDTO.getCategorySecondEnrollDate();
        this.categorySecondUpdateDate = categorySecondDTO.getCategorySecondUpdateDate();
    }
}
