package com.akatsuki.pioms.order.aggregate;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.jpa.repository.Query;

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

}
