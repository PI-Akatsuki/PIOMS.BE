package com.akatsuki.pioms.admin.dto;

import com.akatsuki.pioms.admin.aggregate.Admin;
import com.akatsuki.pioms.franchise.aggregate.Franchise;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

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
    private String enrollDate;
    private String updateDate;
    private String deleteDate;
    private List<String> franchiseNames;
    private int franchiseCount;
    private String adminStatusString;

    // Admin 엔티티에서 AdminDTO로 변환하는 생성자
    public AdminDTO(Admin admin) {
        this.adminCode = admin.getAdminCode();
        this.adminName = admin.getAdminName();
        this.adminId = admin.getAdminId();
        this.adminPwd = admin.getAdminPwd();
        this.adminEmail = admin.getAdminEmail();
        this.adminPhone = admin.getAdminPhone();
        this.accessNumber = admin.getAccessNumber();
        this.adminRole = admin.getAdminRole();
        this.adminStatus = admin.isAdminStatus();
        this.pwdCheckCount = admin.getPwdCheckCount();
        this.enrollDate = admin.getEnrollDate();
        this.updateDate = admin.getUpdateDate();
        this.deleteDate = admin.getDeleteDate();
        this.franchiseNames = admin.getFranchise() != null ?
                admin.getFranchise().stream()
                        .map(Franchise::getFranchiseName)
                        .collect(Collectors.toList())
                : null;
        this.franchiseCount = admin.getFranchiseCount();
        this.adminStatusString = admin.isAdminStatus() ? "활성화" : "비활성화";
    }
}
