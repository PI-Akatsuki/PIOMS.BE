package com.akatsuki.pioms.login.aggregate;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdminLoginRequest {
    private String adminId;
    private String password;
    private String accessNumber;
}
