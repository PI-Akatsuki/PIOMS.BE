package com.akatsuki.pioms.notice.service;


import com.akatsuki.pioms.notice.aggregate.Notice;
import org.springframework.http.ResponseEntity;

import java.util.List;

/* 설명. 공지사항 
 *    1. 관리자가 작성한 공지사항 조회(admin/notice/list)
 *    2. 관리자가 공지사항 등록(admin/notice/register)*/
public interface NoticeService {

    // 관리자가 작성한 공지사항 조회(admin/notice/list)
    List<Notice> getAllNoticeList();

    // 관리자가 공지사항 등록(admin/notice/register)
    ResponseEntity<String> saveNotice(Notice notice, int requestorAdminCode);

}
