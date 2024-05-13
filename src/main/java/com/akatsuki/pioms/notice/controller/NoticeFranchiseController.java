package com.akatsuki.pioms.notice.controller;

import com.akatsuki.pioms.notice.aggregate.NoticeVO;
import com.akatsuki.pioms.notice.service.NoticeService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
    public ResponseEntity<List<NoticeVO>> getAllNoticeList() {
        return ResponseEntity.ok().body(noticeService.getAllNoticeList());
    }

    // 점주 공지사항 상세 조회
    @Operation(summary = "공지사항 상세 목록 조회", description = "공지사항 상세 목록 조회")
    @GetMapping("/notice/{noticeCode}")
    public ResponseEntity<NoticeVO> getNoticeDetails(@PathVariable int noticeCode) {
       NoticeVO noticeVO = noticeService.getNoticeDetails(noticeCode);
       return ResponseEntity.ok(noticeVO);
    }
}
