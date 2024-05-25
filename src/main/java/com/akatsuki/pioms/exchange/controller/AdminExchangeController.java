package com.akatsuki.pioms.exchange.controller;

import com.akatsuki.pioms.exchange.aggregate.EXCHANGE_STATUS;
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

    @GetMapping("/exchanges")
    public ResponseEntity<List<ResponseExchange>> getExchanges(@RequestParam int adminCode){
        List<ExchangeDTO> exchangeDTOS = exchangeService.getExchangesByAdminCode(adminCode);
        List<ResponseExchange> responseExchanges = new ArrayList<>();
        exchangeDTOS.forEach(exchangeDTO -> {
            responseExchanges.add(new ResponseExchange(exchangeDTO));
        });
        return ResponseEntity.ok(responseExchanges);
    }

    @GetMapping("/exchange/{exchangeCode}")
    public ResponseEntity<ResponseExchange> getExchange(@RequestParam int adminCode,@PathVariable int exchangeCode){
        ExchangeDTO exchangeDTO = exchangeService.getAdminExchange(adminCode,exchangeCode);

        if (exchangeDTO == null)
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
        return ResponseEntity.ok(new ResponseExchange(exchangeDTO));
    }

    // admin이 반송 수정시킴
    @PutMapping("/exchange/{exchangeCode}")
    public ResponseEntity<ResponseExchange> putExchange(@RequestParam int adminCode,@PathVariable int exchangeCode,@RequestBody RequestExchange requestExchange){
        return ResponseEntity.ok(new ResponseExchange(exchangeService.putExchange(adminCode,exchangeCode,requestExchange)));
    }


    @PutMapping("/exchange/{exchangeCode}/{exchangeStatus}")
    public ResponseEntity<ResponseExchange> updateExchangeEndDelivery(@PathVariable int exchangeCode, EXCHANGE_STATUS exchangeStatus){
        ExchangeDTO exchangeDTO = exchangeService.updateExchangeEndDelivery(exchangeCode);
        if (exchangeDTO == null){
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
        }
        return ResponseEntity.ok(new ResponseExchange(exchangeDTO));
    }

}
