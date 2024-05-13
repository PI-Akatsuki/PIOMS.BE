package com.akatsuki.pioms.categoryThird.aggregate;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class RequestCategoryThirdPost {
    private int categorySecondCode;
    private String categoryThirdName;

    public RequestCategoryThirdPost(int categorySecondCode, String categoryThirdName) {
        this.categorySecondCode = categorySecondCode;
        this.categoryThirdName = categoryThirdName;
    }
}
