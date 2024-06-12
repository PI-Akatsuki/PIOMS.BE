package com.akatsuki.pioms.franchise.service;

import com.akatsuki.pioms.admin.aggregate.Admin;
import com.akatsuki.pioms.driver.aggregate.DeliveryDriver;
import com.akatsuki.pioms.driver.repository.DeliveryDriverRepository;
import com.akatsuki.pioms.config.GetUserInfo;
import com.akatsuki.pioms.franchise.aggregate.DELIVERY_DATE;
import com.akatsuki.pioms.franchise.aggregate.Franchise;
import com.akatsuki.pioms.franchise.dto.FranchiseDTO;
import com.akatsuki.pioms.franchise.repository.FranchiseRepository;
import com.akatsuki.pioms.admin.repository.AdminRepository;
import com.akatsuki.pioms.frowner.aggregate.FranchiseOwner;
import com.akatsuki.pioms.frowner.repository.FranchiseOwnerRepository;
import com.akatsuki.pioms.log.etc.LogStatus;
import com.akatsuki.pioms.log.service.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@Service
public class FranchiseServiceImpl implements FranchiseService {

    private final FranchiseRepository franchiseRepository;
    private final FranchiseOwnerRepository franchiseOwnerRepository;
    private final DeliveryDriverRepository deliveryDriverRepository;
    private final AdminRepository adminRepository;
    private final LogService logService;
    private final GetUserInfo getUserInfo;

    @Autowired
    public FranchiseServiceImpl(FranchiseRepository franchiseRepository, FranchiseOwnerRepository franchiseOwnerRepository, DeliveryDriverRepository deliveryDriverRepository, AdminRepository adminRepository, LogService logService, GetUserInfo getUserInfo) {
        this.franchiseRepository = franchiseRepository;
        this.franchiseOwnerRepository = franchiseOwnerRepository;
        this.deliveryDriverRepository = deliveryDriverRepository;
        this.adminRepository = adminRepository;
        this.logService = logService;
        this.getUserInfo = getUserInfo;
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

    // 프랜차이즈 전체 조회
    @Transactional(readOnly = true)
    @Override
    public List<FranchiseDTO> findFranchiseList() {

//        List<Franchise> franchiseList = franchiseRepository.findAllByAdminAdminCode(adminCode);
        return franchiseRepository.findAllByOrderByFranchiseEnrollDateDesc().stream()
                .map(FranchiseDTO::new)
                .collect(Collectors.toList());
    }

    // 프랜차이즈 상세 조회
    @Transactional(readOnly = true)
    @Override
    public Optional<FranchiseDTO> findFranchiseById(int franchiseCode) {
        return franchiseRepository.findById(franchiseCode).map(FranchiseDTO::new);
    }

    // 신규 프랜차이즈 등록
    @Override
    @Transactional
    public ResponseEntity<String> saveFranchise(FranchiseDTO franchiseDTO) {
        if (!isCurrentUserRoot()) {
            return ResponseEntity.status(403).body("프랜차이즈 등록은 루트 관리자만 가능합니다.");
        }

        if (franchiseDTO.getFranchiseName() == null || franchiseDTO.getFranchiseAddress() == null || franchiseDTO.getFranchiseCall() == null) {
            return ResponseEntity.badRequest().body("필수 항목(name, address, call)을 모두 입력해야 합니다.");
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String now = LocalDateTime.now().format(formatter);

        FranchiseOwner franchiseOwner;
        if (franchiseDTO.getFranchiseOwner() == null || franchiseDTO.getFranchiseOwner().getFranchiseOwnerCode() == 0) {
            return ResponseEntity.badRequest().body("프랜차이즈 소유자는 필수 항목입니다.");
        } else {
            franchiseOwner = franchiseDTO.getFranchiseOwner().toEntity();
        }

        Franchise franchise = Franchise.builder()
                .franchiseName(franchiseDTO.getFranchiseName())
                .franchiseAddress(franchiseDTO.getFranchiseAddress())
                .franchiseCall(franchiseDTO.getFranchiseCall())
                .franchiseEnrollDate(now)
                .franchiseUpdateDate(now)
                .franchiseBusinessNum(franchiseDTO.getFranchiseBusinessNum())
                .franchiseDeliveryDate(franchiseDTO.getFranchiseDeliveryDate())
                .franchiseOwner(franchiseOwner)
                .deliveryDriver(franchiseDTO.getDeliveryDriver())
                .admin(adminRepository.findById(franchiseDTO.getAdminCode()).orElse(null))
                .build();

        franchiseRepository.save(franchise);
        logService.saveLog(getCurrentUser(), LogStatus.등록, franchise.getFranchiseName(), "Franchise");
        return ResponseEntity.ok("신규 프랜차이즈 등록이 완료되었습니다.");
    }


    // 프랜차이즈 정보 수정
    @Override
    @Transactional
    public ResponseEntity<String> updateFranchise(int franchiseCode, FranchiseDTO updatedFranchiseDTO) {
        Optional<Franchise> franchiseOptional = franchiseRepository.findById(franchiseCode);
        if (franchiseOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Franchise franchise = franchiseOptional.get();
        StringBuilder changes = new StringBuilder();

        // Update the fields with values from DTO
        franchise.setFranchiseName(updatedFranchiseDTO.getFranchiseName());
        franchise.setFranchiseAddress(updatedFranchiseDTO.getFranchiseAddress());
        franchise.setFranchiseCall(updatedFranchiseDTO.getFranchiseCall());
        franchise.setFranchiseBusinessNum(updatedFranchiseDTO.getFranchiseBusinessNum());
        franchise.setFranchiseDeliveryDate(updatedFranchiseDTO.getFranchiseDeliveryDate());

        // Update relationships
        if (updatedFranchiseDTO.getFranchiseOwner() != null) {
            Optional<FranchiseOwner> ownerOptional = franchiseOwnerRepository.findById(updatedFranchiseDTO.getFranchiseOwner().getFranchiseOwnerCode());
            ownerOptional.ifPresent(franchise::setFranchiseOwner);
        }

        if (updatedFranchiseDTO.getDeliveryDriver() != null) {
            Optional<DeliveryDriver> driverOptional = deliveryDriverRepository.findById(updatedFranchiseDTO.getDeliveryDriver().getDriverCode());
            driverOptional.ifPresent(franchise::setDeliveryDriver);
        }

        franchise.setFranchiseUpdateDate(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        return ResponseEntity.ok("가맹점 정보가 성공적으로 수정 되었습니다.");
    }

    private void updateFields(Franchise franchise, FranchiseDTO updatedFranchiseDTO, StringBuilder changes) {
        checkAndUpdateField("Name", franchise.getFranchiseName(), updatedFranchiseDTO.getFranchiseName(), changes, franchise::setFranchiseName);
        checkAndUpdateField("Address", franchise.getFranchiseAddress(), updatedFranchiseDTO.getFranchiseAddress(), changes, franchise::setFranchiseAddress);
        checkAndUpdateField("Call", franchise.getFranchiseCall(), updatedFranchiseDTO.getFranchiseCall(), changes, franchise::setFranchiseCall);
        updateDeliveryDate(franchise, updatedFranchiseDTO.getFranchiseDeliveryDate(), changes);
    }

    private void updateDeliveryDate(Franchise franchise, DELIVERY_DATE updatedDate, StringBuilder changes) {
        if (franchise.getFranchiseDeliveryDate() != updatedDate) {
            changes.append(String.format("Delivery Date: '%s' -> '%s'; ", franchise.getFranchiseDeliveryDate(), updatedDate));
            franchise.setFranchiseDeliveryDate(updatedDate);
        }
    }

    private boolean isAdmin(int requestorCode) {
        return adminRepository.findById(requestorCode)
                .map(Admin::getAdminCode)
                .map(code -> code == 1)
                .orElse(false);
    }

    private void checkAndUpdateField(String fieldName, String original, String updated, StringBuilder changes, Consumer<String> setter) {
        if (!Objects.equals(original, updated)) {
            changes.append(String.format("%s: '%s' -> '%s'; ", fieldName, original, updated));
            setter.accept(updated);
        }
    }

    private void logChanges(StringBuilder changes) {
        String username = getCurrentUser();
        if (changes.length() > 0) {
            logService.saveLog(username, LogStatus.수정, changes.toString(), "Franchise");
        } else {
            logService.saveLog(username, LogStatus.수정, "No changes made.", "Franchise");
        }
    }

    // 프랜차이즈 삭제 (비활성화)
    @Transactional
    @Override
    public ResponseEntity<String> deleteFranchise(int franchiseCode) {
        if (!isCurrentUserRoot()) {
            return ResponseEntity.status(403).body("프랜차이즈 삭제는 루트 관리자만 가능합니다.");
        }

        Optional<Franchise> franchiseOptional = franchiseRepository.findById(franchiseCode);
        if (franchiseOptional.isPresent()) {
            Franchise franchise = franchiseOptional.get();

            if (franchise.getFranchiseDeleteDate() != null && !franchise.getFranchiseDeleteDate().isEmpty()) {
                return ResponseEntity.badRequest().body("해당 프랜차이즈는 이미 삭제되었습니다.");
            }

            franchise.setFranchiseDeleteDate(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            franchiseRepository.save(franchise);
            logService.saveLog(getCurrentUser(), LogStatus.삭제, franchise.getFranchiseName(), "Franchise");
            return ResponseEntity.ok("프랜차이즈가 삭제되었습니다.");
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // 사용자 코드로 프랜차이즈 조회
    @Override
    public FranchiseDTO findFranchiseByFranchiseOwnerCode(int franchiseOwnerCode) {
        Franchise franchise = franchiseRepository.findByFranchiseOwnerFranchiseOwnerCode(franchiseOwnerCode);
        return new FranchiseDTO(franchise);
    }

    // 드라이버 코드로 프랜차이즈 리스트 조회
    @Override
    public List<FranchiseDTO> findFranchiseListByDriverCode(int driverCode) {
        return franchiseRepository.findAllByDeliveryDriverDriverCode(driverCode).stream()
                .map(FranchiseDTO::new)
                .collect(Collectors.toList());
    }



    // 관리자 코드로 프랜차이즈 리스트 조회
    @Override
    public List<FranchiseDTO> findFranchiseByAdminCode() {
        int adminCode = getUserInfo.getAdminCode();
        return franchiseRepository.findAllByAdminAdminCode(adminCode).stream()
                .map(FranchiseDTO::new)
                .collect(Collectors.toList());
    }

    // 사용자 아이디로 프랜차이즈 리스트 조회 (루트 사용자가 아닌 경우)
    @Override
    public List<FranchiseDTO> findFranchiseListByUser(String userId) {
        if (userId == null || isCurrentUserRoot()) {
            return findFranchiseList();
        }

        Optional<Admin> adminOptional = adminRepository.findByAdminId(userId);
        if (adminOptional.isPresent()) {
            Admin admin = adminOptional.get();
            return findFranchiseByAdminCode();
        }

        return List.of();
    }

    @Override
    public FranchiseDTO findFranchiseByFranchiseOwnerCode() {
        int franchiseOwnerCode = getUserInfo.getFranchiseOwnerCode();
        return new FranchiseDTO(franchiseRepository.findById(franchiseOwnerCode).get());
    }
}
