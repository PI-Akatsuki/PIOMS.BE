package com.akatsuki.pioms.driver.dto;

import com.akatsuki.pioms.driver.aggregate.DeliveryDriver;
import lombok.*;

import java.util.HashMap;
import java.util.Map;

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
    private Map<String,String> franchises;

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
        this.franchises = new HashMap<>();
        if (deliveryDriver.getFranchises() != null && !deliveryDriver.getFranchises().isEmpty()){
            deliveryDriver.getFranchises().forEach(franchise -> {
                franchises.put(franchise.getFranchiseName(),franchise.getFranchiseAddress());
            });
        }
    }

    public String driverStatus() {
        return this.driverStatus ? "활성화" : "비활성화";
    }
}
