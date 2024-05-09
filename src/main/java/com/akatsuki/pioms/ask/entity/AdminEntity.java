package com.akatsuki.pioms.ask.entity;

import com.akatsuki.pioms.ask.etc.ADMIN_ROLE;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "admin")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class AdminEntity {
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
    private LocalDateTime adminEnrollDate;

    @Column(name = "admin_update_date")
    private Date adminUpdateDate;

    @Column(name = "admin_delete_date")
    private Date adminDeleteDate;

    @Column(name = "admin_email")
    private String adminEmail;

    @Column(name = "admin_phone")
    private String adminPhone;

    @Column(name = "admin_access_number")
    private UUID adminAccessNumber;

    @Column(name = "admin_role")
    private boolean adminRole;

}
