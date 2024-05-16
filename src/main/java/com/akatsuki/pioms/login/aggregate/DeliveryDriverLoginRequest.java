package com.akatsuki.pioms.login.aggregate;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DeliveryDriverLoginRequest {
    private String driverId;
    private String driverPassword;
}
