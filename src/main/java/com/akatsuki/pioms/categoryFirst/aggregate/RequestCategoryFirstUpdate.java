package com.akatsuki.pioms.categoryFirst.aggregate;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Setter
@Getter
public class RequestCategoryFirstUpdate {
    private int categoryFirstCode;
    private String categoryFirstName;

    public RequestCategoryFirstUpdate(int categoryFirstCode, String categoryFirstName) {
        this.categoryFirstCode = categoryFirstCode;
        this.categoryFirstName = categoryFirstName;
    }
}
