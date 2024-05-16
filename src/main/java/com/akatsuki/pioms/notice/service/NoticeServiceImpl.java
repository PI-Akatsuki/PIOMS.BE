package com.akatsuki.pioms.notice.service;

import com.akatsuki.pioms.admin.aggregate.Admin;
import com.akatsuki.pioms.admin.repository.AdminRepository;
import com.akatsuki.pioms.notice.aggregate.Notice;
import com.akatsuki.pioms.notice.aggregate.NoticeVO;
import com.akatsuki.pioms.notice.repository.NoticeRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
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

    // 공지사항 상세 조회
    @Override
    @Transactional(readOnly = true)
    public NoticeVO getNoticeDetails(int noticeCode) {
        Notice notice = noticeRepository.findById(noticeCode)
                .orElseThrow(() -> new EntityNotFoundException("입력하신 공지사항 목록을 찾을 수 없습니다."));
        return new NoticeVO(notice);
    }

    // root관리자가 공지사항 등록
    @Override
    @Transactional
    public ResponseEntity<String> saveNotice(Notice notice, int requesterAdminCode) {
        try {
            Admin requesterAdmin = adminRepository.findById(requesterAdminCode)
                    .orElseThrow(() -> new RuntimeException("root 관리자를 찾을 수 없습니다." + requesterAdminCode));

            // root 관리자가 아닐 경우 관리자 코드 확인
            if (requesterAdmin == null || requesterAdmin.getAdminCode() != 1) {
                return ResponseEntity.status(403).body("공지사항 등록은 root 관리자만 가능합니다.");
            }

            // 공지사항 필수 항목 확인
            if (notice.getNoticeTitle() == null || notice.getNoticeContent() == null) {
                return ResponseEntity.badRequest().body("공지사항 작성 시 필수 항목을 모두 입력해야 합니다.");
            }

            // 날짜 포맷터로 등록일, 수정일 설정
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String now = LocalDateTime.now().format(formatter);

            // 등록일 및 수정일 설정
            notice.setNoticeEnrollDate(now);
            notice.setNoticeUpdateDate(now);

            // notice엔티티에 admin 주입
            notice.setAdmin(requesterAdmin);

            // 공지사항 저장
            noticeRepository.save(notice);
            return ResponseEntity.ok("공지사항 등록이 완료되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("서버에 오류가 생겼습니다.");
        }
    }

    // root 관리자가 작성한 공지사항 수정
    @Override
    @Transactional
    public ResponseEntity<String> updateNotice(Notice updatedNotice, int noticeCode, int requesterAdminCode) {
        try {
            // 요청한 관리자가 존재하는지 확인
            Admin requesterAdmin = adminRepository.findById(requesterAdminCode)
                    .orElseThrow(() -> new RuntimeException("root 관리자를 찾을 수 없습니다." + requesterAdminCode));

            // root 관리자가 아닐 경우 수정 불가
            if (requesterAdmin == null || requesterAdmin.getAdminCode() != 1) {
                return ResponseEntity.status(403).body("공지사항 수정은 root 관리자만 가능합니다.");
            }

            // 공지사항 필수 항목 확인
            if (updatedNotice.getNoticeTitle() == null || updatedNotice.getNoticeContent() == null) {
                return ResponseEntity.badRequest().body("공지사항 수정 후 비어았는 곳이 없는지 확인해주세요.");
            }

            // 수정하려는 공지사항이 존재하는지 확인
            Notice existingNotice = noticeRepository.findById(noticeCode)
                    .orElseThrow(() -> new RuntimeException("해당 코드의 공지사항을 찾을 수 없습니다!" + noticeCode));

            // 날짜 포맷터로 수정일 설정
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String now = LocalDateTime.now().format(formatter);

            // 수정일 및 공지사항 내용 업데이트
            existingNotice.setNoticeTitle(updatedNotice.getNoticeTitle());
            existingNotice.setNoticeContent(updatedNotice.getNoticeContent());
            existingNotice.setNoticeUpdateDate(now);

            // Notice 엔티티에 생성자 admin 주입
            existingNotice.setAdmin(requesterAdmin);

            // 공지사항 수정사항 저장
            noticeRepository.save(existingNotice);
            return ResponseEntity.ok("공지사항이 수정되었습니다.");

            // 기타 예외 처리
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("서버에 오류가 생겼습니다.");
        }
    }

    // root 관리자가 공지사항 삭제
    @Override
    @Transactional
    public ResponseEntity<String> deleteNotice(int noticeCode, int requesterAdminCode) {
        try {
            // 요청한 관리자가 존재하는지 확인
            Admin requesterAdmin = adminRepository.findById(requesterAdminCode)
                    .orElseThrow(() -> new RuntimeException("root 관리자를 찾을 수 없습니다." + requesterAdminCode));

            // root 관리자가 아닐 경우 삭제 불가
            if (requesterAdmin == null || requesterAdmin.getAdminCode() != 1) {
                return ResponseEntity.status(403).body("공지사항 삭제는 root 관리자만 가능합니다.");
            }

            // 삭제하려는 공지사항이 존재하는지 확인
            Notice existingNotice = noticeRepository.findById(noticeCode)
                    .orElseThrow(() -> new RuntimeException("해당 코드의 공지사항을 찾을 수 없습니다!" + noticeCode));

            // Notice 엔티티에 생성자 admin 주입
            existingNotice.setAdmin(requesterAdmin);

            // 공지사항 삭제
            noticeRepository.delete(existingNotice);
            return ResponseEntity.ok("공지사항이 삭제되었습니다.");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("서버에 오류가 생겼습니다.");
        }
    }
}
