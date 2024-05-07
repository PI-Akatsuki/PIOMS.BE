package com.akatsuki.pioms.ask;

import com.akatsuki.pioms.ask.service.AskService;
import com.akatsuki.pioms.ask.vo.AskListVO;
import com.akatsuki.pioms.ask.vo.AskVO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * <h1>문의사항 API</h1>
 * <br>
 * <h2>공통</h2>
 * 1. 상세 조회(/ask/{askId})<br>
 * 2.

 * <br><h2>관리자</h2>
 * 1. 모든 문의사항 목록 조회(admin/ask/list)<br>
 * 2. 문의사항 답변(ask/reply/{askId})<br>

 * <br><h2>점주</h2>
 * 1. 문의사항 목록 조회(ask/franchise/{franchiseId}/asks)<br>
 * 2. 문의사항 작성(ask/franchise/asks/write)<br>
 * 3. 문의사항 삭제(ask/franchise/asks/delete)<br>
 * */

@RestController
@RequestMapping("/admin")
public class AskAdminController {
    AskService askService;

    public AskAdminController(AskService askService){this.askService = askService;}

    /**
    * 모든 문의사항 목록 조회
    * */
    @GetMapping("/ask/list")
    public ResponseEntity<AskListVO> getAllAskList(){
        AskListVO askListVO = askService.getAllAskList();
        return ResponseEntity.ok().body(askListVO);
    }

    /**
     * 답변대기 문의사항 목록 조회
     * */
    @GetMapping("/ask/waiting")
    public ResponseEntity<AskListVO> getWaitingForReplyAsks() {
        AskListVO askListVO = askService.getWaitingForReplyAsks();
        return ResponseEntity.ok().body(askListVO);
    }


}
