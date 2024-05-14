package com.akatsuki.pioms.specs.dto;

import com.akatsuki.pioms.franchise.aggregate.Franchise;
import com.akatsuki.pioms.franchise.dto.FranchiseDTO;
import com.akatsuki.pioms.order.aggregate.Order;
import com.akatsuki.pioms.order.dto.OrderDTO;
import com.akatsuki.pioms.specs.aggregate.SpecsEntity;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class SpecsDTO {
    private int specsCode;
    private LocalDateTime specsDate;
    private OrderDTO order;

    public SpecsDTO(SpecsEntity specs) {
        this.specsCode= specs.getSpecsCode();
        this.specsDate= specs.getSpecsDate();
        this.order= new OrderDTO(specs.getOrder());
    }
}
