package com.akatsuki.pioms.categoryThird.dto;

import com.akatsuki.pioms.categoryThird.aggregate.CategoryThird;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CategoryThirdDTO {
    private int categoryThirdCode;
    private String categoryThirdName;
    private String categoryThirdEnrollDate;
    private String categoryThirdUpdateDate;
    private int categorySecondCode;

    public CategoryThirdDTO(CategoryThird categoryThird) {
        System.out.println("categoryThird = " + categoryThird);
        this.categoryThirdCode = categoryThirdCode;
        this.categoryThirdName = categoryThirdName;
        this.categoryThirdEnrollDate = categoryThirdEnrollDate;
        this.categoryThirdUpdateDate = categoryThirdUpdateDate;
        this.categorySecondCode = categorySecondCode;
    }
}
