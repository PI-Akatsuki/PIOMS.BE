package com.akatsuki.pioms.frwarehouse.aggregate;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ResponseFranchiseWarehouseUpdate {
    private int franchiseWarehouseCode;
    private int franchiseWarehouseTotal;
    private int franchiseWarehouseCount;
    private int franchiseWarehouseEnable;

    public ResponseFranchiseWarehouseUpdate(int franchiseWarehouseCode, int franchiseWarehouseTotal, int franchiseWarehouseCount, int franchiseWarehouseEnable) {
        this.franchiseWarehouseCode = franchiseWarehouseCode;
        this.franchiseWarehouseTotal = franchiseWarehouseTotal;
        this.franchiseWarehouseCount = franchiseWarehouseCount;
        this.franchiseWarehouseEnable = franchiseWarehouseEnable;
    }
}
