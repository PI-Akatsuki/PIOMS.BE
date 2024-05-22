package com.akatsuki.pioms.admin.dto;

import com.akatsuki.pioms.franchise.dto.FranchiseDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdminDTO {
    private int adminCode;
    private String adminName;
    private String adminId;
    private String adminPwd;
    private String adminEmail;
    private String adminPhone;
    private String accessNumber;
    private String adminRole;
    private boolean adminStatus;
    private int pwdCheckCount;
    private boolean adminDormancy;
    private String enrollDate;
    private String updateDate;
    private String deleteDate;
    private List<FranchiseDTO> franchiseList;
}
