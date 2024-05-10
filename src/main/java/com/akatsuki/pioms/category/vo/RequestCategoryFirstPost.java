package com.akatsuki.pioms.category.vo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Setter
@Getter
public class RequestCategoryFirstPost {
    private String category_first_name;

    public RequestCategoryFirstPost(String category_first_name) {
        this.category_first_name = category_first_name;
    }

}
