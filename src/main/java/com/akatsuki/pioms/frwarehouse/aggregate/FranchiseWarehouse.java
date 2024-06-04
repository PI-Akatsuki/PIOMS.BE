package com.akatsuki.pioms.frwarehouse.aggregate;

import com.akatsuki.pioms.product.aggregate.Product;
import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "franchise_warehouse")
@ToString
public class FranchiseWarehouse {
    @Id
    @Column(name = "franchise_warehouse_code")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    @ManyToOne
    @JoinColumn(name = "product_code", referencedColumnName = "product_code")
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

    // 상품 이름 가져오기
    public String getProductName() {
        return this.product.getProductName();
    }
}
