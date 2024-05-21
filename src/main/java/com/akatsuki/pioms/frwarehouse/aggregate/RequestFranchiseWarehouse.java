package com.akatsuki.pioms.frwarehouse.aggregate;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
public class RequestFranchiseWarehouse {
    private int franchiseWarehouseCode;
    private int franchiseWarehouseTotal;
    private int franchiseWarehouseCount;
    private int franchiseWarehouseEnable;
    private boolean franchiseWarehouseFavorite;
    private int franchiseCode;
    private int productCode;

}
