package com.akatsuki.pioms.categoryThird.aggregate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class RequestCategoryThird {
    private int categorySecondCode;
    private String categoryThirdName;
}
