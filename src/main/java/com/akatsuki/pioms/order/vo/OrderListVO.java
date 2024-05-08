package com.akatsuki.pioms.order.vo;

import com.akatsuki.pioms.order.entity.OrderEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class OrderListVO {

    List<OrderVO> orderList;

}
