package com.akatsuki.pioms.admin.aggregate;

import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
@Setter
@ToString
@Entity
@Table(name = "admin")
public class Admin {
    @Id
    @Column(name = "admin_code")
    private int adminCode;

    @Column(name = "admin_name")
    private String adminName;

    @Column(name = "admin_id")
    private String adminId;

    @Column(name = "admin_pwd")
    private String adminPwd;

    @Column(name = "admin_enroll_date")
    private String enrollDate;

    @Column(name = "admin_update_date")
    private String updateDate;

    @Column(name = "admin_delete_date")
    private String deleteDate;

    @Column(name = "admin_email")
    private String adminEmail;

    @Column(name = "admin_phone")
    private String adminPhone;

    @Column(name = "admin_access_number")
    private String accessNumber;

    @Column(name = "admin_role")
    private boolean adminRole;
}
