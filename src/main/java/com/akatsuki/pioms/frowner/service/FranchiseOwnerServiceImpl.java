package com.akatsuki.pioms.frowner.service;

import com.akatsuki.pioms.config.GetUserInfo;
import com.akatsuki.pioms.frowner.aggregate.FranchiseOwner;
import com.akatsuki.pioms.frowner.dto.FranchiseOwnerDTO;
import com.akatsuki.pioms.frowner.repository.FranchiseOwnerRepository;
import com.akatsuki.pioms.log.etc.LogStatus;
import com.akatsuki.pioms.log.service.LogService;
import jakarta.persistence.EntityNotFoundException;
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
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class FranchiseOwnerServiceImpl implements FranchiseOwnerService {

    private final FranchiseOwnerRepository franchiseOwnerRepository;
    private final LogService logService;
    private final PasswordEncoder passwordEncoder;

    private final GetUserInfo getUserInfo;

    @Autowired
    public FranchiseOwnerServiceImpl(FranchiseOwnerRepository franchiseOwnerRepository, LogService logService, PasswordEncoder passwordEncoder, GetUserInfo getUserInfo) {
        this.franchiseOwnerRepository = franchiseOwnerRepository;
        this.logService = logService;
        this.passwordEncoder = passwordEncoder;
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

    // 전체 조회
    @Transactional(readOnly = true)
    @Override
    public List<FranchiseOwnerDTO> findAllFranchiseOwners() {
        int adminCode = getUserInfo.getAdminCode();
        if (adminCode == 1) {
        return franchiseOwnerRepository.findAllByOrderByFranchiseOwnerEnrollDateDesc().stream()
                .map(FranchiseOwnerDTO::new)
                .collect(Collectors.toList());
        }
        return franchiseOwnerRepository.findAllByFranchiseAdminAdminCodeOrderByFranchiseOwnerEnrollDateDesc(adminCode).stream()
                .map(FranchiseOwnerDTO::new)
                .collect(Collectors.toList());
    }

    // 상세 조회
    @Transactional(readOnly = true)
    @Override
    public ResponseEntity<FranchiseOwnerDTO> findFranchiseOwnerById(int franchiseOwnerCode) {
        try {
            FranchiseOwner franchiseOwner = franchiseOwnerRepository.findById(franchiseOwnerCode)
                    .orElseThrow(() -> new RuntimeException("프랜차이즈 오너 코드를 찾을 수 없음: " + franchiseOwnerCode));
            FranchiseOwnerDTO dto = new FranchiseOwnerDTO(franchiseOwner);
            return ResponseEntity.ok(dto);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // 오너 등록
    @Override
    @Transactional
    public ResponseEntity<String> registerFranchiseOwner(FranchiseOwnerDTO franchiseOwnerDTO) {
        if (!isCurrentUserRoot()) {
            return ResponseEntity.status(403).body("프랜차이즈 오너 등록은 루트 관리자만 가능합니다.");
        }

        if (franchiseOwnerDTO.getFranchiseOwnerId() == null || franchiseOwnerDTO.getFranchiseOwnerName() == null ||
                franchiseOwnerDTO.getFranchiseOwnerPwd() == null || franchiseOwnerDTO.getFranchiseOwnerEmail() == null ||
                franchiseOwnerDTO.getFranchiseOwnerPhone() == null) {
            return ResponseEntity.badRequest().body("필수 항목을 모두 입력해야 합니다.");
        }

        // ID 중복 확인
        if (franchiseOwnerRepository.existsByFranchiseOwnerId(franchiseOwnerDTO.getFranchiseOwnerId())) {
            return ResponseEntity.status(409).body("이미 존재하는 아이디입니다.");
        }

        // 비밀번호 암호화
        franchiseOwnerDTO.setFranchiseOwnerPwd(passwordEncoder.encode(franchiseOwnerDTO.getFranchiseOwnerPwd()));

        // 날짜 포맷터
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String now = LocalDateTime.now().format(formatter);

        // 등록일 및 수정일 설정
        franchiseOwnerDTO.setFranchiseOwnerEnrollDate(now);
        franchiseOwnerDTO.setFranchiseOwnerUpdateDate(now);

        FranchiseOwner franchiseOwner = FranchiseOwner.builder()
                .franchiseOwnerId(franchiseOwnerDTO.getFranchiseOwnerId())
                .franchiseOwnerName(franchiseOwnerDTO.getFranchiseOwnerName())
                .franchiseOwnerPwd(franchiseOwnerDTO.getFranchiseOwnerPwd())
                .franchiseOwnerEmail(franchiseOwnerDTO.getFranchiseOwnerEmail())
                .franchiseOwnerPhone(franchiseOwnerDTO.getFranchiseOwnerPhone())
                .franchiseOwnerEnrollDate(franchiseOwnerDTO.getFranchiseOwnerEnrollDate())
                .franchiseOwnerUpdateDate(franchiseOwnerDTO.getFranchiseOwnerUpdateDate())
                .franchiseOwnerStatus(franchiseOwnerDTO.isFranchiseOwnerStatus())
                .ownerPwdCheckCount(franchiseOwnerDTO.getOwnerPwdCheckCount())
                .franchiseRole(franchiseOwnerDTO.getFranchiseRole())
                .build();
        franchiseOwner.setFranchiseRole("ROLE_OWNER");
        franchiseOwnerRepository.save(franchiseOwner);
        String username = getCurrentUser();
        logService.saveLog(username, LogStatus.등록, franchiseOwner.getFranchiseOwnerName(), "FranchiseOwner");
        return ResponseEntity.ok("신규 프랜차이즈 오너 등록이 완료되었습니다.");
    }

    // 오너 정보 수정
    @Override
    @Transactional
    public ResponseEntity<String> updateFranchiseOwner(int franchiseOwnerCode, FranchiseOwnerDTO updatedFranchiseOwnerDTO) {
        FranchiseOwner existingFranchiseOwner = franchiseOwnerRepository.findById(franchiseOwnerCode)
                .orElseThrow(() -> new RuntimeException("프랜차이즈 오너 코드를 찾을 수 없음: " + franchiseOwnerCode));

        StringBuilder changes = new StringBuilder();

        if (!Objects.equals(existingFranchiseOwner.getFranchiseOwnerPwd(), updatedFranchiseOwnerDTO.getFranchiseOwnerPwd())) {
            changes.append(String.format("pwd 변경 '%s'에서 '%s'으로; ", existingFranchiseOwner.getFranchiseOwnerPwd(), updatedFranchiseOwnerDTO.getFranchiseOwnerPwd()));
            existingFranchiseOwner.setFranchiseOwnerPwd(passwordEncoder.encode(updatedFranchiseOwnerDTO.getFranchiseOwnerPwd()));
        }
        if (!Objects.equals(existingFranchiseOwner.getFranchiseOwnerPhone(), updatedFranchiseOwnerDTO.getFranchiseOwnerPhone())) {
            changes.append(String.format("phone 변경 '%s'에서 '%s'으로; ", existingFranchiseOwner.getFranchiseOwnerPhone(), updatedFranchiseOwnerDTO.getFranchiseOwnerPhone()));
            existingFranchiseOwner.setFranchiseOwnerPhone(updatedFranchiseOwnerDTO.getFranchiseOwnerPhone());
        }
        if (!Objects.equals(existingFranchiseOwner.getFranchiseOwnerEmail(), updatedFranchiseOwnerDTO.getFranchiseOwnerEmail())) {
            changes.append(String.format("Email 변경 '%s'에서 '%s'으로; ", existingFranchiseOwner.getFranchiseOwnerEmail(), updatedFranchiseOwnerDTO.getFranchiseOwnerEmail()));
            existingFranchiseOwner.setFranchiseOwnerEmail(updatedFranchiseOwnerDTO.getFranchiseOwnerEmail());
        }
        if (existingFranchiseOwner.isFranchiseOwnerStatus() != updatedFranchiseOwnerDTO.isFranchiseOwnerStatus()) {
            changes.append(String.format("Status 변경 '%s'에서 '%s'으로; ", existingFranchiseOwner.isFranchiseOwnerStatus(), updatedFranchiseOwnerDTO.isFranchiseOwnerStatus()));
            existingFranchiseOwner.setFranchiseOwnerStatus(updatedFranchiseOwnerDTO.isFranchiseOwnerStatus());
        }

        // 수정일 업데이트
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        existingFranchiseOwner.setFranchiseOwnerUpdateDate(LocalDateTime.now().format(formatter));

        franchiseOwnerRepository.save(existingFranchiseOwner);
        String username = getCurrentUser();
        if (changes.length() > 0) {
            logService.saveLog(username, LogStatus.수정, changes.toString(), "FranchiseOwner");
        }
        return ResponseEntity.ok("프랜차이즈 오너 정보가 성공적으로 업데이트되었습니다.");
    }

    // 오너 삭제 (비활성화)
    @Override
    @Transactional
    public ResponseEntity<String> deleteFranchiseOwner(int franchiseOwnerCode) {
        if (!isCurrentUserRoot()) {
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
        String username = getCurrentUser();
        logService.saveLog(username, LogStatus.삭제, existingFranchiseOwner.getFranchiseOwnerName(), "FranchiseOwner");
        return ResponseEntity.ok("프랜차이즈 오너가 성공적으로 삭제(비활성화)되었습니다.");
    }

    @Override
    @Transactional
    public ResponseEntity<String> resetFranchiseOwnerPassword(int franchiseOwnerCode) {
        if (!isCurrentUserRoot()) {
            return ResponseEntity.status(403).body("비밀번호 초기화는 루트 관리자만 가능합니다.");
        }

        FranchiseOwner franchiseOwner = franchiseOwnerRepository.findById(franchiseOwnerCode)
                .orElseThrow(() -> new RuntimeException("프랜차이즈 오너 코드를 찾을 수 없음: " + franchiseOwnerCode));

        String encodedPassword = passwordEncoder.encode("1234");
        franchiseOwner.setFranchiseOwnerPwd(encodedPassword);
        franchiseOwnerRepository.save(franchiseOwner);

        String username = getCurrentUser();
        logService.saveLog(username, LogStatus.수정, "비밀번호 초기화: " + franchiseOwner.getFranchiseOwnerName(), "FranchiseOwner");

        return ResponseEntity.ok("프랜차이즈 오너 비밀번호 초기화가 완료되었습니다.");
    }


    @Override
    public FranchiseOwnerDTO getFranchiseOwnerWithFranchiseName(int franchiseOwnerCode) {
        FranchiseOwner franchiseOwner = franchiseOwnerRepository.findByFranchiseOwnerCode(franchiseOwnerCode);
        if (franchiseOwner != null) {
            return new FranchiseOwnerDTO(franchiseOwner);
        } else {
            throw new EntityNotFoundException("Franchise owner not found with code: " + franchiseOwnerCode);
        }
    }

}
