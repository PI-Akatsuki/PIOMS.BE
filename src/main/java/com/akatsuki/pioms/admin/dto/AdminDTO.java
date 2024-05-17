package com.akatsuki.pioms.admin.dto;

import com.akatsuki.pioms.franchise.dto.FranchiseDTO;
import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
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

    private String adminRole;

    private boolean adminStatus;

    private List<FranchiseDTO> franchiseList;
}
