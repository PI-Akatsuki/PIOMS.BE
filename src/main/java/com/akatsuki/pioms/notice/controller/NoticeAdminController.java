package com.akatsuki.pioms.notice.controller;

import com.akatsuki.pioms.notice.aggregate.Notice;
import com.akatsuki.pioms.notice.aggregate.NoticeVO;
import com.akatsuki.pioms.notice.dto.NoticeDTO;
import com.akatsuki.pioms.notice.service.NoticeService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// 관리자 공지사항 페이지
@RestController
@RequestMapping("admin")
public class NoticeAdminController {

    private final NoticeService noticeService;

    public NoticeAdminController(NoticeService noticeService) {
        this.noticeService = noticeService;
    }

    // 관리자 공지사항 전체 목록 조회
    @Operation(summary = "공지사항 전체 목록 조회", description = "공지사항 전체 목록 조회")
    @GetMapping("/notice/list")
    public ResponseEntity<List<NoticeVO>> getAllNoticeList() {
        return ResponseEntity.ok().body(noticeService.getAllNoticeList());
    }

    // 관리자 공지사항 상세 목록 조회
    @Operation(summary = "공지사항 상세 목록 조회", description = "공지사항 상세 목록 조회")
    @GetMapping("/notice/details/{noticeCode}")
    public ResponseEntity<NoticeVO> getNoticeDetails(@PathVariable int noticeCode) {
        NoticeVO noticeVO = noticeService.getNoticeDetails(noticeCode);
        return ResponseEntity.ok(noticeVO);
    }


    // 관리자가 공지사항 등록
    @Operation(summary = "공지사항 등록", description = "notice register")
    @PostMapping("/notice/register")
    public ResponseEntity<String> registerNotice (
            @RequestBody Notice notice,
            @RequestParam int requestorAdminCode
    ) {
        return noticeService.saveNotice(notice, requestorAdminCode);
    }

}
