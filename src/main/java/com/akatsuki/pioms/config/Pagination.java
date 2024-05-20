package com.akatsuki.pioms.config;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class Pagination {

//    public static <T> List<T> splitPage(List<T> inputList, int page, int pageSize) {
//        int fromIndex = Math.min(page * pageSize, inputList.size());
//        int toIndex = Math.min(fromIndex + pageSize, inputList.size());
//        return inputList.subList(fromIndex, toIndex);
//    }

    public static <T> List<T> splitPage(List<T> inputList, int page, int pageSize) {
        // 입력 리스트가 null이면 예외를 던짐
        if (inputList == null ||pageSize <= 0) {
            return null;
        }

        int s = page * pageSize;
        int e = Math.min(s + pageSize, inputList.size());

        // 페이지가 범위를 벗어나면 빈 리스트 반환
        if (s >= inputList.size()) {
            return Collections.emptyList();
        }

        // sublist로 페이지 자르기
        // overflow 문제 해결하기 위해 새로운 리스트로 반환
        return new ArrayList<>(inputList.subList(s, e));
    }


}
