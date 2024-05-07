package com.akatsuki.pioms.ask;

import com.akatsuki.pioms.ask.dto.AskCreateDTO;
import com.akatsuki.pioms.ask.entity.AskEntity;
import com.akatsuki.pioms.ask.service.AskService;
import com.akatsuki.pioms.ask.vo.AskVO;
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
    @PostMapping("/ask/create/{franchise_owner_code}")
    public ResponseEntity<AskVO> createAsk(@PathVariable("franchise_owner_code") int franchiseOwnerCode, @RequestBody AskCreateDTO askDTO) {
        askDTO.setFranchiseOwnerCode(franchiseOwnerCode);
        AskVO askVO = askService.createAsk(askDTO);
        return ResponseEntity.ok(askVO);
    }

    /**
     * 문의사항 수정
     * */

    /**
     * 문의사항 삭제
     * */

}
