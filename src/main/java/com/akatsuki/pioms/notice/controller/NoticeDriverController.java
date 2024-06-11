package com.akatsuki.pioms.notice.controller;

import com.akatsuki.pioms.notice.aggregate.NoticeVO;
import com.akatsuki.pioms.notice.service.NoticeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

// 점주 공지사항 페이지
@RestController
@RequestMapping("driver")
@Tag(name = "[배송기사]공지사항 API")
public class NoticeDriverController {

    private final NoticeService noticeService;

    @Autowired
    public NoticeDriverController(NoticeService noticeService) {
        this.noticeService = noticeService;
    }


    // 점주 공지사항 전체 조회
    @Operation(summary = "공지사항 전체 목록 조회", description = "공지사항 전체 목록 조회")
    @GetMapping("/notice/list")
    public ResponseEntity<List<NoticeVO>> getAllNoticeList() {
        return ResponseEntity.ok().body(noticeService.getAllNoticeList());
    }

    // 점주 공지사항 상세 조회
    @Operation(summary = "공지사항 상세 목록 조회", description = "공지사항 상세 목록 조회")
    @GetMapping("/notice/list/details/{noticeCode}")
    public ResponseEntity<NoticeVO> getNoticeDetails(@PathVariable int noticeCode) {
        NoticeVO noticeVO = noticeService.getNoticeDetails(noticeCode);
        return ResponseEntity.ok(noticeVO);
    }
}
