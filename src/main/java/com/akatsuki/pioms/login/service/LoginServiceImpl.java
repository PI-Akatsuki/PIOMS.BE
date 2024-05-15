package com.akatsuki.pioms.login.service;

import com.akatsuki.pioms.admin.aggregate.Admin;
import com.akatsuki.pioms.admin.repository.AdminRepository;
import com.akatsuki.pioms.driver.repository.DeliveryDriverRepository;
import com.akatsuki.pioms.frowner.aggregate.FranchiseOwner;
import com.akatsuki.pioms.frowner.repository.FranchiseOwnerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.logging.Logger;

@Service
public class LoginServiceImpl implements LoginService {

    private static final Logger logger = Logger.getLogger(LoginServiceImpl.class.getName());

    private final AdminRepository adminRepository;
    private final FranchiseOwnerRepository franchiseOwnerRepository;
    private final DeliveryDriverRepository deliveryDriverRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public LoginServiceImpl(AdminRepository adminRepository, FranchiseOwnerRepository franchiseOwnerRepository, DeliveryDriverRepository deliveryDriverRepository, PasswordEncoder passwordEncoder) {
        this.adminRepository = adminRepository;
        this.franchiseOwnerRepository = franchiseOwnerRepository;
        this.deliveryDriverRepository = deliveryDriverRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public ResponseEntity<Admin> adminLogin(String adminId, String password, String accessNumber) {
        Optional<Admin> optionalAdmin = adminRepository.findByAdminId(adminId);

        if (optionalAdmin.isPresent()) {
            Admin admin = optionalAdmin.get();
            logger.info("관리자: " + admin.getAdminId());

            if (passwordEncoder.matches(password, admin.getAdminPwd()) && admin.getAccessNumber().equals(accessNumber)) {
                logger.info("비밀번호와 접속번호가 일치함");
                return ResponseEntity.ok(admin);
            } else {
                logger.warning("비밀번호나 접속번호가 일치하지 않음");
            }
        } else {
            logger.warning("관리자를 찾을 수 없음: " + adminId);
        }

        return ResponseEntity.status(401).build();
    }

    @Override
    public ResponseEntity<FranchiseOwner> frOwnerLogin(String frOwnerId, String frOwnerPassword) {
        logger.info("Attempting to find FranchiseOwner with ID: " + frOwnerId);
        Optional<FranchiseOwner> optionalFranchiseOwner = franchiseOwnerRepository.findByFranchiseOwnerId(frOwnerId);

        if (optionalFranchiseOwner.isPresent()) {
            FranchiseOwner franchiseOwner = optionalFranchiseOwner.get();
            logger.info("점주: " + franchiseOwner.getFranchiseOwnerId());

            if (passwordEncoder.matches(frOwnerPassword, franchiseOwner.getFranchiseOwnerPwd())) {
                logger.info("비밀번호 일치함");
                return ResponseEntity.ok(franchiseOwner);
            } else {
                logger.warning("비밀번호가 일치하지 않음");
            }
        } else {
            logger.warning("점주 찾을 수 없음: " + frOwnerId);
        }

        return ResponseEntity.status(401).build();
    }

}
