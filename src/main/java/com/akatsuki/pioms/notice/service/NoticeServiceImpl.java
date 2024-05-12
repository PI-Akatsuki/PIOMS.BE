package com.akatsuki.pioms.notice.service;

import com.akatsuki.pioms.admin.aggregate.Admin;
import com.akatsuki.pioms.admin.repository.AdminRepository;
import com.akatsuki.pioms.notice.aggregate.Notice;
import com.akatsuki.pioms.notice.repository.NoticeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;


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

    // 필요한 값만 가져오는데 null뜬다.
//    @Override
//    public List<NoticeVO> getAllNoticeList() {
//        return noticeRepository.findAll().stream()
//                .map(NoticeVO::new)
//                .collect(Collectors.toList());
//    }

    // 필요한 값이랑 admin정보까지 다 가져온다.
    @Transactional(readOnly = true)
    @Override
    public List<Notice> getAllNoticeList() {
        return noticeRepository.findAll();
    }

    // 공지사항 등록
    @Override
    @Transactional
    public ResponseEntity<String> saveNotice(Notice notice, int requestorAdminCode) {
        // 루트 관리자 확인
        Optional<Admin> reqeustorAdmin = adminRepository.findById(requestorAdminCode);
        if (reqeustorAdmin.isEmpty() || reqeustorAdmin.get().getAdminCode() !=1) {
            return ResponseEntity.status(403).body("공지사항 등록은 루트 관리자만 가능합니다.");
        }

        // 필수 필드 확인
        if (notice.getNoticeTitle() == null || notice.getNoticeContent() == null) {
            return ResponseEntity.badRequest().body("공지사항 제목과 내용은 필수 항목입니다. 모두 입력해주셔야 합니다!");
        }

        // 날짜 포맷터
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String now = LocalDateTime.now().format(formatter);

        // 등록일 설정
        notice.setNoticeEnrollDate(now);

        //수정일 설정
        notice.setNoticeUpdateDate(now);

        noticeRepository.save(notice);
        return ResponseEntity.ok("공지사항 등록이 완료되었습니다.");
    }
}
