package com.akatsuki.pioms.exchange.controller;

import com.akatsuki.pioms.exchange.entity.RequestExchange;
import com.akatsuki.pioms.exchange.vo.ResponseExchange;
import com.akatsuki.pioms.exchange.service.ExchangeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/exchange")
public class ExchangeController {
    ExchangeService exchangeService;

    @Autowired
    public ExchangeController(ExchangeService exchangeService) {
        this.exchangeService = exchangeService;
    }

    @GetMapping("/list")
    public ResponseEntity<List<ResponseExchange>> getExchanges(){
        return ResponseEntity.ok(exchangeService.getExchanges());
    }



    @PutMapping("/{exchangeCode}")
    public ResponseEntity<ResponseExchange> putExchange(@PathVariable int exchangeCode,@RequestBody RequestExchange requestExchange){
        return ResponseEntity.ok(exchangeService.putExchange(exchangeCode,requestExchange));
    }

}
