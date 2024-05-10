package com.akatsuki.pioms.franchise.service;

import com.akatsuki.pioms.admin.aggregate.Admin;
import com.akatsuki.pioms.franchise.aggregate.Franchise;
import com.akatsuki.pioms.franchise.repository.FranchiseRepository;
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
public class FranchiseServiceImpl implements FranchiseService {

    private final FranchiseRepository franchiseRepository;
    private final AdminRepository adminRepository;

    @Autowired
    public FranchiseServiceImpl(FranchiseRepository franchiseRepository, AdminRepository adminRepository) {
        this.franchiseRepository = franchiseRepository;
        this.adminRepository = adminRepository;
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
        if (isOwner) {
            // 점주
            franchise.setFranchiseName(updatedFranchise.getFranchiseName());
            franchise.setFranchiseAddress(updatedFranchise.getFranchiseAddress());
            franchise.setFranchiseCall(updatedFranchise.getFranchiseCall());
            franchise.setFranchiseDeliveryDate(updatedFranchise.getFranchiseDeliveryDate());
        } else {
            Optional<Admin> requestorAdmin = adminRepository.findById(requestorCode);
            if (requestorAdmin.isEmpty()) {
                return ResponseEntity.status(403).body("수정 권한이 없습니다.");
            }

            Admin admin = requestorAdmin.get();
            if (admin.getAdminCode() == 1) {
                // 루트 관리자
                franchise.setFranchiseName(updatedFranchise.getFranchiseName());
                franchise.setFranchiseAddress(updatedFranchise.getFranchiseAddress());
                franchise.setFranchiseCall(updatedFranchise.getFranchiseCall());
                franchise.setFranchiseOwner(updatedFranchise.getFranchiseOwner());
                // admin 지정해주기
                franchise.setAdmin(updatedFranchise.getAdmin());
            } else {
                return ResponseEntity.status(403).body("읽기 전용 권한만 있습니다.");
            }
        }

        // 수정일
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        franchise.setFranchiseUpdateDate(LocalDateTime.now().format(formatter));

        franchiseRepository.save(franchise);
        return ResponseEntity.ok("프랜차이즈 정보가 성공적으로 업데이트되었습니다.");
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
            return ResponseEntity.ok("프랜차이즈가 삭제되었습니다.");
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
