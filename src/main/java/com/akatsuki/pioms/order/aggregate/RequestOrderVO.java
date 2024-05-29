package com.akatsuki.pioms.order.aggregate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;
import java.util.Map;


@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class RequestOrderVO {
//    private List<>
    private Map<Integer,Integer> products;
    private int franchiseCode;
    private int orderTotalPrice;
}
