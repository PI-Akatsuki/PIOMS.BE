package com.akatsuki.pioms.ask.aggregate;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name = "franchiseOwner")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class FranchiseOwnerEntity {
    @Id
    @Column(name = "franchise_owner_code")
    private int franchiseOwnerCode;

    @Column(name = "franchise_owner_name")
    private String franchiseOwnerName;

    @Column(name = "franchise_owner_Id")
    private String franchiseOwnerId;

    @Column(name = "franchise_owner_pwd")
    private String franchiseOwnerPwd;

    @Column(name = "franchise_owner_email")
    private String franchiseOwnerEmail;

    @Column(name = "franchise_owner_enroll_date")
    private LocalDateTime franchiseOwnerEnrollDate;

    @Column(name = "franchise_owner_update_date")
    private Date franchiseOwnerUpdateDate;

    @Column(name = "franchise_owner_delete_date")
    private Date franchiseOwnerDeleteDate;

}
