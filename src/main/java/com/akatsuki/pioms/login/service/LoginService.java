package com.akatsuki.pioms.login.service;

import com.akatsuki.pioms.admin.aggregate.Admin;
import com.akatsuki.pioms.frowner.aggregate.FranchiseOwner;
import org.springframework.http.ResponseEntity;

public interface LoginService {

    // 관리자 로그인
    ResponseEntity<Admin> adminLogin(String adminId, String password, String accessNumber);

    // 오너 로그인
    ResponseEntity<FranchiseOwner> frOwnerLogin(String frOwnerId, String frOwnerPassword);

}
