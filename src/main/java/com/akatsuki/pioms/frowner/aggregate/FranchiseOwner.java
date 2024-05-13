package com.akatsuki.pioms.frowner.aggregate;


import com.akatsuki.pioms.frowner.dto.FranchiseOwnerDTO;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.time.LocalDateTime;


@Entity
@Table(name = "franchise_owner")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class FranchiseOwner {
    @Id
    @Column(name = "franchise_owner_code")
    private int franchiseOwnerCode;

    @Column(name = "franchise_owner_name")
    private String franchiseOwnerName;

    @Column(name = "franchise_owner_id")
    private String franchiseOwnerId;

    @Column(name = "franchise_owner_pwd")
    private String franchiseOwnerPwd;

    @Column(name = "franchise_owner_email")
    private String franchiseOwnerEmail;

    @Column(name = "franchise_owner_phone")
    private String franchiseOwnerPhone;

    @Column(name = "franchise_owner_enroll_date")
    private LocalDateTime franchiseOwnerEnrollDate;

    @Column(name = "franchise_owner_update_date")
    private LocalDateTime franchiseOwnerUpdateDate;

    @Column(name = "franchise_owner_delete_date")
    private LocalDateTime franchiseOwnerDeleteDate;

    public FranchiseOwner(FranchiseOwnerDTO franchiseOwner) {
        this.franchiseOwnerCode= franchiseOwner.getFranchiseOwnerCode();
        this.franchiseOwnerName= franchiseOwner.getFranchiseOwnerName();
        this.franchiseOwnerId= franchiseOwner.getFranchiseOwnerId();
        this.franchiseOwnerPwd= franchiseOwner.getFranchiseOwnerPwd();
        this.franchiseOwnerEmail= franchiseOwner.getFranchiseOwnerEmail();
        this.franchiseOwnerPhone= franchiseOwner.getFranchiseOwnerPhone();
        this.franchiseOwnerEnrollDate= franchiseOwner.getFranchiseOwnerEnrollDate();
        this.franchiseOwnerUpdateDate= franchiseOwner.getFranchiseOwnerUpdateDate();
        this.franchiseOwnerDeleteDate= franchiseOwner.getFranchiseOwnerDeleteDate();
    }
}
