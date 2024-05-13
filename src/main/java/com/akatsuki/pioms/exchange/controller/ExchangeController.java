package com.akatsuki.pioms.exchange.controller;

import com.akatsuki.pioms.exchange.aggregate.RequestExchange;
import com.akatsuki.pioms.exchange.aggregate.ResponseExchange;
import com.akatsuki.pioms.exchange.dto.ExchangeDTO;
import com.akatsuki.pioms.exchange.service.ExchangeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
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
        List<ExchangeDTO> exchangeDTOS = exchangeService.getExchanges();
        List<ResponseExchange> responseExchanges = new ArrayList<>();
        exchangeDTOS.forEach(exchangeDTO -> {
            responseExchanges.add(new ResponseExchange(exchangeDTO));
        });
        return ResponseEntity.ok(responseExchanges);
    }

    @PostMapping("/{franchiseCode}")
    public ResponseEntity<ResponseExchange> postExchange(@PathVariable int franchiseCode, @RequestBody RequestExchange requestExchange){
        ResponseExchange exchange=  exchangeService.postExchange(franchiseCode, requestExchange);
        if (exchange==null)
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();

        return ResponseEntity.ok(exchange);
    }


    @PutMapping("/{exchangeCode}")
    public ResponseEntity<ResponseExchange> putExchange(@PathVariable int exchangeCode,@RequestBody RequestExchange requestExchange){
        return ResponseEntity.ok(exchangeService.putExchange(exchangeCode,requestExchange));
    }

}
