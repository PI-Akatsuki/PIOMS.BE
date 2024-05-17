package com.akatsuki.pioms.categorySecond.aggregate;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
public class CategorySecondVO {
    private int categorySecondCode;
    private String categorySecondName;
    private String categorySecondEnrollDate;
    private String categorySecondUpdateDate;

    public CategorySecondVO(CategorySecond categorySecond) {
        this.categorySecondCode = categorySecondCode;
        this.categorySecondName = categorySecondName;
        this.categorySecondEnrollDate = categorySecondEnrollDate;
        this.categorySecondUpdateDate = categorySecondUpdateDate;
    }
}
