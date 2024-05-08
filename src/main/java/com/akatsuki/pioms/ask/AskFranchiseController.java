package com.akatsuki.pioms.ask;

import com.akatsuki.pioms.ask.dto.AskCreateDTO;
import com.akatsuki.pioms.ask.dto.AskUpdateDTO;
import com.akatsuki.pioms.ask.entity.AskEntity;
import com.akatsuki.pioms.ask.service.AskService;
import com.akatsuki.pioms.ask.vo.AskListVO;
import com.akatsuki.pioms.ask.vo.AskVO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/franchise")
public class AskFranchiseController {
    AskService askService;

    public AskFranchiseController(AskService askService){this.askService = askService;}

    /**
     * 문의사항 작성
     * */
    @GetMapping("/ask/list")
    public ResponseEntity<AskListVO> getAllAskList(){
        AskListVO askListVO = askService.getAllAskList();
        return ResponseEntity.ok().body(askListVO);
    }

    /**
     * 문의사항 작성
     * */
    @PostMapping("/ask/create/{franchise_owner_code}")
    public ResponseEntity<AskVO> createAsk(@PathVariable("franchise_owner_code") int franchiseOwnerCode, @RequestBody AskCreateDTO askDTO) {
        askDTO.setFranchiseOwnerCode(franchiseOwnerCode);
        AskVO askVO = askService.createAsk(askDTO);
        return ResponseEntity.ok(askVO);
    }

    /**
     * 문의사항 수정
     * */
    @PutMapping("/update/{askCode}")
    public ResponseEntity<?> updateAsk(@PathVariable int askCode, @RequestBody AskUpdateDTO askUpdateDTO) {
        try {
            AskEntity updatedAsk = askService.updateAsk(askCode, askUpdateDTO);
            AskVO askVO = new AskVO(updatedAsk);
            return ResponseEntity.ok(askVO);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
    }

    /**
     * 문의사항 삭제
     * */

}
