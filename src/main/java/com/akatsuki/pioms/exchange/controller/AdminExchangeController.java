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
@RequestMapping("/admin")
public class AdminExchangeController {
    ExchangeService exchangeService;

    @Autowired
    public AdminExchangeController(ExchangeService exchangeService) {
        this.exchangeService = exchangeService;
    }

    @GetMapping("/{adminCode}/exchanges")
    public ResponseEntity<List<ResponseExchange>> getExchanges(int adminCode){
        List<ExchangeDTO> exchangeDTOS = exchangeService.getExchanges(adminCode);
        List<ResponseExchange> responseExchanges = new ArrayList<>();
        exchangeDTOS.forEach(exchangeDTO -> {
            responseExchanges.add(new ResponseExchange(exchangeDTO));
        });
        return ResponseEntity.ok(responseExchanges);
    }

    @GetMapping("/{adminCode}/exchange/{exchangeCode}")
    public ResponseEntity<ResponseExchange> getExchange(@PathVariable int adminCode,@PathVariable int exchangeCode){
        ExchangeDTO exchangeDTO = exchangeService.getAdminExchange(adminCode,exchangeCode);

        if (exchangeDTO == null)
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
        return ResponseEntity.ok(new ResponseExchange(exchangeDTO));
    }

    // admin이 반송 수정시킴
    @PutMapping("{AdminCode}/exchange/{exchangeCode}")
    public ResponseEntity<ResponseExchange> putExchange(@PathVariable int AdminCode,@PathVariable int exchangeCode,@RequestBody RequestExchange requestExchange){
        return ResponseEntity.ok(new ResponseExchange(exchangeService.putExchange(AdminCode,exchangeCode,requestExchange)));
    }
}
