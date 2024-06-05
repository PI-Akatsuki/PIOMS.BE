package com.akatsuki.pioms.frwarehouse.dto;

import com.akatsuki.pioms.frwarehouse.aggregate.FranchiseWarehouse;
import com.akatsuki.pioms.product.aggregate.Product;
import com.akatsuki.pioms.product.dto.ProductDTO;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.*;


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class FranchiseWarehouseDTO {
    private int franchiseWarehouseCode;
    private int franchiseWarehouseTotal;
    private int franchiseWarehouseCount;
    private int franchiseWarehouseEnable;
    private boolean franchiseWarehouseFavorite;
    private int franchiseCode;
    private ProductDTO product;

    public FranchiseWarehouseDTO(FranchiseWarehouse franchiseWarehouse) {
        this.franchiseWarehouseCode = franchiseWarehouse.getFranchiseWarehouseCode();
        this.franchiseWarehouseTotal = franchiseWarehouse.getFranchiseWarehouseTotal();
        this.franchiseWarehouseCount = franchiseWarehouse.getFranchiseWarehouseCount();
        this.franchiseWarehouseEnable = franchiseWarehouse.getFranchiseWarehouseEnable();
        this.franchiseWarehouseFavorite = franchiseWarehouse.isFranchiseWarehouseFavorite();
        this.franchiseCode = franchiseWarehouse.getFranchiseCode();
        this.product = new ProductDTO(franchiseWarehouse.getProduct());
    }

}
