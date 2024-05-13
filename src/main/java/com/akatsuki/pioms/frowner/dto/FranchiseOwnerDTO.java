package com.akatsuki.pioms.frowner.dto;

import lombok.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
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
    private String adminName;
    private String franchiseName;
}
