package com.akatsuki.pioms.categoryThird.aggregate;

import com.akatsuki.pioms.categoryThird.dto.CategoryThirdDTO;
import lombok.Data;

@Data
public class ResponseCategoryThirdPost {

    private int category_third_code;
    private String category_third_name;
    private String category_third_enroll_date;

    public ResponseCategoryThirdPost(int category_third_code, String category_third_name,String category_third_enroll_date) {
        this.category_third_code = category_third_code;
        this.category_third_name = category_third_name;
        this.category_third_enroll_date = category_third_enroll_date;
    }

    public ResponseCategoryThirdPost(CategoryThirdDTO categoryThirdDTO) {

    }
}
