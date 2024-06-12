package com.akatsuki.pioms.ask.controller;

import com.akatsuki.pioms.ask.dto.AskCreateDTO;
import com.akatsuki.pioms.ask.dto.AskDTO;
import com.akatsuki.pioms.ask.dto.AskListDTO;
import com.akatsuki.pioms.ask.dto.AskUpdateDTO;
import com.akatsuki.pioms.ask.aggregate.Ask;
import com.akatsuki.pioms.ask.service.AskService;
import com.akatsuki.pioms.config.GetUserInfo;
import com.akatsuki.pioms.frowner.dto.FranchiseOwnerDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
@Tag(name="[점주]문의사항 API", description = "Franchise Ask")
@RestController
@RequestMapping("/franchise")
public class AskFranchiseController {
    private final AskService askService;
    private final GetUserInfo getUserInfo;

    @Autowired
    public AskFranchiseController(AskService askService, GetUserInfo getUserInfo){this.askService = askService;
        this.getUserInfo = getUserInfo;
    }

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
//    @PostMapping("/ask/create/{franchise_owner_code}")
//    public ResponseEntity<AskDTO> createAsk(@PathVariable("franchise_owner_code") int franchiseOwnerCode, @RequestBody AskCreateDTO askCreateDTO) {
//        askCreateDTO.setFranchiseOwnerCode(franchiseOwnerCode);
//        AskDTO AskDTO = askService.createAsk(askCreateDTO);
//        return ResponseEntity.ok(AskDTO);
//    }

    @PostMapping("/create")
    @Operation(summary = "문의사항 생성", description = "문의사항 생성 입니다.")
    public ResponseEntity<AskDTO> createAsk(@RequestBody AskCreateDTO askCreateDTO) {
        int franchiseOwnerCode = getUserInfo.getFranchiseOwnerCode();
        askCreateDTO.setFranchiseOwnerCode(franchiseOwnerCode);
        AskDTO askDTO = askService.createAsk(askCreateDTO);
        return ResponseEntity.ok(askDTO);
    }

    /**
     * 문의사항 수정
     * */
    @PutMapping("/update/ask/{askCode}")
    @Operation(summary = "문의사항 수정", description = "")
    public ResponseEntity<?> updateAsk(@PathVariable int askCode, @RequestBody AskUpdateDTO askUpdateDTO) {
        try {
            Ask updatedAsk = askService.updateAsk(askCode, askUpdateDTO);
            AskDTO askDTO = new AskDTO(updatedAsk);
            return ResponseEntity.ok(askDTO);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }
    }

    @GetMapping("/asklist")
    @Operation(summary = "문의사항조회", description = "점주별 문의사항 전체 조회입니다.")
    public ResponseEntity<AskListDTO> getAsksByFranchiseOwner() {
        int franchiseOwnerId = getUserInfo.getFranchiseOwnerCode();  // 토큰에서 값 가져오기
        AskListDTO askListDTO = askService.getAsksByFranchiseOwnerId(franchiseOwnerId);
        return ResponseEntity.ok().body(askListDTO);
    }

    @GetMapping("/owner")
    @Operation(summary = "문의사항조회", description = "점주별 문의사항 상세 조회입니다.")
    public ResponseEntity<FranchiseOwnerDTO> getFranchiseOwnerDetails() {
        try {
            int franchiseOwnerCode = getUserInfo.getFranchiseOwnerCode();  // 토큰에서 값 가져오기
            FranchiseOwnerDTO franchiseOwnerDTO = askService.getFranchiseOwnerDetails(franchiseOwnerCode);
            return ResponseEntity.ok(franchiseOwnerDTO);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

}
