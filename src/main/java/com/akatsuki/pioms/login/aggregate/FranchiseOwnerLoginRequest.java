package com.akatsuki.pioms.login.aggregate;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FranchiseOwnerLoginRequest {
    private String frOwnerId;
    private String frOwnerPassword;
}
