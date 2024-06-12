package com.akatsuki.pioms.franchise.aggregate;

import com.akatsuki.pioms.admin.aggregate.Admin;
import com.akatsuki.pioms.driver.aggregate.DeliveryDriver;
import com.akatsuki.pioms.frowner.aggregate.FranchiseOwner;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
@Entity
@Table(name = "franchise")
public class Franchise {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "franchise_code")
    private int franchiseCode;

    @Column(name = "franchise_name")
    private String franchiseName;

    @Column(name = "franchise_address")
    private String franchiseAddress;

    @Column(name = "franchise_call")
    private String franchiseCall;

    @Column(name = "franchise_enroll_date")
    private String franchiseEnrollDate;

    @Column(name = "franchise_update_date")
    private String franchiseUpdateDate;

    @Column(name = "franchise_delete_date")
    private String franchiseDeleteDate;

    @Column(name = "franchise_business_num")
    private String franchiseBusinessNum;

    @Column(name = "franchise_delivery_date")
    @Enumerated(EnumType.STRING)
    private DELIVERY_DATE franchiseDeliveryDate;

    @JoinColumn(name = "franchise_owner_code")
    @OneToOne
    private FranchiseOwner franchiseOwner;

    @JoinColumn(name = "delivery_man_code")
    @ManyToOne
    private DeliveryDriver deliveryDriver;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "admin_code", referencedColumnName = "admin_code")
    @ToString.Exclude
    private Admin admin;

}
