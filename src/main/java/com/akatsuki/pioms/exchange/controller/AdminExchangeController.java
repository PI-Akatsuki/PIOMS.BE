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
@RequestMapping("/admin")
@Tag(name = "[관리자]반품 API")
public class AdminExchangeController {
    ExchangeService exchangeService;

    @Autowired
    public AdminExchangeController(ExchangeService exchangeService) {
        this.exchangeService = exchangeService;
    }

    @GetMapping("/exchange/list")
    @Operation(summary = "관리자가 자신이 관리하고 있는 모든 가맹점에 대한 반품서들을 조회할 수 있습니다.")
    public ResponseEntity<List<ResponseExchange>> getExchanges(){
        List<ExchangeDTO> exchangeDTOS = exchangeService.getExchangesByAdminCodeOrderByExchangeDateDesc();
        List<ResponseExchange> responseExchanges = new ArrayList<>();
        exchangeDTOS.forEach(exchangeDTO -> {
            responseExchanges.add(new ResponseExchange(exchangeDTO));
        });
        return ResponseEntity.ok(responseExchanges);
    }

    @GetMapping("/exchange/{exchangeCode}")
    @Operation(summary = "반품서를 상세 조회할 수 있습니다.")
    public ResponseEntity<ResponseExchange> getExchange(@PathVariable int exchangeCode){
        ExchangeDTO exchangeDTO = exchangeService.getExchangeByAdminCode(exchangeCode);
        if (exchangeDTO == null)
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
        return ResponseEntity.ok(new ResponseExchange(exchangeDTO));
    }

    // admin이 반송 수정시킴
    @PutMapping("/exchange/{exchangeCode}")
    @Operation(summary = "관리자가 처리 대기중인 반품서를 검수하여 검수 결과를 업데이트합니다.")
    public ResponseEntity<ResponseExchange> processArrivedExchange(@PathVariable int exchangeCode,@RequestBody RequestExchange requestExchange){
        System.out.println("requestExchange = " + requestExchange);
        ExchangeDTO exchangeDTO= exchangeService.processArrivedExchange(exchangeCode,requestExchange);
        if (exchangeDTO==null)
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(null);
        return ResponseEntity.ok(new ResponseExchange(exchangeDTO));
    }
}
