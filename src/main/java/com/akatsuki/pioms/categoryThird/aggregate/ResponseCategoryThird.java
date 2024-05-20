package com.akatsuki.pioms.categoryThird.aggregate;

import com.akatsuki.pioms.categoryThird.dto.CategoryThirdDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ResponseCategoryThird {
    private int categoryThirdCode;
    private String categoryThirdName;
    private String categoryThirdEnrollDate;
    private String categoryThirdUpdateDate;
    private int categorySecondCode;

    public ResponseCategoryThird(CategoryThird categoryThird) {
        this.categoryThirdCode = categoryThird.getCategoryThirdCode();
        this.categoryThirdName = categoryThird.getCategoryThirdName();
        this.categoryThirdEnrollDate = categoryThird.getCategoryThirdEnrollDate();
        this.categoryThirdUpdateDate = categoryThird.getCategoryThirdUpdateDate();
        this.categorySecondCode = categoryThird.getCategorySecond().getCategorySecondCode();
    }

    public ResponseCategoryThird(CategoryThirdDTO categoryThirdDTO) {
        this.categoryThirdCode = categoryThirdDTO.getCategoryThirdCode();
        this.categoryThirdName = categoryThirdDTO.getCategoryThirdName();
        this.categoryThirdEnrollDate = categoryThirdDTO.getCategoryThirdEnrollDate();
        this.categoryThirdUpdateDate = categoryThirdDTO.getCategoryThirdUpdateDate();
        this.categorySecondCode = categoryThirdDTO.getCategorySecondCode();
    }
}
