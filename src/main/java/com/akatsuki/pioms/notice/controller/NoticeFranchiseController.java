package com.akatsuki.pioms.notice.controller;

import com.akatsuki.pioms.admin.repository.AdminRepository;
import com.akatsuki.pioms.notice.aggregate.Notice;
import com.akatsuki.pioms.notice.repository.NoticeRepository;
import com.akatsuki.pioms.notice.service.NoticeService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

// 점주 공지사항 페이지
@RestController
@RequestMapping("franchise")
public class NoticeFranchiseController {

    private final NoticeService noticeService;

    public NoticeFranchiseController(NoticeService noticeService) {
        this.noticeService = noticeService;
    }


    // 점주 공지사항 전체 조회
    @Operation(summary = "공지사항 전체 목록 조회", description = "공지사항 전체 목록 조회")
    @GetMapping("/notice/view")
    public ResponseEntity<List<Notice>> getAllNoticeList() {
        return ResponseEntity.ok().body(noticeService.getAllNoticeList());
    }
}
