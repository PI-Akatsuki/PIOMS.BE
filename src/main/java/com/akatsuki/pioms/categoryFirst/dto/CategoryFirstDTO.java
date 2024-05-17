package com.akatsuki.pioms.categoryFirst.dto;

import com.akatsuki.pioms.categoryFirst.aggregate.CategoryFirst;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryFirstDTO {
    private int categoryFirstCode;
    private String categoryFirstName;
    private String categoryFirstEnrollDate;
    private String categoryFirstUpdateDate;

    public CategoryFirstDTO(CategoryFirst categoryFirst) {
        System.out.println("categoryFirst = " + categoryFirst);
        this.categoryFirstCode = categoryFirstCode;
        this.categoryFirstName = categoryFirstName;
        this.categoryFirstEnrollDate = categoryFirstEnrollDate;
        this.categoryFirstUpdateDate = categoryFirstUpdateDate;
    }

}
