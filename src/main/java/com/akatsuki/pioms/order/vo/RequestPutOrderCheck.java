package com.akatsuki.pioms.order.vo;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Map;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class RequestPutOrderCheck {
    private int orderCode;
    private Map<Integer,Integer> requestProduct;
}
