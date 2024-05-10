package com.akatsuki.pioms.categorySecond.aggregate;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Setter
@Getter
public class RequestCategorySecondPost {
    private int categoryFirstCode;
    private String categorySecondName;

    public RequestCategorySecondPost(int categoryFirstCode, String categorySecondName) {
        this.categoryFirstCode = categoryFirstCode;
        this.categorySecondName = categorySecondName;
    }
}
