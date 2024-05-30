package com.akatsuki.pioms.ask.controller;

import com.akatsuki.pioms.ask.dto.AskCreateDTO;
import com.akatsuki.pioms.ask.dto.AskDTO;
import com.akatsuki.pioms.ask.dto.AskListDTO;
import com.akatsuki.pioms.ask.dto.AskUpdateDTO;
import com.akatsuki.pioms.ask.aggregate.Ask;
import com.akatsuki.pioms.ask.service.AskService;
import com.akatsuki.pioms.frowner.dto.FranchiseOwnerDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
@Tag(name="문의사항", description = "Franchise Ask")
@RestController
@RequestMapping("/franchise")
public class AskFranchiseController {
    AskService askService;

    public AskFranchiseController(AskService askService){this.askService = askService;}

    /**
     * 문의사항 전체조회
     * */
    @Operation(summary = "문의사항조회", description = "문의사항 전체 조회입니다.")
    @GetMapping("/ask/list")
    public ResponseEntity<AskListDTO> getAllAskList(){
        AskListDTO askListDTO = askService.getAllAskList();
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

    @Operation(summary = "문의사항조회", description = "점주별 문의사항 전체 조회입니다.")
    @GetMapping("/asklist/{franchiseOwnerId}")
    public ResponseEntity<AskListDTO> getAsksByFranchiseOwnerId(@PathVariable Integer franchiseOwnerId) {
        AskListDTO askListDTO = askService.getAsksByFranchiseOwnerId(franchiseOwnerId);
        return ResponseEntity.ok().body(askListDTO);
    }

    @Operation(summary = "문의사항조회", description = "점주별 문의사항 상세 조회입니다.")
    @GetMapping("/owner/{franchiseOwnerCode}")
    public ResponseEntity<FranchiseOwnerDTO> getFranchiseOwnerDetails(@PathVariable int franchiseOwnerCode) {
        try {
            FranchiseOwnerDTO franchiseOwnerDTO = askService.getFranchiseOwnerDetails(franchiseOwnerCode);
            return ResponseEntity.ok(franchiseOwnerDTO);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    /**
     * 문의사항 삭제
     * */

}
