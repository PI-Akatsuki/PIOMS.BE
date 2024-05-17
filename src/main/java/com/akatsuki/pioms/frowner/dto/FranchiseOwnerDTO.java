package com.akatsuki.pioms.frowner.dto;



import com.akatsuki.pioms.franchise.dto.FranchiseDTO;
import com.akatsuki.pioms.frowner.aggregate.FranchiseOwner;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Builder;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class FranchiseOwnerDTO {
    private int franchiseOwnerCode;
    private String franchiseOwnerName;
    private String franchiseOwnerId;
    private String franchiseOwnerPwd;
    private String franchiseOwnerEmail;
    private String franchiseOwnerPhone;
    private String franchiseOwnerEnrollDate;
    private String franchiseOwnerUpdateDate;
    private String franchiseOwnerDeleteDate;
    private String franchiseName;
    private String adminName;
  
    public FranchiseOwnerDTO(FranchiseOwner franchiseOwner) {
        this.franchiseOwnerCode = franchiseOwner.getFranchiseOwnerCode();
        this.franchiseOwnerName = franchiseOwner.getFranchiseOwnerName();
        this.franchiseOwnerId = franchiseOwner.getFranchiseOwnerId();
        this.franchiseOwnerPwd = franchiseOwner.getFranchiseOwnerPwd();
        this.franchiseOwnerEmail = franchiseOwner.getFranchiseOwnerEmail();
        this.franchiseOwnerPhone = franchiseOwner.getFranchiseOwnerPhone();
//         this.franchiseOwnerEnrollDate = franchiseOwner.getFranchiseOwnerEnrollDate();
//         this.franchiseOwnerUpdateDate = franchiseOwner.getFranchiseOwnerUpdateDate();
//         this.franchiseOwnerDeleteDate = franchiseOwner.getFranchiseOwnerDeleteDate();
    }
}
