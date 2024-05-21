package com.akatsuki.pioms.notice.service;

import com.akatsuki.pioms.admin.aggregate.Admin;
import com.akatsuki.pioms.notice.aggregate.Notice;
import com.akatsuki.pioms.notice.aggregate.NoticeVO;
import com.akatsuki.pioms.notice.repository.NoticeRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;


@SpringBootTest
@Transactional
public class NoticeServiceTest {

    @Autowired
    private NoticeService noticeService;

    @MockBean
    private NoticeRepository noticeRepository;

    Admin admin;
    int noticeCode = 1;

    @BeforeEach
    void init() {
        admin = new Admin();
        admin.setAdminCode(1);
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

//    @Test
//    @DisplayName(value = "공지사항 코드로 상세조회 실패 - 공지사항 없음")
//    void getNoticeByCode_NotFound() {
//
//        // given
//        given(noticeRepository.findById(noticeCode)).willReturn(Optional.empty());
//
//        //when
//        Throwable thrown = catchThrowable(() -> noticeService.getNoticeDetails(noticeCode));
//
//        //then
//        assertThat(thrown).isInstanceOf(RuntimeException.class).hasMessageContaning("해당 코드의 공지사항을 찾을 수 없습니다.");
//    }

    // 공지사항 등록
    @Test
    @DisplayName(value = "Root 관리자 권한으로 공지사항 등록 성공")
    void postNotice() {

        // given
        NoticeVO noticeVO = new NoticeVO(noticeCode, "공지사항을 새롭게 등록합니다.", "2024-01-11 12:34:45", "공지사항 등록에 대한 내용입니다.", "2024-01-11 12:34:45", "root");
        Notice notice = noticeVO.toDto();

        // when
        ResponseEntity<String> saveNoticeCode = noticeService.saveNotice(notice,1);

        // then
        Assertions.assertThat(notice.getNoticeCode()).isEqualTo(saveNoticeCode);
    }
    // 공지사항 수정
//    @Test
//    @DisplayName()
//    void updateNoticeByCode() {

        // given

        // when

        // then
//    }

    // 공지사항 삭제
    @Test
    void deleteNoticeByCode() {

        // given

        // when

        // then
    }
}
