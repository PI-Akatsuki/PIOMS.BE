package com.akatsuki.pioms.categorySecond.aggregate;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Setter
@Getter
public class ResponseCategorySecondUpdate {
    private int category_second_code;
    private String category_second_name;
    private String category_second_update_date;

    public ResponseCategorySecondUpdate(int category_second_code, String category_second_name, String category_second_update_date) {
        this.category_second_code = category_second_code;
        this.category_second_name = category_second_name;
        this.category_second_update_date = category_second_update_date;
    }
}
