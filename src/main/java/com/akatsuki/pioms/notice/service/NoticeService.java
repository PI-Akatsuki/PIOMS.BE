package com.akatsuki.pioms.notice.service;

import com.akatsuki.pioms.notice.aggregate.Notice;
import com.akatsuki.pioms.notice.aggregate.NoticeVO;
import org.springframework.http.ResponseEntity;

import java.util.List;

/* 설명. 공지사항 - 관리자
 *    1. 공지사항 조회
 *  1-1. 관리자 공지사항 전체 조회(admin/notice/list)
 *  1-2. 관리자 공지사항 상세 조회(admin/notice/details/{noticeCode})
 *    2. root 관리자가 공지사항 등록(admin/notice/register)
 *    3. root 관리자가 공지사항 수정
 *    4. root 관리자가 공지사항 삭제
 *
 * 설명. 공지사항 - 점주
 *    1. 공지사항 전체 조회(franchise/notice/list)
 *    2. 공지사항 상세 조회(franchise/notice/details/{noticeCode})*/
public interface NoticeService {

    // 관리자가 작성한 공지사항 조회(admin/notice/list)
    List<NoticeVO> getAllNoticeList();


    // 공지사항 noticeCode로 상세 조회
    NoticeVO getNoticeDetails(int noticeCode);

    // 공지사항 등록
    ResponseEntity<String> saveNotice(Notice notice, int requesterAdminCode);

    // root 관리자가 작성한 공지사항 수정
    ResponseEntity<String> updateNotice(Notice updatedNotice, int noticeCode, int requesterAdminCode);


    // root 관리자가 공지사항 삭제
    ResponseEntity<String> deleteNotice(int noticeCode, int requesterAdminCode);
}
