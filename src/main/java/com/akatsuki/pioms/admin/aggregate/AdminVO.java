package com.akatsuki.pioms.admin.aggregate;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
@ToString
public class AdminVO {

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
