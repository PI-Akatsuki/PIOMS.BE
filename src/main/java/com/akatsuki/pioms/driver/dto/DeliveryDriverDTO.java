package com.akatsuki.pioms.driver.dto;

import com.akatsuki.pioms.driver.aggregate.DeliveryDriver;
import lombok.*;

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
    }
}
