package com.akatsuki.pioms.driver.dto;

import com.akatsuki.pioms.driver.aggregate.DeliveryDriver;
import lombok.*;

import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class DeliveryDriverDTO {
    private int driverCode;
    private String driverName;
    private String driverId;
    private String driverPwd;
    private String driverRole;
    private String driverPhone;
    private boolean driverStatus;
    private int driverPwdCheckCount;
    private String driverEnrollDate;
    private String driverUpdateDate;
    private String driverDeleteDate;
    private String driverStatusString;
    private List<String> franchiseNames;

    // DeliveryDriver 엔티티에서 DeliveryDriverDTO로 변환하는 메서드
    public DeliveryDriverDTO(DeliveryDriver deliveryDriver) {
        this.driverCode = deliveryDriver.getDriverCode();
        this.driverName = deliveryDriver.getDriverName();
        this.driverId = deliveryDriver.getDriverId();
        this.driverPwd = deliveryDriver.getDriverPwd();
        this.driverRole = deliveryDriver.getDriverRole();
        this.driverPhone = deliveryDriver.getDriverPhone();
        this.driverStatus = deliveryDriver.isDriverStatus();
        this.driverPwdCheckCount = deliveryDriver.getDriverPwdCheckCount();
        this.driverEnrollDate = deliveryDriver.getDriverEnrollDate();
        this.driverUpdateDate = deliveryDriver.getDriverUpdateDate();
        this.driverDeleteDate = deliveryDriver.getDriverDeleteDate();
        this.driverStatusString = deliveryDriver.isDriverStatus() ? "활성화" : "비활성화";
        if (deliveryDriver.getFranchises() != null) {
            this.franchiseNames = deliveryDriver.getFranchises().stream()
                    .map(franchise -> franchise.getFranchiseName())
                    .collect(Collectors.toList());
        }
    }
}
