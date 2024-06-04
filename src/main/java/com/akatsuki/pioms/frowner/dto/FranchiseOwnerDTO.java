package com.akatsuki.pioms.frowner.dto;

import com.akatsuki.pioms.franchise.aggregate.Franchise;
import com.akatsuki.pioms.frowner.aggregate.FranchiseOwner;
import lombok.*;
import com.fasterxml.jackson.annotation.JsonIgnore;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class
FranchiseOwnerDTO {
    private int franchiseOwnerCode;
    private String franchiseOwnerName;
    private String franchiseOwnerId;
    private String franchiseOwnerPwd;
    private String franchiseOwnerEmail;
    private String franchiseOwnerPhone;
    private String franchiseOwnerEnrollDate;
    private String franchiseOwnerUpdateDate;
    private String franchiseOwnerDeleteDate;
//    @JsonIgnore
    private String franchiseName;

    // FranchiseOwner 엔티티에서 FranchiseOwnerDTO로 변환하는 메서드
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
        if (franchiseOwner.getFranchise()!=null)
            this.franchiseName = franchiseOwner.getFranchise().getFranchiseName();
    }

}
