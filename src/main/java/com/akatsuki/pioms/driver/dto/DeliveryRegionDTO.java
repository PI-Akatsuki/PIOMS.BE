package com.akatsuki.pioms.driver.dto;

import com.akatsuki.pioms.driver.aggregate.DeliveryDriver;
import com.akatsuki.pioms.driver.aggregate.DeliveryRegion;
import com.akatsuki.pioms.franchise.aggregate.Franchise;
import com.akatsuki.pioms.franchise.dto.FranchiseDTO;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class DeliveryRegionDTO {
    private int deliveryRegionCode;

    private FranchiseDTO franchise;

    private int driverCode;
    private String driverName;

    public DeliveryRegionDTO(DeliveryRegion deliveryRegion) {
        this.deliveryRegionCode = deliveryRegion.getDeliveryRegionCode();
        this.franchise = new FranchiseDTO(deliveryRegion.getFranchise());
        this.driverCode = deliveryRegion.getDeliveryDriver().getDriverCode();
        this.driverName = deliveryRegion.getDeliveryDriver().getDriverName();
    }
}
