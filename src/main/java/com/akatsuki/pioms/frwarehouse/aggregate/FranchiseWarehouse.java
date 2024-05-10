package com.akatsuki.pioms.frwarehouse.aggregate;


import com.akatsuki.pioms.product.entity.Product;
import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "franchise_warehouse")
@ToString()
public class FranchiseWarehouse {
    @Id
    @Column(name = "franchise_warehouse_code")
    private int franchiseWarehouseCode;
    @Column(name = "franchise_warehouse_total")
    private int franchiseWarehouseTotal;
    @Column(name = "franchise_warehouse_count")
    private int franchiseWarehouseCount;
    @Column(name = "franchise_warehouse_enable")
    private int franchiseWarehouseEnable;
    @Column(name = "franchise_warehouse_favorite")
    private boolean franchiseWarehouseFavorite;
    @Column(name = "franchise_code")
    private int franchiseCode;
    @JoinColumn(name = "product_code")
    @OneToOne
    @ToString.Exclude
    private Product product;

    public FranchiseWarehouse(boolean franchiseWarehouseFavorite, int franchiseCode, int productCode) {
        this.franchiseWarehouseTotal = 0;
        this.franchiseWarehouseCount = 0;
        this.franchiseWarehouseEnable = 0;
        this.franchiseWarehouseFavorite = franchiseWarehouseFavorite;
        this.franchiseCode = franchiseCode;
        Product product = new Product();
        product.setProductCode(productCode);
        this.product = product;
    }
}
