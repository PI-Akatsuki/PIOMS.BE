package com.akatsuki.pioms.order.vo;

import lombok.Getter;

import java.util.Map;


@Getter
public class RequestPutOrder {
    private int orderCode;
    private Map<Integer,Integer> products;
    private int franchiseCode;
}
