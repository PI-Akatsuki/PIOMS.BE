package com.akatsuki.pioms.ask.controller;

import com.akatsuki.pioms.ask.dto.AskCreateDTO;
import com.akatsuki.pioms.ask.dto.AskDTO;
import com.akatsuki.pioms.ask.dto.AskListDTO;
import com.akatsuki.pioms.ask.dto.AskUpdateDTO;
import com.akatsuki.pioms.ask.aggregate.Ask;
import com.akatsuki.pioms.ask.service.AskService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/franchise")
public class AskFranchiseController {
    AskService askService;

    public AskFranchiseController(AskService askService){this.askService = askService;}

    /**
     * 문의사항 전체조회
     * */
    @GetMapping("/ask/list")
    public ResponseEntity<AskListDTO> getAllAskList(){
        AskListDTO askListDTO = askService.getAllAskList();
        return ResponseEntity.ok().body(askListDTO);
    }

    /**
     * 문의사항 상세 조회
     * */
    @GetMapping("/ask/{askCode}")
    public ResponseEntity<AskDTO> getAskDetails(@PathVariable int askCode) {
        try {
            AskDTO askDTO = askService.getAskDetails(askCode);
            return ResponseEntity.ok(askDTO);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    /**
     * 문의사항 작성
     * */
    @PostMapping("/ask/create/{franchise_owner_code}")
    public ResponseEntity<AskDTO> createAsk(@PathVariable("franchise_owner_code") int franchiseOwnerCode, @RequestBody AskCreateDTO askDTO) {
        askDTO.setFranchiseOwnerCode(franchiseOwnerCode);
        AskDTO AskDTO = askService.createAsk(askDTO);
        return ResponseEntity.ok(AskDTO);
    }

    /**
     * 문의사항 수정
     * */
    @PutMapping("/update/{askCode}")
    public ResponseEntity<?> updateAsk(@PathVariable int askCode, @RequestBody AskUpdateDTO askUpdateDTO) {
        try {
            Ask updatedAsk = askService.updateAsk(askCode, askUpdateDTO);
            AskDTO askDTO = new AskDTO(updatedAsk);
            return ResponseEntity.ok(askDTO);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
    }

    /**
     * 문의사항 삭제
     * */

}
