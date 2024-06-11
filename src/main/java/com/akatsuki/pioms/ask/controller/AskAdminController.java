package com.akatsuki.pioms.ask.controller;

import com.akatsuki.pioms.ask.dto.AskDTO;
import com.akatsuki.pioms.ask.dto.AskListDTO;
import com.akatsuki.pioms.ask.service.AskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

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
@Tag(name="[관리자]문의사항 API", description = "Admin Ask")
@RestController
@RequestMapping("/admin")
public class AskAdminController {
    private final AskService askService;

    @Autowired
    public AskAdminController(AskService askService){this.askService = askService;}

    /**
     * 모든 문의사항 목록 조회
     * */
    @Operation(summary = "문의사항조회", description = "문의사항 전체 조회입니다.")
    @GetMapping("/ask/list")
    public ResponseEntity<AskListDTO> getAllAskList(){
        AskListDTO askListDTO = askService.getAllAskList();
        return ResponseEntity.ok().body(askListDTO);
    }

    /**
     * 답변대기 문의사항 목록 조회
     * */
    @Operation(summary = "문의사항조회", description = "답변대기상태 문의사항 조회입니다.")
    @GetMapping("/ask/waiting")
    public ResponseEntity<AskListDTO> getWaitingForReplyAsks() {
        AskListDTO askListDTO = askService.getWaitingForReplyAsks();
        return ResponseEntity.ok().body(askListDTO);
    }
    /**
     * 점주 별 작성한 문의사항 전체 조회
     * */
    @Operation(summary = "문의사항조회", description = "점주별 작성 문의사항 전체 조회입니다.")
    @GetMapping("/ask/franchise/{franchiseOwnerId}")
    public ResponseEntity<AskListDTO> getAsksByFranchiseOwnerId(@PathVariable Integer franchiseOwnerId) {
        AskListDTO askListDTO = askService.getAsksByFranchiseOwnerId(franchiseOwnerId);
        return ResponseEntity.ok().body(askListDTO);
    }
    /**
     * 문의사항 상세 조회
     * */
    @Operation(summary = "문의사항조회", description = "문의사항 상세 조회입니다.")
    @GetMapping("/ask/{askCode}")
    public ResponseEntity<AskDTO> getAskDetails(@PathVariable int askCode) {
        try {
            AskDTO askDTO = askService.getAskDetails(askCode);
            return ResponseEntity.ok(askDTO);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Ask not found", e);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error", e);
        }
    }



    /**
     * 답변작성후 상태 '답변완료'변경
     * */
    @PostMapping("/ask/answer/{askId}")
    @Operation(summary = "문의사항등록", description = "문의사항 등록입니다. 이 때 답변 완료 상태로 변경합니다.")
    public ResponseEntity<?> answerAsk(@PathVariable Integer askId, @RequestBody Map<String, String> payload) {
        try {
            String answer = payload.get("answer");
            if (answer == null || answer.isEmpty()) {
                return ResponseEntity.badRequest().body("Answer cannot be empty");
            }

            AskDTO askDTO = askService.answerAsk(askId, answer);
            return ResponseEntity.ok(askDTO);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Ask not found");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal server error");
        }
    }



}
