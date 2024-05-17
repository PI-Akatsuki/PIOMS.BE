package com.akatsuki.pioms.categoryFirst.aggregate;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class ResponseCategoryFirstUpdate {
    private int categoryFirstCode;
    private String categoryFirstName;
    private String categoryFirstUpdateDate;

    public ResponseCategoryFirstUpdate(int categoryFirstCode, String categoryFirstName, String categoryFirstUpdateDate) {
        this.categoryFirstCode = categoryFirstCode;
        this.categoryFirstName = categoryFirstName;
        this.categoryFirstUpdateDate = categoryFirstUpdateDate;
    }
}
