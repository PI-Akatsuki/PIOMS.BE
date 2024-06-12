package com.akatsuki.pioms.admin.aggregate;

import com.akatsuki.pioms.franchise.aggregate.Franchise;
import jakarta.persistence.*;
import lombok.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Formula;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@ToString
@Entity
@Table(name = "admin")
public class Admin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "admin_code")
    private int adminCode;

    @Column(name = "admin_name")
    private String adminName;

    @Column(name = "admin_id")
    private String adminId;

    @Column(name = "admin_pwd")
    private String adminPwd;

    @Column(name = "admin_email")
    private String adminEmail;

    @Column(name = "admin_phone")
    private String adminPhone;

    @Column(name = "admin_access_number")
    private String accessNumber;

    @Column(name = "admin_role")
    private String adminRole;

    @Column(name = "admin_status")
    private boolean adminStatus;

    @Column(name = "admin_pwd_check")
    private int pwdCheckCount;

    @Column(name = "admin_enroll_date")
    private String enrollDate;

    @Column(name = "admin_update_date")
    private String updateDate;

    @Column(name = "admin_delete_date")
    private String deleteDate;

    @OneToMany(mappedBy = "admin", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Franchise> franchise;

    @Formula("(select count(f.franchise_code) from franchise f where f.admin_code = admin_code)")
    private int franchiseCount;
}
