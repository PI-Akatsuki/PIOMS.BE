package com.akatsuki.pioms.category.vo;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class ResponseCategoryFirstUpdate {
    private int category_first_code;
    private String category_first_name;
    private String category_first_update_date;

    public ResponseCategoryFirstUpdate(int category_first_code, String category_first_name, String category_first_update_date) {
        this.category_first_code = category_first_code;
        this.category_first_name = category_first_name;
        this.category_first_update_date = category_first_update_date;
    }
}
