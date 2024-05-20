package com.akatsuki.pioms.frwarehouse.aggregate;

import com.akatsuki.pioms.frwarehouse.dto.FranchiseWarehouseDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class ResponseFranchiseWarehouse {
    private int franchiseWarehouseCode;
    private int franchiseWarehouseTotal;
    private int franchiseWarehouseCount;
    private int franchiseWarehouseEnable;
    private boolean franchiseWarehouseFavorite;
    private int franchiseCode;
    private int productCode;

    public ResponseFranchiseWarehouse(FranchiseWarehouse franchiseWarehouse) {
        this.franchiseWarehouseCode = franchiseWarehouse.getFranchiseWarehouseCode();
        this.franchiseWarehouseTotal = franchiseWarehouse.getFranchiseWarehouseTotal();
        this.franchiseWarehouseCount = franchiseWarehouse.getFranchiseWarehouseCount();
        this.franchiseWarehouseEnable = franchiseWarehouse.getFranchiseWarehouseEnable();
        this.franchiseWarehouseFavorite = franchiseWarehouse.isFranchiseWarehouseFavorite();
        this.franchiseCode = franchiseWarehouse.getFranchiseCode();
        this.productCode = franchiseWarehouse.getFranchiseCode();
    }

    public ResponseFranchiseWarehouse(FranchiseWarehouseDTO franchiseWarehouseDTO) {
        this.franchiseWarehouseCode = franchiseWarehouseDTO.getFranchiseWarehouseCode();
        this.franchiseWarehouseTotal = franchiseWarehouseDTO.getFranchiseWarehouseTotal();
        this.franchiseWarehouseCount = franchiseWarehouseDTO.getFranchiseWarehouseCount();
        this.franchiseWarehouseEnable = franchiseWarehouseDTO.getFranchiseWarehouseEnable();
        this.franchiseWarehouseFavorite = franchiseWarehouseDTO.isFranchiseWarehouseFavorite();
        this.franchiseCode = franchiseWarehouseDTO.getFranchiseCode();
        this.productCode = franchiseWarehouseDTO.getFranchiseCode();
    }
}
