package com.akatsuki.pioms.frwarehouse.aggregate;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class RequestFranchiseWarehouseUpdate {
    private int franchiseWarehouseTotal;
    private int franchiseWarehouseCount;
    private int franchiseWarehouseEnable;

    public RequestFranchiseWarehouseUpdate(int franchiseWarehouseTotal, int franchiseWarehouseCount, int franchiseWarehouseEnable) {
        this.franchiseWarehouseTotal = franchiseWarehouseTotal;
        this.franchiseWarehouseCount = franchiseWarehouseCount;
        this.franchiseWarehouseEnable = franchiseWarehouseEnable;
    }
}
