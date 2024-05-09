package com.akatsuki.pioms.franchise.entity;


import com.akatsuki.pioms.admin.entity.Admin;
import com.akatsuki.pioms.franchise.etc.DELIVERY_DATE;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;


@Entity
@Table(name = "franchise")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class FranchiseEntity {
    @Id
    @Column(name = "franchise_code")
    private int franchiseCode;

    @Column(name = "franchise_name")
    private String franchiseName;

    @Column(name = "franchise_address")
    private String franchiseAddress;

    @Column(name = "franchise_call")
    private String franchiseCall;

    @Column(name = "franchise_enroll_date")
    private LocalDateTime franchiseEnrollDate;

    @Column(name = "franchise_update_date")
    private LocalDateTime franchiseUpdateDate;

    @Column(name = "franchise_business_num")
    private String franchiseBusinessNum;

    @Column(name = "franchise_delivery_date")
    @Enumerated(EnumType.STRING)
    private DELIVERY_DATE FranchiseDeliveryDate;

    @JoinColumn(name = "franchise_owner_code")
    @OneToOne
    private FranchiseOwner franchiseOwner;

    @JoinColumn(name = "admin_code")
    @ManyToOne
    private Admin admin;

    @Column(name = "company_code")
    private int companyCode;
}
