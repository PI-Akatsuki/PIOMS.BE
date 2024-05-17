package com.akatsuki.pioms.categorySecond.aggregate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CategorySecondListVO {
    List<CategorySecondVO> categoryList;
}
