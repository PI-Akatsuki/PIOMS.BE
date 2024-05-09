package com.akatsuki.pioms.admin.dto;

import lombok.*;

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

    private boolean adminRole;
}
