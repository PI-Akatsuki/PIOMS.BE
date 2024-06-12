package com.akatsuki.pioms.notice.controller;

import com.akatsuki.pioms.notice.aggregate.Notice;
import com.akatsuki.pioms.notice.aggregate.NoticeVO;
import com.akatsuki.pioms.notice.service.NoticeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.modelmapper.internal.bytebuddy.build.Plugin;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// 관리자 공지사항 페이지
@RestController
@RequestMapping("admin")
@Tag(name = "[관리자]공지사항 API")
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
    @Operation(summary = "공지사항 상세 목록 조회", description = "공지사항 코드로 공지사항 상세 목록 조회")
    @GetMapping("/notice/list/details/{noticeCode}")
    public ResponseEntity<NoticeVO> getNoticeDetails(@PathVariable int noticeCode) {
        NoticeVO noticeVO = noticeService.getNoticeDetails(noticeCode);
        return ResponseEntity.ok(noticeVO);
    }

    // root 관리자가 공지사항 등록
    @Operation(summary = "공지사항 등록", description = "root 관리자 권한으로 공지사항 등록")
    @PostMapping("/notice/list/register")
    public ResponseEntity<String> registerNotice (
            @RequestBody Notice notice,
            @RequestParam int requesterAdminCode
    ) {
        return noticeService.saveNotice(notice, requesterAdminCode);
    }

    // root 관리자가 공지사항 수정
    @Operation(summary = "공지사항 수정", description = "공지사항 코드로 root 관리자가 공지사항 수정")
    @PutMapping("/notice/list/update/{noticeCode}")
    public ResponseEntity<String> updateNotice(
            @PathVariable int noticeCode,
            @RequestBody Notice updatedNotice,
            @RequestParam int requesterAdminCode) {
        return noticeService.updateNotice(updatedNotice, noticeCode, requesterAdminCode);
    }

    // root 관리자가 공지사항 삭제
    @Operation(summary = "공지사항 삭제", description = "공지사항 코드로 root 관리자가 공지사항 삭제")
    @DeleteMapping("/notice/list/delete/{noticeCode}")
    public ResponseEntity<String> deleteNotice(@PathVariable int noticeCode, int requesterAdminCode){
        return noticeService.deleteNotice(noticeCode, requesterAdminCode);
    }
}
