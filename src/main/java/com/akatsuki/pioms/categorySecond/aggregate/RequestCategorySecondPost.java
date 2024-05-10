package com.akatsuki.pioms.categorySecond.aggregate;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Setter
@Getter
public class RequestCategorySecondPost {
    private int category_first_code;
    private String category_second_name;

    public RequestCategorySecondPost(int category_first_code, String category_second_name) {
        this.category_first_code = category_first_code;
        this.category_second_name = category_second_name;
    }
}
