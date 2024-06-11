package com.akatsuki.pioms.notice.service;

import com.akatsuki.pioms.admin.aggregate.Admin;
import com.akatsuki.pioms.admin.repository.AdminRepository;
import com.akatsuki.pioms.notice.aggregate.Notice;
import com.akatsuki.pioms.notice.aggregate.NoticeVO;
import com.akatsuki.pioms.notice.repository.NoticeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

@SpringBootTest
@Transactional
public class NoticeServiceTest {

    @InjectMocks
    private NoticeServiceImpl noticeService;

    @Mock
    private NoticeRepository noticeRepository;
    @Mock
    private AdminRepository adminRepository;


    Admin admin;


    @BeforeEach
    void init() {
        admin = Admin.builder()
                .adminCode(1)
                .adminId("root")
                .accessNumber("rootAccess")
                .adminStatus(true)
                .adminName("root")
                .adminEmail("root@example.com")
                .adminPhone("010-1234-5678")
                .enrollDate("2023-01-01 00:00:00")
                .updateDate("2023-01-01 00:00:00")
                .adminRole("ROLE_ROOT")
                .franchise(new ArrayList<>())
                .build();

        Notice notice = new Notice();
        notice.setNoticeTitle("공지사항 제목입니다.");
        notice.setNoticeContent("공지사항 내용입니다.");
        when(adminRepository.findById(1)).thenReturn(Optional.ofNullable(admin));
    }


    @Test
    @DisplayName(value = "공지사항 전체조회 성공")
    void getAllNoticeList() {

        // given
        List<Notice> noticeList = new LinkedList<>();
        noticeList.add(new Notice(11, "공지사항 제목 1", "2024-05-05 12:30:52", "공지사항1 내용입니다.", "2024-05-07 11:30:52", admin));
        noticeList.add(new Notice(12, "공지사항 제목 2", "2024-05-06 13:30:52", "공지사항2 내용입니다.", "2024-05-08 12:30:52", admin));
        noticeList.add(new Notice(13, "공지사항 제목 3", "2024-05-06 15:30:52", "공지사항3 내용입니다.", "2024-05-09 13:30:52", admin));

        given(noticeRepository.findAll()).willReturn(noticeList);

        // when
        List<NoticeVO> noticeVOS = noticeService.getAllNoticeList();

        // then
        assertEquals(noticeList.get(1).getNoticeContent(), noticeVOS.get(1).getNoticeContent());
    }

//    @Test
//    @DisplayName(value = "공지사항 코드로 상세조회 성공")
//    void getFindNoticeByCode() {
//
//        // given
//        int noticeCode = 2;
//        Notice savedNotice =new Notice(noticeCode, "발주 관련 공지 입니다.", "2024-03-11 14:55:02", "발주 하실 때 교환항목까지 담아주세요.", "2024-05-11 15:55:48", admin);
//        given(noticeRepository.findById(noticeCode)).willReturn(Optional.of(savedNotice));
//
//        // when
//        NoticeVO findNotice = noticeService.getNoticeDetails(noticeCode);
//
//        // then
//        assertEquals(savedNotice.getNoticeTitle(), findNotice.getNoticeTitle());
//        assertEquals(savedNotice.getNoticeContent(), findNotice.getNoticeContent());
//        assertEquals(savedNotice.getNoticeEnrollDate(), findNotice.getNoticeEnrollDate());
//    }

    @Test
    @DisplayName(value = "공지사항 코드로 상세조회 성공")
    void getFindNoticeByCode() {

        // given
        int noticeCode = 2;
        Notice savedNotice =new Notice(noticeCode, "발주 관련 공지 입니다.", "2024-03-11 14:55:02", "발주 하실 때 교환항목까지 담아주세요.", "2024-05-11 15:55:48", admin);
        given(noticeRepository.findById(noticeCode)).willReturn(Optional.of(savedNotice));

        // when
        NoticeVO findNotice = noticeService.getNoticeDetails(noticeCode);

        // then
        assertEquals(savedNotice.getNoticeTitle(), findNotice.getNoticeTitle());
        assertEquals(savedNotice.getNoticeContent(), findNotice.getNoticeContent());
        assertEquals(savedNotice.getNoticeEnrollDate(), findNotice.getNoticeEnrollDate());
    }

    // 공지사항 등록
    @Test
    @DisplayName(value = "Root 관리자 권한으로 공지사항 등록 성공")
    void postNotice() {

        // given
        Notice notice = new Notice();
        notice.setNoticeTitle("공지사항 제목입니다.");
        notice.setNoticeContent("공지사항 내용입니다.");


        // when
        // root 관리자로 공지 등록
        ResponseEntity<String> responseRoot = noticeService.saveNotice(notice,1);

        // then
        assertNotNull(responseRoot);
        assertEquals(200, responseRoot.getStatusCodeValue());
        assertEquals("공지사항 등록이 완료되었습니다.", responseRoot.getBody());
    }

    @Test
    @DisplayName(value = "Root 관리자 권한으로 공지사항 수정 성공")
    void updateNotice() {
        // given
        Notice notice1 = new Notice();
        notice1.setNoticeCode(1);
        notice1.setNoticeTitle("공지사항 제목입니다.");
        notice1.setNoticeEnrollDate("2024-03-11 14:55:02");
        notice1.setNoticeContent("공지사항 내용입니다.");
        notice1.setNoticeUpdateDate("2024-03-11 14:55:02");
        when(noticeRepository.findById(1)).thenReturn(Optional.of(notice1));

        // when
        notice1.setNoticeTitle("공지사항 제목을 수정합니다.");
        notice1.setNoticeUpdateDate("2024-03-12 14:22:02");
        notice1.setNoticeContent("공지사항 내용을 수정합니다.");

        ResponseEntity<String> result = noticeService.updateNotice(notice1, notice1.getNoticeCode(), 1);

        // then
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals("공지사항이 수정되었습니다.", result.getBody());
    }




    // 공지사항 삭제
    @Test
    @DisplayName(value = "Root 관리자 권한으로 공지사항 삭제 성공")
    void deleteNoticeByCode() {

        // given
        Notice notice = new Notice();
        notice.setNoticeCode(1);
        notice.setNoticeTitle("공지사항 제목");
        notice.setNoticeEnrollDate("2024-05-21 11:23:45");
        notice.setNoticeContent("공지사항 내요오오오오오옹");
        notice.setNoticeUpdateDate("2024-05-21 11:23:45");

        when(noticeRepository.findById(1)).thenReturn(Optional.of(notice));

        // when
        noticeService.deleteNotice(1, 1);

        ResponseEntity<String> result = noticeService.deleteNotice(1, 1);

        // then
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals("공지사항이 삭제되었습니다.", result.getBody());
    }
}
