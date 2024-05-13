package com.akatsuki.pioms.frowner.dto;


import com.akatsuki.pioms.franchise.dto.FranchiseDTO;
import com.akatsuki.pioms.frowner.aggregate.FranchiseOwner;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class FranchiseOwnerDTO {
    private int franchiseOwnerCode;
    private String franchiseOwnerName;
    private String franchiseOwnerId;
    private String franchiseOwnerPwd;
    private String franchiseOwnerEmail;
    private String franchiseOwnerPhone;
    private LocalDateTime franchiseOwnerEnrollDate;
    private LocalDateTime franchiseOwnerUpdateDate;
    private LocalDateTime franchiseOwnerDeleteDate;

    public FranchiseOwnerDTO(FranchiseOwner franchiseOwner) {
        this.franchiseOwnerCode = franchiseOwner.getFranchiseOwnerCode();
        this.franchiseOwnerName = franchiseOwner.getFranchiseOwnerName();
        this.franchiseOwnerId = franchiseOwner.getFranchiseOwnerId();
        this.franchiseOwnerPwd = franchiseOwner.getFranchiseOwnerPwd();
        this.franchiseOwnerEmail = franchiseOwner.getFranchiseOwnerEmail();
        this.franchiseOwnerPhone = franchiseOwner.getFranchiseOwnerPhone();
        this.franchiseOwnerEnrollDate = franchiseOwner.getFranchiseOwnerEnrollDate();
        this.franchiseOwnerUpdateDate = franchiseOwner.getFranchiseOwnerUpdateDate();
        this.franchiseOwnerDeleteDate = franchiseOwner.getFranchiseOwnerDeleteDate();
    }
}
