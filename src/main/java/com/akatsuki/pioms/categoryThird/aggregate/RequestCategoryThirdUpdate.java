package com.akatsuki.pioms.categoryThird.aggregate;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class RequestCategoryThirdUpdate {
    private int categoryThirdCode;
    private String categoryThirdName;

    public RequestCategoryThirdUpdate(int categoryThirdCode, String categoryThirdName) {
        this.categoryThirdCode = categoryThirdCode;
        this.categoryThirdName = categoryThirdName;
    }
}
