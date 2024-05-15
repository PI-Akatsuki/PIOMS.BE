package com.akatsuki.pioms.admin.dto;

import com.akatsuki.pioms.admin.aggregate.Admin;
import com.akatsuki.pioms.franchise.dto.FranchiseDTO;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
@ToString
public class AdminDTO {
    private int adminCode;

    private String adminName;

    private String adminId;

    private String adminPwd;

    private String enrollDate;

    private String updateDate;

    private String deleteDate;

    private String adminEmail;

    private String adminPhone;

    private String accessNumber;

    private boolean adminStatus;

    private List<FranchiseDTO> franchiseList;

    public AdminDTO(Admin admin) {
        this.adminCode = admin.getAdminCode();
        this.adminName = admin.getAdminName();
        this.adminId = admin.getAdminId();
        this.adminPwd = admin.getAdminPwd();
        this.enrollDate = admin.getEnrollDate();
        this.updateDate = admin.getUpdateDate();
        this.deleteDate = admin.getDeleteDate();
        this.adminEmail = admin.getAdminEmail();
        this.adminPhone = admin.getAdminPhone();
        this.accessNumber = admin.getAccessNumber();
        this.adminStatus = admin.isAdminStatus();
        this.franchiseList = new ArrayList<>();
        admin.getFranchise().forEach(
                franchise -> {
                    franchiseList.add(new FranchiseDTO(franchise));
                }
        );
    }
}
