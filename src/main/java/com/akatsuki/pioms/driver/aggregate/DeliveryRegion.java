package com.akatsuki.pioms.driver.aggregate;


import com.akatsuki.pioms.franchise.aggregate.Franchise;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name = "delivery_region")
@Entity
public class DeliveryRegion {
    @Id
    @Column(name = "delivery_region_code")
    private int deliveryRegionCode;

    @JoinColumn(name = "franchise_code")
    @OneToOne
    private Franchise franchise;

    @JoinColumn(name = "delivery_man_code")
    @ManyToOne
    private DeliveryDriver deliveryDriver;

}
