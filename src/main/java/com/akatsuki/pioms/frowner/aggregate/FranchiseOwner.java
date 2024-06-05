package com.akatsuki.pioms.frowner.aggregate;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import com.akatsuki.pioms.franchise.aggregate.Franchise;

@Entity
@Table(name = "franchise_owner")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class FranchiseOwner {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    @Column(name = "franchise_owner_role")
    private String franchiseRole;

    @Column(name = "franchise_owner_status")
    private boolean franchiseOwnerStatus;

    @Column(name = "franchise_owner_pwd_check")
    private int ownerPwdCheckCount;

    @Column(name = "franchise_owner_enroll_date")
    private String franchiseOwnerEnrollDate;

    @Column(name = "franchise_owner_update_date")
    private String franchiseOwnerUpdateDate;

    @Column(name = "franchise_owner_delete_date")
    private String franchiseOwnerDeleteDate;

    @OneToOne(mappedBy = "franchiseOwner")
    @ToString.Exclude
    @JsonIgnore
    private Franchise franchise;
}
