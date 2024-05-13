package com.akatsuki.pioms.admin.aggregate;

import com.akatsuki.pioms.admin.dto.AdminDTO;
import com.akatsuki.pioms.franchise.aggregate.Franchise;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
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

    @Column(name = "admin_status")
    private boolean adminStatus;

    @OneToMany(mappedBy = "admin")
    private List<Franchise> franchise;

    public Admin(AdminDTO admin) {
        this.adminCode= admin.getAdminCode();
        this.adminName= admin.getAdminName();
        this.adminId= admin.getAdminId();
        this.adminPwd= admin.getAdminPwd();
        this.enrollDate= admin.getEnrollDate();
        this.updateDate= admin.getUpdateDate();
        this.deleteDate= admin.getDeleteDate();
        this.adminEmail= admin.getAdminEmail();
        this.adminPhone= admin.getAdminPhone();
        this.accessNumber= admin.getAccessNumber();
        this.adminStatus= admin.isAdminStatus();
        this.franchise= new ArrayList<>();
        for (int i = 0; i < admin.getFranchiseList().size(); i++) {
            franchise.add(new Franchise(admin.getFranchiseList().get(i) ) );
        }
    }
}
