package com.akatsuki.pioms.frowner.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
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
}
