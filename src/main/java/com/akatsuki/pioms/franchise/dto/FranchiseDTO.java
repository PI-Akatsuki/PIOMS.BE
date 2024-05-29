package com.akatsuki.pioms.franchise.dto;

import com.akatsuki.pioms.admin.aggregate.Admin;
import com.akatsuki.pioms.admin.dto.AdminDTO;
import com.akatsuki.pioms.driver.aggregate.DeliveryDriver;
import com.akatsuki.pioms.franchise.aggregate.DELIVERY_DATE;
import com.akatsuki.pioms.franchise.aggregate.Franchise;
import com.akatsuki.pioms.frowner.aggregate.FranchiseOwner;
import com.akatsuki.pioms.frowner.dto.FranchiseOwnerDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor
@AllArgsConstructor
public class FranchiseDTO {

    private int franchiseCode;
    private String franchiseName;
    private String franchiseAddress;
    private String franchiseCall;
    private String franchiseEnrollDate;
    private String franchiseUpdateDate;
    private String franchiseDeleteDate;
    private String franchiseBusinessNum;
    private DELIVERY_DATE franchiseDeliveryDate;
    private FranchiseOwnerDTO franchiseOwner;
    private DeliveryDriver deliveryDriver;

    private int adminCode;
    private String adminName;


    public FranchiseDTO(Franchise franchise) {
        this.franchiseCode = franchise.getFranchiseCode();
        this.franchiseName = franchise.getFranchiseName();
        this.franchiseAddress = franchise.getFranchiseAddress();
        this.franchiseCall = franchise.getFranchiseCall();
        this.franchiseEnrollDate = franchise.getFranchiseEnrollDate();
        this.franchiseUpdateDate = franchise.getFranchiseUpdateDate();
        this.franchiseDeleteDate = franchise.getFranchiseDeleteDate();
        this.franchiseBusinessNum = franchise.getFranchiseBusinessNum();
        this.franchiseDeliveryDate = franchise.getFranchiseDeliveryDate();
        this.franchiseOwner = new FranchiseOwnerDTO(franchise.getFranchiseOwner(), franchise);
        this.adminCode = franchise.getAdmin().getAdminCode();
        this.adminName = franchise.getAdmin().getAdminName();
        this.deliveryDriver = franchise.getDeliveryDriver();
    }
}
