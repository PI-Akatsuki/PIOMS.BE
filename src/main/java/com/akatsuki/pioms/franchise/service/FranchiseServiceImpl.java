package com.akatsuki.pioms.franchise.service;

import com.akatsuki.pioms.admin.aggregate.Admin;
import com.akatsuki.pioms.franchise.aggregate.DELIVERY_DATE;
import com.akatsuki.pioms.franchise.aggregate.Franchise;
import com.akatsuki.pioms.franchise.dto.FranchiseDTO;
import com.akatsuki.pioms.franchise.repository.FranchiseRepository;
import com.akatsuki.pioms.admin.repository.AdminRepository;
import com.akatsuki.pioms.log.etc.LogStatus;
import com.akatsuki.pioms.log.service.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;

@Service
public class FranchiseServiceImpl implements FranchiseService {

    private final FranchiseRepository franchiseRepository;
    private final AdminRepository adminRepository;
    private final LogService logService;

    @Autowired
    public FranchiseServiceImpl(FranchiseRepository franchiseRepository, AdminRepository adminRepository,LogService logService) {
        this.franchiseRepository = franchiseRepository;
        this.adminRepository = adminRepository;
        this.logService = logService;
    }

    // 프랜차이즈 전체 조회
    @Transactional(readOnly = true)
    @Override
    public List<Franchise> findFranchiseList() {
        return franchiseRepository.findAll();
    }

    // 프랜차이즈 상세 조회
    @Transactional(readOnly = true)
    @Override
    public Optional<Franchise> findFranchiseById(int franchiseCode) {
        return franchiseRepository.findById(franchiseCode);
    }

    // 신규 프랜차이즈 등록
    @Override
    @Transactional
    public ResponseEntity<String> saveFranchise(Franchise franchise, int requestorAdminCode) {
        // 루트 관리자 확인
        Optional<Admin> requestorAdmin = adminRepository.findById(requestorAdminCode);
        if (requestorAdmin.isEmpty() || requestorAdmin.get().getAdminCode() != 1) {
            return ResponseEntity.status(403).body("프랜차이즈 등록은 루트 관리자만 가능합니다.");
        }

        // 필수 필드 확인
        if (franchise.getFranchiseName() == null || franchise.getFranchiseAddress() == null || franchise.getFranchiseCall() == null) {
            return ResponseEntity.badRequest().body("필수 항목(name, address, call)을 모두 입력해야 합니다.");
        }

        // 날짜 포맷터
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String now = LocalDateTime.now().format(formatter);

        // 등록일 설정
        franchise.setFranchiseEnrollDate(now);

        // 수정일 설정
        franchise.setFranchiseUpdateDate(now);

        franchiseRepository.save(franchise);
        logService.saveLog("root", LogStatus.등록, franchise.getFranchiseName(), "Franchise");
        return ResponseEntity.ok("신규 프랜차이즈 등록이 완료되었습니다.");
    }

    // 프랜차이즈 정보 수정
    @Override
    @Transactional
    public ResponseEntity<String> updateFranchise(int franchiseCode, Franchise updatedFranchise, int requestorCode, boolean isOwner) {
        Optional<Franchise> franchiseOptional = franchiseRepository.findById(franchiseCode);
        if (franchiseOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Franchise franchise = franchiseOptional.get();
        StringBuilder changes = new StringBuilder();
        if (isOwner || isAdmin(requestorCode)) {
            updateFields(franchise, updatedFranchise, changes);
        } else {
            return ResponseEntity.status(403).body("수정 권한이 없습니다.");
        }

        franchise.setFranchiseUpdateDate(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        franchiseRepository.save(franchise);
        logChanges(changes);

        return ResponseEntity.ok("가맹점 정보가 성공적으로 수정 되었습니다..");
    }

    private void updateFields(Franchise franchise, Franchise updatedFranchise, StringBuilder changes) {
        checkAndUpdateField("Name", franchise.getFranchiseName(), updatedFranchise.getFranchiseName(), changes, franchise::setFranchiseName);
        checkAndUpdateField("Address", franchise.getFranchiseAddress(), updatedFranchise.getFranchiseAddress(), changes, franchise::setFranchiseAddress);
        checkAndUpdateField("Call", franchise.getFranchiseCall(), updatedFranchise.getFranchiseCall(), changes, franchise::setFranchiseCall);
        updateDeliveryDate(franchise, updatedFranchise.getFranchiseDeliveryDate(), changes);
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
        if (changes.length() > 0) {
            logService.saveLog("root", LogStatus.수정, changes.toString(), "Franchise");
        } else {
            logService.saveLog("root", LogStatus.수정, "No changes made.", "Franchise");
        }
    }



    // 프랜차이즈 삭제 (비활성화)
    @Transactional
    @Override
    public ResponseEntity<String> deleteFranchise(int franchiseCode, int requestorAdminCode) {
        // 루트 관리자만 삭제 가능
        Optional<Admin> requestorAdmin = adminRepository.findById(requestorAdminCode);
        if (requestorAdmin.isEmpty() || requestorAdmin.get().getAdminCode() != 1) {
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
            logService.saveLog("root", LogStatus.삭제, franchise.getFranchiseName(), "Franchise");
            return ResponseEntity.ok("프랜차이즈가 삭제되었습니다.");
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Override
    public FranchiseDTO findFranchiseByFranchiseOwnerCode(int franchiseOwnerCode) {
        Franchise franchise = franchiseRepository.findByFranchiseOwnerFranchiseOwnerCode(franchiseOwnerCode);
        return new FranchiseDTO(franchise);
    }
}
