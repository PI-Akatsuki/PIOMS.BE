package com.akatsuki.pioms.driver.aggregate;

import com.akatsuki.pioms.franchise.aggregate.Franchise;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@ToString
@Entity
@Table(name = "delivery_man")
public class DeliveryDriver {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "delivery_man_code")
    private int driverCode;

    @Column(name = "delivery_man_name")
    private String driverName;

    @Column(name = "delivery_man_id")
    private String driverId;

    @Column(name = "delivery_man_pwd")
    private String driverPwd;

    @Column(name = "delivery_man_role")
    private String driverRole;

    @Column(name = "delivery_man_phone")
    private String driverPhone;

    @Column(name = "delivery_man_enroll_date")
    private String driverEnrollDate;

    @Column(name = "delivery_man_update_date")
    private String driverUpdateDate;

    @Column(name = "delivery_man_delete_date")
    private String driverDeleteDate;

    @Column(name = "delivery_man_pwd_check")
    private int driverPwdCheckCount;

    @Column(name = "delivery_man_status")
    private boolean driverStatus;

    @OneToMany(mappedBy = "deliveryDriver", fetch = FetchType.LAZY)
    @JsonIgnore
    @ToString.Exclude
    private List<Franchise> franchises;
}
