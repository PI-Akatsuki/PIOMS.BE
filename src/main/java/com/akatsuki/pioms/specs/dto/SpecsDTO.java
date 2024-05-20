package com.akatsuki.pioms.specs.dto;

import com.akatsuki.pioms.order.dto.OrderDTO;
import com.akatsuki.pioms.specs.aggregate.Specs;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SpecsDTO {
    private int specsCode;
    private LocalDateTime specsDate;
    private OrderDTO order;

    public SpecsDTO(Specs specs) {
        this.specsCode= specs.getSpecsCode();
        this.specsDate= specs.getSpecsDate();
        this.order= new OrderDTO(specs.getOrder());
    }
}
