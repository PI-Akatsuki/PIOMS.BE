package com.akatsuki.pioms.categoryThird.aggregate;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class RequestCategoryThird {
    private int categorySecondCode;
    private int categoryThirdCode;
    private String categoryThirdName;
}
