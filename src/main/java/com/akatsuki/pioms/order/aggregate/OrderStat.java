package com.akatsuki.pioms.order.aggregate;


import com.akatsuki.pioms.order.dto.OrderDTO;
import com.akatsuki.pioms.order.etc.ORDER_CONDITION;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

import static com.akatsuki.pioms.order.etc.ORDER_CONDITION.*;

@ToString
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class OrderStat {
    private int waitCnt;
    private int acceptCnt;
    private int denyCnt;
    private int deliveryCnt;
    private int inspectionWaitCnt;
    private int inspectionFinishCnt;

    public OrderStat(List<OrderDTO> orders) {
        if(orders == null) return;
        for (int i = 0; i < orders.size(); i++) {
            ORDER_CONDITION orderCondition = orders.get(i).getOrderCondition();
            if (orderCondition == 승인대기) waitCnt++;
            else if (orderCondition== 승인완료) acceptCnt++;
            else if(orderCondition == 승인거부) denyCnt++;
            else if(orderCondition== 검수대기) inspectionWaitCnt++;
            else inspectionFinishCnt++;

        }
    }
}
