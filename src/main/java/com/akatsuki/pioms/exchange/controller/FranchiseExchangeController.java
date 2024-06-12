package com.akatsuki.pioms.exchange.controller;

import com.akatsuki.pioms.exchange.aggregate.RequestExchange;
import com.akatsuki.pioms.exchange.aggregate.ResponseExchange;
import com.akatsuki.pioms.exchange.dto.ExchangeDTO;
import com.akatsuki.pioms.exchange.service.ExchangeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("franchise")
@Tag(name = "[점주]반품 API")
public class FranchiseExchangeController {
    ExchangeService exchangeService;

    @Autowired
    public FranchiseExchangeController(ExchangeService exchangeService) {
        this.exchangeService = exchangeService;
    }

    @GetMapping("/exchange/list")
    @Operation(summary = "점주 자신의 모든 반품서들을 조회합니다.")
    public ResponseEntity<List<ResponseExchange>> getMyExchanges(){
        List<ExchangeDTO> exchangeDTOS = exchangeService.getFrOwnerExchanges();

        if (exchangeDTOS ==null){
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
        }
        System.out.println("exchangeDTOS.size() = " + exchangeDTOS.size());
        List<ResponseExchange> responseExchanges = new ArrayList<>();

        for (int i = 0; i < exchangeDTOS.size(); i++) {
            responseExchanges.add(new ResponseExchange(exchangeDTOS.get(i)));
        }
        return ResponseEntity.ok(responseExchanges);
    }

    @GetMapping("/exchange/{exchangeCode}")
    @Operation(summary = "점주가 자신의 발주를 상세 조회합니다.")
    public ResponseEntity<ResponseExchange> getMyExchange(@PathVariable int exchangeCode){
        ExchangeDTO exchangeDTO = exchangeService.getExchangeByFranchiseOwnerCode(exchangeCode);
        if (exchangeDTO == null)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        return ResponseEntity.ok(new ResponseExchange(exchangeDTO));
    }

    @PostMapping("exchange")
    @Operation(summary = "점주가 새로운 반품서를 생성합니다. ")
    public ResponseEntity<ResponseExchange> postExchange( @RequestBody RequestExchange requestExchange){
        ExchangeDTO exchange=  exchangeService.postExchange(requestExchange);
        if (exchange==null)
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
        System.out.println("/");
        return ResponseEntity.ok(new ResponseExchange(exchange));
    }

    @DeleteMapping("/exchange/{exchangeCode}")
    @Operation(summary = "점주가 자신의 반품서를 제거합니다.")
    public ResponseEntity<String> deleteExchange(@PathVariable int exchangeCode){

        boolean isDelete = exchangeService.deleteExchange(exchangeCode);

        if (!isDelete)
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("삭제 -실패-");

        return ResponseEntity.status(HttpStatus.OK).body("삭제 -완-");
    }

}
