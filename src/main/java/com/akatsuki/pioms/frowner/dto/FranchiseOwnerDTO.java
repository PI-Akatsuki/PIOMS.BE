package com.akatsuki.pioms.frowner.dto;

import com.akatsuki.pioms.frowner.aggregate.FranchiseOwner;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@ToString
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
    }

    // FranchiseOwnerDTO를 FranchiseOwner 엔티티로 변환
    public FranchiseOwner toEntity() {
        return FranchiseOwner.builder()
                .franchiseOwnerCode(this.franchiseOwnerCode)
                .franchiseOwnerId(this.franchiseOwnerId)
                .franchiseOwnerName(this.franchiseOwnerName)
                .franchiseOwnerPwd(this.franchiseOwnerPwd)
                .franchiseOwnerEmail(this.franchiseOwnerEmail)
                .franchiseOwnerPhone(this.franchiseOwnerPhone)
                .franchiseOwnerEnrollDate(this.franchiseOwnerEnrollDate)
                .franchiseOwnerUpdateDate(this.franchiseOwnerUpdateDate)
                .franchiseOwnerDeleteDate(this.franchiseOwnerDeleteDate)
                .build();
    }

}
