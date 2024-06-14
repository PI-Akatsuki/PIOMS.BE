package com.akatsuki.pioms.driver.service;

import com.akatsuki.pioms.driver.aggregate.DeliveryDriver;
import com.akatsuki.pioms.driver.dto.DeliveryDriverDTO;
import com.akatsuki.pioms.driver.repository.DeliveryDriverRepository;
import com.akatsuki.pioms.log.etc.LogStatus;
import com.akatsuki.pioms.log.service.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DeliveryDriverServiceImpl implements DeliveryDriverService {

    private final DeliveryDriverRepository deliveryDriverRepository;
    private final PasswordEncoder passwordEncoder;
    private final LogService logService;

    @Autowired
    public DeliveryDriverServiceImpl(DeliveryDriverRepository deliveryDriverRepository, PasswordEncoder passwordEncoder, LogService logService) {
        this.deliveryDriverRepository = deliveryDriverRepository;
        this.passwordEncoder = passwordEncoder;
        this.logService = logService;
    }

    // 현재 사용자가 ROOT인지 확인
    private boolean isCurrentUserRoot() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getAuthorities() != null) {
            for (GrantedAuthority authority : authentication.getAuthorities()) {
                if ("ROLE_ROOT".equals(authority.getAuthority())) {
                    return true;
                }
            }
        }
        return false;
    }

    // 현재 사용자 이름 가져오기
    private String getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null ? authentication.getName() : null;
    }

    @Transactional(readOnly = true)
    @Override
    public List<DeliveryDriverDTO> findDriverList() {
        return deliveryDriverRepository.findAllByOrderByDriverEnrollDateDesc().stream()
                .map(DeliveryDriverDTO::new)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<DeliveryDriverDTO> findDriverById(int driverId) {
        return deliveryDriverRepository.findById(driverId).map(DeliveryDriverDTO::new);
    }

    @Override
    @Transactional
    public ResponseEntity<String> saveDriver(DeliveryDriverDTO driverDTO) {
        if (!isCurrentUserRoot()) {
            return ResponseEntity.status(403).body("배송기사 등록은 루트 관리자만 가능합니다.");
        }

        if (driverDTO.getDriverName() == null || driverDTO.getDriverId() == null || driverDTO.getDriverPwd() == null || driverDTO.getDriverPhone() == null) {
            return ResponseEntity.badRequest().body("필수 항목을 모두 입력해야 합니다.");
        }

        if (deliveryDriverRepository.findByDriverId(driverDTO.getDriverId()).isPresent()) {
            return ResponseEntity.status(409).body("이미 존재하는 ID입니다.");
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String now = LocalDateTime.now().format(formatter);

        DeliveryDriver driver = DeliveryDriver.builder()
                .driverName(driverDTO.getDriverName())
                .driverId(driverDTO.getDriverId())
                .driverPwd(passwordEncoder.encode(driverDTO.getDriverPwd()))
                .driverRole("ROLE_DRIVER")
                .driverPhone(driverDTO.getDriverPhone())
                .driverStatus(true)
                .driverPwdCheckCount(driverDTO.getDriverPwdCheckCount())
                .driverEnrollDate(now)
                .driverUpdateDate(now)
                .build();

        deliveryDriverRepository.save(driver);
        return ResponseEntity.ok("신규 배송기사 등록이 완료되었습니다.");
    }

    @Transactional
    @Override
    public ResponseEntity<String> updateDriver(int driverId, DeliveryDriverDTO updatedDriverDTO) {
        boolean isAdmin = isCurrentUserRoot();
        boolean isDriver = getCurrentUser() != null && updatedDriverDTO.getDriverId().equals(getCurrentUser());

        if (!isAdmin && !isDriver) {
            return ResponseEntity.status(403).body("수정 권한이 없습니다.");
        }

        Optional<DeliveryDriver> existingDriver = deliveryDriverRepository.findById(driverId);
        if (existingDriver.isPresent()) {
            DeliveryDriver driver = existingDriver.get();

            StringBuilder changes = new StringBuilder();

            if (!driver.getDriverPwd().equals(passwordEncoder.encode(updatedDriverDTO.getDriverPwd()))) {
                changes.append(String.format("pwd 변경 '%s'에서 '%s'으로; ", driver.getDriverPwd(), updatedDriverDTO.getDriverPwd()));
                driver.setDriverPwd(passwordEncoder.encode(updatedDriverDTO.getDriverPwd()));
            }
            if (!driver.getDriverPhone().equals(updatedDriverDTO.getDriverPhone())) {
                changes.append(String.format("phone 변경 '%s'에서 '%s'으로; ", driver.getDriverPhone(), updatedDriverDTO.getDriverPhone()));
                driver.setDriverPhone(updatedDriverDTO.getDriverPhone());
            }
            if (driver.isDriverStatus() != updatedDriverDTO.isDriverStatus()) {
                changes.append(String.format("Status 변경 '%s'에서 '%s'으로; ", driver.isDriverStatus(), updatedDriverDTO.isDriverStatus()));
                driver.setDriverStatus(updatedDriverDTO.isDriverStatus());
            }

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            driver.setDriverUpdateDate(LocalDateTime.now().format(formatter));

            deliveryDriverRepository.save(driver);
            String username = getCurrentUser();
            if (changes.length() > 0) {
                logService.saveLog(username, LogStatus.수정, changes.toString(), "DeliveryDriver");
            }
            return ResponseEntity.ok("배송기사 정보가 성공적으로 업데이트되었습니다.");
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Transactional
    @Override
    public ResponseEntity<String> deleteDriver(int driverId) {
        if (!isCurrentUserRoot()) {
            return ResponseEntity.status(403).body("배송기사 삭제는 루트 관리자만 가능합니다.");
        }

        DeliveryDriver existingDriver = deliveryDriverRepository.findById(driverId)
                .orElseThrow(() -> new RuntimeException("배송기사 코드를 찾을 수 없음: " + driverId));

        if (existingDriver.getDriverDeleteDate() != null) {
            return ResponseEntity.status(409).body("이미 비활성화된 배송기사입니다.");
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        existingDriver.setDriverDeleteDate(LocalDateTime.now().format(formatter));
        existingDriver.setDriverStatus(false);
        deliveryDriverRepository.save(existingDriver);
        String username = getCurrentUser();
        logService.saveLog(username, LogStatus.삭제, existingDriver.getDriverName(), "DeliveryDriver");
        return ResponseEntity.ok("배송기사가 비활성화되었습니다.");
    }

    @Override
    @Transactional
    public ResponseEntity<String> resetDriverPassword(int driverId) {
        if (!isCurrentUserRoot()) {
            return ResponseEntity.status(403).body("비밀번호 초기화는 루트 관리자만 가능합니다.");
        }

        DeliveryDriver driver = deliveryDriverRepository.findById(driverId)
                .orElseThrow(() -> new RuntimeException("배송기사 코드를 찾을 수 없음: " + driverId));

        String encodedPassword = passwordEncoder.encode("1234");
        driver.setDriverPwd(encodedPassword);
        deliveryDriverRepository.save(driver);

        String username = getCurrentUser();
        logService.saveLog(username, LogStatus.수정, "비밀번호 초기화: " + driver.getDriverName(), "DeliveryDriver");

        return ResponseEntity.ok("배송기사 비밀번호 초기화가 완료되었습니다.");
    }

}
