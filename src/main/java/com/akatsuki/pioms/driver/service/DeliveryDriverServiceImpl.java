package com.akatsuki.pioms.driver.service;

import com.akatsuki.pioms.admin.aggregate.Admin;
import com.akatsuki.pioms.driver.aggregate.DeliveryDriver;
import com.akatsuki.pioms.driver.repository.DeliveryDriverRepository;
import com.akatsuki.pioms.admin.repository.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
public class DeliveryDriverServiceImpl implements DeliveryDriverService {

    private final DeliveryDriverRepository deliveryDriverRepository;
    private final AdminRepository adminRepository;

    @Autowired
    public DeliveryDriverServiceImpl(DeliveryDriverRepository deliveryDriverRepository, AdminRepository adminRepository) {
        this.deliveryDriverRepository = deliveryDriverRepository;
        this.adminRepository = adminRepository;
    }

    @Transactional(readOnly = true)
    @Override
    public List<DeliveryDriver> findDriverList() {
        return deliveryDriverRepository.findAll();
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<DeliveryDriver> findDriverById(int driverId) {
        return deliveryDriverRepository.findById(driverId);
    }

    @Override
    @Transactional
    public ResponseEntity<String> saveDriver(DeliveryDriver driver, int requestorAdminCode) {
        // 루트 관리자 확인
        Optional<Admin> requestorAdmin = adminRepository.findById(requestorAdminCode);
        if (requestorAdmin.isEmpty() || requestorAdmin.get().getAdminCode() != 1) {
            return ResponseEntity.status(403).body("배송기사 등록은 루트 관리자만 가능합니다.");
        }

        // 필수 필드 확인
        if (driver.getDriverName() == null || driver.getDriverId() == null || driver.getDriverPwd() == null || driver.getDriverPhone() == null) {
            return ResponseEntity.badRequest().body("필수 항목(deliveryManName, deliveryManId, deliveryManPwd, deliveryManPhone)을 모두 입력해야 합니다.");
        }

        // 중복 ID 확인
        if (deliveryDriverRepository.findByDriverId(driver.getDriverId()).isPresent()) {
            return ResponseEntity.badRequest().body("이미 존재하는 ID입니다.");
        }

        // 날짜 포맷터
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String now = LocalDateTime.now().format(formatter);

        // 등록일 및 수정일 설정
        driver.setDriverEnrollDate(now);
        driver.setDriverUpdateDate(now);

        deliveryDriverRepository.save(driver);
        return ResponseEntity.ok("신규 배송기사 등록이 완료되었습니다.");
    }

    @Transactional
    @Override
    public ResponseEntity<String> updateDriver(int driverId, DeliveryDriver updatedDriver, Integer requestorAdminCode, Integer requestorDriverCode) {
        // 관리자 권한 확인
        boolean isAdmin = requestorAdminCode != null && adminRepository.findById(requestorAdminCode).isPresent();
        boolean isDriver = requestorDriverCode != null && requestorDriverCode == driverId;

        if (!isAdmin && !isDriver) {
            return ResponseEntity.status(403).body("수정 권한이 없습니다.");
        }

        Optional<DeliveryDriver> existingDriver = deliveryDriverRepository.findById(driverId);
        if (existingDriver.isPresent()) {
            DeliveryDriver driver = existingDriver.get();

            if (isAdmin || isDriver) {
                driver.setDriverName(updatedDriver.getDriverName());
                driver.setDriverPwd(updatedDriver.getDriverPwd());
                driver.setDriverPhone(updatedDriver.getDriverPhone());

                // 수정일 업데이트
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                driver.setDriverUpdateDate(LocalDateTime.now().format(formatter));

                deliveryDriverRepository.save(driver);
                return ResponseEntity.ok("배송기사 정보가 성공적으로 업데이트되었습니다.");
            } else {
                return ResponseEntity.status(403).body("수정 권한이 없습니다.");
            }
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Transactional
    @Override
    public ResponseEntity<String> deleteDriver(int driverId, int requestorAdminCode) {
        // 관리자 권한 확인
        Optional<Admin> requestorAdmin = adminRepository.findById(requestorAdminCode);
        if (requestorAdmin.isEmpty() || requestorAdmin.get().getAdminCode() != 1) {
            return ResponseEntity.status(403).body("배송기사 삭제는 루트 관리자만 가능합니다.");
        }

        Optional<DeliveryDriver> existingDriver = deliveryDriverRepository.findById(driverId);
        if (existingDriver.isPresent()) {
            DeliveryDriver driver = existingDriver.get();

            // 이미 비활성화된 경우 처리
            if (driver.getDriverDeleteDate() != null) {
                return ResponseEntity.badRequest().body("이미 비활성화된 배송기사입니다.");
            }

            // DeleteDate 남기기
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            driver.setDriverDeleteDate(LocalDateTime.now().format(formatter));

            deliveryDriverRepository.save(driver);
            return ResponseEntity.ok("배송기사가 비활성화되었습니다.");
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
