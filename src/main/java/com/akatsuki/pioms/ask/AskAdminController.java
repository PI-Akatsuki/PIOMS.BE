package com.akatsuki.pioms.ask;

import com.akatsuki.pioms.ask.service.AskService;
import com.akatsuki.pioms.ask.vo.AskListVO;
import com.akatsuki.pioms.ask.vo.AskVO;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
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
    /**
     * 점주 별 작성한 문의사항 전체 조회
     * */
    @GetMapping("/ask/franchise/{franchiseOwnerId}")
    public ResponseEntity<AskListVO> getAsksByFranchiseOwnerId(@PathVariable Integer franchiseOwnerId) {
        AskListVO askListVO = askService.getAsksByFranchiseOwnerId(franchiseOwnerId);
        return ResponseEntity.ok().body(askListVO);
    }
    /**
     * 문의사항 상세 조회
     * */
    @GetMapping("/ask/{askCode}")
    public ResponseEntity<AskVO> getAskDetails(@PathVariable int askCode) {
        try {
            AskVO askVO = askService.getAskDetails(askCode);
            return ResponseEntity.ok(askVO);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }



    /**
     * 답변작성후 상태 '답변완료'변경
     * */
    @PostMapping("/ask/answer/{askId}")
    public ResponseEntity<AskVO> answerAsk(@PathVariable Integer askId, @RequestBody String answer) {
        AskVO askVO = askService.answerAsk(askId, answer);
        return ResponseEntity.ok(askVO);
    }


}
