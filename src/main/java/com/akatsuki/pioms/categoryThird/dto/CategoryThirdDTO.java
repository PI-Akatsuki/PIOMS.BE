package com.akatsuki.pioms.categoryThird.dto;

import com.akatsuki.pioms.categoryThird.aggregate.CategoryThird;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CategoryThirdDTO {
    private int categoryThirdCode;
    private String categoryThirdName;
    private String categoryThirdEnrollDate;
    private String categoryThirdUpdateDate;
    private int categorySecondCode;

    public CategoryThirdDTO(CategoryThird categoryThird) {
        System.out.println("categoryThird = " + categoryThird);
        this.categoryThirdCode = categoryThird.getCategoryThirdCode();
        this.categoryThirdName = categoryThird.getCategoryThirdName();
        this.categoryThirdEnrollDate = categoryThird.getCategoryThirdEnrollDate();
        this.categoryThirdUpdateDate = categoryThird.getCategoryThirdUpdateDate();
        this.categorySecondCode = categoryThird.getCategorySecond().getCategorySecondCode();
    }
}
