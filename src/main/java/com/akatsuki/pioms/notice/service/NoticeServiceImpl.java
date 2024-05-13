package com.akatsuki.pioms.notice.service;

import com.akatsuki.pioms.admin.aggregate.Admin;
import com.akatsuki.pioms.admin.repository.AdminRepository;
import com.akatsuki.pioms.notice.aggregate.Notice;
import com.akatsuki.pioms.notice.aggregate.NoticeVO;
import com.akatsuki.pioms.notice.repository.NoticeRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.UnexpectedRollbackException;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;


@Service
public class NoticeServiceImpl implements NoticeService {

    private final NoticeRepository noticeRepository;
    private final AdminRepository adminRepository;

    @Autowired
    public NoticeServiceImpl(NoticeRepository noticeRepository, AdminRepository adminRepository) {
        this.noticeRepository = noticeRepository;
        this.adminRepository = adminRepository;
    }

    // 관리자 공지사항 전체 조회
    @Override
    @Transactional(readOnly = true)
    public List<NoticeVO> getAllNoticeList() {
        List<Notice> noticeList = noticeRepository.findAll();
        List<NoticeVO> noticeVOS = new ArrayList<>();
        noticeList.forEach(
                notice -> {
                    noticeVOS.add(new NoticeVO(notice));
                }
        );
        return noticeVOS;
    }

    // 공자사항 상세 조회
    @Override
    @Transactional(readOnly = true)
    public NoticeVO getNoticeDetails(int noticeCode) {
        Notice notice = noticeRepository.findById(noticeCode)
                .orElseThrow(() -> new EntityNotFoundException("입력하신 공지사항 목록을 찾을 수 없습니다."));
        return new NoticeVO(notice);
    }

    @Override
    @Transactional
    public ResponseEntity<String> saveNotice(Notice notice, int requestorAdminCode) {
        try {
            Admin requestorAdmin = adminRepository.findById(requestorAdminCode)
                    .orElseThrow(() -> new RuntimeException("찾을 수 없음" + requestorAdminCode));
            if (requestorAdmin == null || requestorAdmin.getAdminCode() != 1) {
                return ResponseEntity.status(403).body("공지사항 등록은 루트 관리자만 가능합니다.");
            }

            // 공지사항 필수 항목 확인
            if (notice.getNoticeTitle() == null || notice.getNoticeContent() == null) {
                return ResponseEntity.badRequest().body("공지사항 작성 시 필수 항목을 모두 입력해야 합니다.");
            }

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String now = LocalDateTime.now().format(formatter);

            // 등록일 및 수정일 설정
            notice.setNoticeEnrollDate(now);
            notice.setNoticeUpdateDate(now);

            // notice엔티티에 admin 주입
            notice.setAdmin(requestorAdmin);

            // 공지사항 저장
            noticeRepository.save(notice);
            return ResponseEntity.ok("공지사항 등록이 완료되었습니다.");
            // 기타 오류 처리
        } catch (Exception e) {
            return ResponseEntity.status(500).body("서버에 오류가 생겼습니다.");
        }

    }


}
