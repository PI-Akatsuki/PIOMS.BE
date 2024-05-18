package com.akatsuki.pioms.config;

import java.util.List;
import java.util.Optional;

public class Pagination {

    public static <T> List<T> splitPage(List<T> inputList, int page, int pageSize) {
        int fromIndex = Math.min(page * pageSize, inputList.size());
        int toIndex = Math.min(fromIndex + pageSize, inputList.size());
        return inputList.subList(fromIndex, toIndex);
    }

}
