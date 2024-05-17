package com.akatsuki.pioms.categoryFirst.aggregate;

import com.akatsuki.pioms.categoryFirst.dto.CategoryFirstDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ResponseCategoryFirst {
    private int categoryFirstCode;
    private String categoryFirstName;
    private String categoryFirstEnrollDate;
    private String categoryFirstUpdateDate;

    public ResponseCategoryFirst(CategoryFirst categoryFirst) {
        this.categoryFirstCode = categoryFirst.getCategoryFirstCode();
        this.categoryFirstName = categoryFirst.getCategoryFirstName();
        this.categoryFirstEnrollDate = categoryFirst.getCategoryFirstEnrollDate();
        this.categoryFirstUpdateDate = categoryFirst.getCategoryFirstUpdateDate();
    }

    public ResponseCategoryFirst(CategoryFirstDTO categoryFirstDTO) {
        this.categoryFirstCode = categoryFirstDTO.getCategoryFirstCode();
        this.categoryFirstName = categoryFirstDTO.getCategoryFirstName();
        this.categoryFirstEnrollDate = categoryFirstDTO.getCategoryFirstEnrollDate();
        this.categoryFirstUpdateDate = categoryFirstDTO.getCategoryFirstUpdateDate();
    }
}
