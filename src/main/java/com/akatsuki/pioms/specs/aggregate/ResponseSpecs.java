package com.akatsuki.pioms.specs.aggregate;

import com.akatsuki.pioms.specs.dto.SpecsDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;


@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ResponseSpecs {
    private int specsCode;
    private LocalDateTime specsDate;
    private int franchiseCode;
    private String franchiseName;

    private int orderCode;
    private Map<String, Integer> products;

    public ResponseSpecs(SpecsDTO specs) {
        this.specsCode = specs.getSpecsCode();
        this.specsDate = specs.getSpecsDate();
        this.franchiseCode = specs.getOrder().getFranchiseCode();
        this.franchiseName = specs.getOrder().getFranchiseName();
        this.orderCode = specs.getOrder().getOrderCode();
        products = new HashMap<>();
        specs.getOrder().getOrderProductList().forEach(product->{
            products.put( product.getProductName(),product.getRequestProductCount());
        });

    }
}
