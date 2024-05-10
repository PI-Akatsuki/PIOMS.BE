package com.akatsuki.pioms.categoryFirst.aggregate;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class ResponseCategoryFirstPost {

    private int categoryFirstCode;
    private String categoryFirstName;
    private String categoryFirstEnrollDate;

    public ResponseCategoryFirstPost(int categoryFirstCode, String categoryFirstName,String categoryFirstEnrollDate) {
        this.categoryFirstCode = categoryFirstCode;
        this.categoryFirstName = categoryFirstName;
        this.categoryFirstEnrollDate = categoryFirstEnrollDate;
    }



}
