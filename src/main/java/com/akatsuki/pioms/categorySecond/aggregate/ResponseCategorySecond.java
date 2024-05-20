package com.akatsuki.pioms.categorySecond.aggregate;

import com.akatsuki.pioms.categorySecond.dto.CategorySecondDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Setter
public class ResponseCategorySecond {
    private int categorySecondCode;
    private String categorySecondName;
    private String categorySecondEnrollDate;
    private String categorySecondUpdateDate;
    private int categoryFirstCode;

    public ResponseCategorySecond(CategorySecond categorySecond) {
        this.categorySecondCode = categorySecond.getCategorySecondCode();
        this.categorySecondName = categorySecond.getCategorySecondName();
        this.categorySecondEnrollDate = categorySecond.getCategorySecondEnrollDate();
        this.categorySecondUpdateDate = categorySecond.getCategorySecondUpdateDate();
        this.categoryFirstCode = categorySecond.getCategoryFirst().getCategoryFirstCode();
    }

    public ResponseCategorySecond(CategorySecondDTO categorySecondDTO) {
        this.categorySecondCode = categorySecondDTO.getCategorySecondCode();
        this.categorySecondName = categorySecondDTO.getCategorySecondName();
        this.categorySecondEnrollDate = categorySecondDTO.getCategorySecondEnrollDate();
        this.categorySecondUpdateDate = categorySecondDTO.getCategorySecondUpdateDate();
        this.categoryFirstCode = categorySecondDTO.getCategoryFirstCode();
    }

}
