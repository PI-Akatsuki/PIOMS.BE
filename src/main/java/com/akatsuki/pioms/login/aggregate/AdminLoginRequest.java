package com.akatsuki.pioms.login.aggregate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminLoginRequest {
    private String adminId;
    private String password;
    private String accessNumber;
}
