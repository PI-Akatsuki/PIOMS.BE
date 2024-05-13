package com.akatsuki.pioms.frowner.service;

import com.akatsuki.pioms.admin.aggregate.Admin;
import com.akatsuki.pioms.admin.repository.AdminRepository;
import com.akatsuki.pioms.frowner.aggregate.FranchiseOwner;
import com.akatsuki.pioms.frowner.dto.FranchiseOwnerDTO;
import com.akatsuki.pioms.frowner.repository.FranchiseOwnerRepository;
import com.akatsuki.pioms.franchise.aggregate.Franchise;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FranchiseOwnerServiceImpl implements FranchiseOwnerService {

    private final FranchiseOwnerRepository franchiseOwnerRepository;
    private final AdminRepository adminRepository;

    @Autowired
    public FranchiseOwnerServiceImpl(FranchiseOwnerRepository franchiseOwnerRepository, AdminRepository adminRepository) {
        this.franchiseOwnerRepository = franchiseOwnerRepository;
        this.adminRepository = adminRepository;
    }



    // 전체 조회
    @Transactional(readOnly = true)
    @Override
    public List<FranchiseOwnerDTO> findAllFranchiseOwners() {
        return franchiseOwnerRepository.findAll().stream()
                .map(this::convertEntityToDTO)
                .collect(Collectors.toList());
    }

    // 상세 조회
    @Transactional(readOnly = true)
    @Override
    public ResponseEntity<FranchiseOwnerDTO> findFranchiseOwnerById(int franchiseOwnerCode) {
        try {
            FranchiseOwner franchiseOwner = franchiseOwnerRepository.findById(franchiseOwnerCode)
                    .orElseThrow(() -> new RuntimeException("프랜차이즈 오너 코드를 찾을 수 없음: " + franchiseOwnerCode));
            FranchiseOwnerDTO dto = convertEntityToDTO(franchiseOwner);
            return ResponseEntity.ok(dto);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }


    // 오너 등록
    @Override
    @Transactional
    public ResponseEntity<String> registerFranchiseOwner(FranchiseOwner franchiseOwner, int requestorAdminCode) {
        try {
            Admin requestorAdmin = adminRepository.findById(requestorAdminCode).orElse(null);
            if (requestorAdmin == null || requestorAdmin.getAdminCode() != 1) {
                return ResponseEntity.status(403).body("프랜차이즈 오너 등록은 루트 관리자만 가능합니다.");
            }

            if (franchiseOwner.getFranchiseOwnerId() == null || franchiseOwner.getFranchiseOwnerName() == null ||
                    franchiseOwner.getFranchiseOwnerPwd() == null || franchiseOwner.getFranchiseOwnerEmail() == null ||
                    franchiseOwner.getFranchiseOwnerPhone() == null) {
                return ResponseEntity.badRequest().body("필수 항목을 모두 입력해야 합니다.");
            }

            // ID 중복 확인
            if (franchiseOwnerRepository.existsByFranchiseOwnerId(franchiseOwner.getFranchiseOwnerId())) {
                return ResponseEntity.status(409).body("이미 존재하는 아이디입니다.");
            }

            // 날짜 포맷터
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String now = LocalDateTime.now().format(formatter);

            // 등록일 및 수정일 설정
            franchiseOwner.setFranchiseOwnerEnrollDate(now);
            franchiseOwner.setFranchiseOwnerUpdateDate(now);

            franchiseOwnerRepository.save(franchiseOwner);
            return ResponseEntity.ok("신규 프랜차이즈 오너 등록이 완료되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("프랜차이즈 오너 등록 중 오류가 발생했습니다.");
        }
    }


    // 오너 정보 수정
    @Override
    @Transactional
    public ResponseEntity<String> updateFranchiseOwner(int franchiseOwnerCode, FranchiseOwnerDTO updatedFranchiseOwner, int requestorAdminCode) {
        try {
            Admin requestorAdmin = adminRepository.findById(requestorAdminCode).orElse(null);
            if (requestorAdmin == null) {
                return ResponseEntity.status(403).body("수정 권한이 없습니다.");
            }

            FranchiseOwner existingFranchiseOwner = franchiseOwnerRepository.findById(franchiseOwnerCode)
                    .orElseThrow(() -> new RuntimeException("프랜차이즈 오너 코드를 찾을 수 없음: " + franchiseOwnerCode));

            if (requestorAdmin.getAdminCode() == 1 ||
                    (existingFranchiseOwner.getFranchise().getAdmin().getAdminCode() == requestorAdminCode) ||
                    existingFranchiseOwner.getFranchiseOwnerCode() == requestorAdminCode) {

                // 이름 수정 불가
                existingFranchiseOwner.setFranchiseOwnerPwd(updatedFranchiseOwner.getFranchiseOwnerPwd());
                existingFranchiseOwner.setFranchiseOwnerPhone(updatedFranchiseOwner.getFranchiseOwnerPhone());
                existingFranchiseOwner.setFranchiseOwnerEmail(updatedFranchiseOwner.getFranchiseOwnerEmail());

                // 수정일 업데이트
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                existingFranchiseOwner.setFranchiseOwnerUpdateDate(LocalDateTime.now().format(formatter));

                franchiseOwnerRepository.save(existingFranchiseOwner);
                return ResponseEntity.ok("프랜차이즈 오너 정보가 성공적으로 업데이트되었습니다.");
            } else {
                return ResponseEntity.status(403).body("수정 권한이 없습니다.");
            }
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(500).body("프랜차이즈 오너 정보 수정 중 오류가 발생했습니다.");
        }
    }

    // 오너 삭제 (비활성화)
    @Override
    @Transactional
    public ResponseEntity<String> deleteFranchiseOwner(int franchiseOwnerCode, int requestorAdminCode) {
        try {
            Admin requestorAdmin = adminRepository.findById(requestorAdminCode).orElse(null);
            if (requestorAdmin == null || requestorAdmin.getAdminCode() != 1) {
                return ResponseEntity.status(403).body("프랜차이즈 오너 삭제는 루트 관리자만 가능합니다.");
            }

            FranchiseOwner existingFranchiseOwner = franchiseOwnerRepository.findById(franchiseOwnerCode)
                    .orElseThrow(() -> new RuntimeException("프랜차이즈 오너 코드를 찾을 수 없음: " + franchiseOwnerCode));

            if (existingFranchiseOwner.getFranchiseOwnerDeleteDate() != null) {
                return ResponseEntity.status(409).body("이미 삭제된 프랜차이즈 오너입니다.");
            }

            // 삭제일 설정
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            existingFranchiseOwner.setFranchiseOwnerDeleteDate(LocalDateTime.now().format(formatter));

            franchiseOwnerRepository.save(existingFranchiseOwner);
            return ResponseEntity.ok("프랜차이즈 오너가 성공적으로 삭제(비활성화)되었습니다.");
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(500).body("프랜차이즈 오너 삭제 중 오류가 발생했습니다.");
        }
    }


    // Entity -> DTO로 변환
    private FranchiseOwnerDTO convertEntityToDTO(FranchiseOwner franchiseOwner) {
        Franchise franchise = franchiseOwner.getFranchise();
        return FranchiseOwnerDTO.builder()
                .franchiseOwnerCode(franchiseOwner.getFranchiseOwnerCode())
                .franchiseOwnerName(franchiseOwner.getFranchiseOwnerName())
                .franchiseOwnerId(franchiseOwner.getFranchiseOwnerId())
                .franchiseOwnerPwd(franchiseOwner.getFranchiseOwnerPwd())
                .franchiseOwnerEmail(franchiseOwner.getFranchiseOwnerEmail())
                .franchiseOwnerPhone(franchiseOwner.getFranchiseOwnerPhone())
                .franchiseOwnerEnrollDate(franchiseOwner.getFranchiseOwnerEnrollDate())
                .franchiseOwnerUpdateDate(franchiseOwner.getFranchiseOwnerUpdateDate())
                .franchiseOwnerDeleteDate(franchiseOwner.getFranchiseOwnerDeleteDate())
                .franchiseName(franchise.getFranchiseName())
                .adminName(franchise.getAdmin().getAdminName())
                .build();
    }

}
