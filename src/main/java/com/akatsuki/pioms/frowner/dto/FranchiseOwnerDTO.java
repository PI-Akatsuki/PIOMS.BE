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
    private String franchiseRole;
    private boolean franchiseOwnerStatus;
    private int ownerPwdCheckCount;
    private String franchiseName;
    private String adminName;

    // FranchiseOwner 엔티티에서 FranchiseOwnerDTO로 변환
    public FranchiseOwnerDTO(FranchiseOwner franchiseOwner) {
        this.franchiseOwnerCode = franchiseOwner.getFranchiseOwnerCode();
        this.franchiseOwnerName = franchiseOwner.getFranchiseOwnerName();
        this.franchiseOwnerId = franchiseOwner.getFranchiseOwnerId();
        this.franchiseOwnerPwd = franchiseOwner.getFranchiseOwnerPwd();
        this.franchiseOwnerEmail = franchiseOwner.getFranchiseOwnerEmail();
        this.franchiseOwnerPhone = franchiseOwner.getFranchiseOwnerPhone();
        this.franchiseOwnerStatus = franchiseOwner.isFranchiseOwnerStatus();
        this.ownerPwdCheckCount = franchiseOwner.getOwnerPwdCheckCount();
        this.franchiseRole = franchiseOwner.getFranchiseRole();
        this.franchiseOwnerEnrollDate = franchiseOwner.getFranchiseOwnerEnrollDate();
        this.franchiseOwnerUpdateDate = franchiseOwner.getFranchiseOwnerUpdateDate();
        this.franchiseOwnerDeleteDate = franchiseOwner.getFranchiseOwnerDeleteDate();
        if (franchiseOwner.getFranchise() != null) {
            this.franchiseName = franchiseOwner.getFranchise().getFranchiseName();
            if (franchiseOwner.getFranchise().getAdmin() != null) {
                this.adminName = franchiseOwner.getFranchise().getAdmin().getAdminName();
            }
        }
    }

    // DTO를 Entity로 변환
    public FranchiseOwner toEntity() {
        return FranchiseOwner.builder()
                .franchiseOwnerCode(this.franchiseOwnerCode)
                .franchiseOwnerName(this.franchiseOwnerName)
                .franchiseOwnerId(this.franchiseOwnerId)
                .franchiseOwnerPwd(this.franchiseOwnerPwd)
                .franchiseOwnerEmail(this.franchiseOwnerEmail)
                .franchiseOwnerPhone(this.franchiseOwnerPhone)
                .franchiseRole(this.franchiseRole)
                .franchiseOwnerStatus(this.franchiseOwnerStatus)
                .ownerPwdCheckCount(this.ownerPwdCheckCount)
                .franchiseOwnerEnrollDate(this.franchiseOwnerEnrollDate)
                .franchiseOwnerUpdateDate(this.franchiseOwnerUpdateDate)
                .franchiseOwnerDeleteDate(this.franchiseOwnerDeleteDate)
                .build();
    }

    // 상태를 문자열로 반환
    public String getFranchiseStatus() {
        return this.franchiseOwnerStatus ? "활성화" : "비활성화";
    }
}
