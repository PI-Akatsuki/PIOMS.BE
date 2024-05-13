package com.akatsuki.pioms.specs.dto;

import com.akatsuki.pioms.franchise.aggregate.Franchise;
import com.akatsuki.pioms.franchise.dto.FranchiseDTO;
import com.akatsuki.pioms.order.aggregate.Order;
import com.akatsuki.pioms.order.dto.OrderDTO;
import com.akatsuki.pioms.specs.aggregate.SpecsEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SpecsDTO {
    private int specsCode;
    private LocalDateTime specsDate;
    private FranchiseDTO franchise;
    private OrderDTO order;

    public SpecsDTO(SpecsEntity specs) {
        this.specsCode= specs.getSpecsCode();
        this.specsDate= specs.getSpecsDate();
        this.franchise= new FranchiseDTO(specs.getFranchise());
        this.order= new OrderDTO(specs.getOrder());
    }
}
