package com.akatsuki.pioms.order.aggregate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Map;


@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class RequestPutOrder {
    private int orderCode;
    private Map<Integer,Integer> products;
}
