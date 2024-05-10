package com.akatsuki.pioms.categoryFirst.aggregate;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Setter
@Getter
public class RequestCategoryFirstPost {
    private String categoryFirstName;

    public RequestCategoryFirstPost(String categoryFirstName) {
        this.categoryFirstName = categoryFirstName;
    }

}
