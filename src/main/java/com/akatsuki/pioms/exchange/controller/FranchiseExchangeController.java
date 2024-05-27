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
@RequestMapping("franchise")
public class FranchiseExchangeController {
    ExchangeService exchangeService;

    @Autowired
    public FranchiseExchangeController(ExchangeService exchangeService) {
        this.exchangeService = exchangeService;
    }

    @GetMapping("{franchiseOwnerCode}/exchanges")
    public ResponseEntity<List<ResponseExchange>> getMyExchanges(@PathVariable int franchiseOwnerCode){
        List<ExchangeDTO> exchangeDTOS = exchangeService.getFrOwnerExchanges(franchiseOwnerCode);

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

    @GetMapping("{franchiseOwnerCode}/exchange/{exchangeCode}")
    public ResponseEntity<ResponseExchange> getMyExchange(@PathVariable int franchiseOwnerCode, @PathVariable int exchangeCode){
        ExchangeDTO exchangeDTO = exchangeService.getFranchiseExchange(franchiseOwnerCode,exchangeCode);
        if (exchangeDTO == null)
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
        return ResponseEntity.ok(new ResponseExchange(exchangeDTO));
    }

    @PostMapping("exchange")
    public ResponseEntity<ResponseExchange> postExchange(@RequestParam int franchiseOwnerCode, @RequestBody RequestExchange requestExchange){
        System.out.println("requestExchange = " + requestExchange);
        ExchangeDTO exchange=  exchangeService.postExchange(franchiseOwnerCode, requestExchange);
        System.out.println("exchange = " + exchange);
        if (exchange==null)
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
        System.out.println("/");
        return ResponseEntity.ok(new ResponseExchange(exchange));
    }

    @DeleteMapping("/exchange/{exchangeCode}")
    public ResponseEntity<String> deleteExchange(@RequestParam int franchiseOwnerCode, @PathVariable int exchangeCode){

        boolean isDelete = exchangeService.deleteExchange(franchiseOwnerCode,exchangeCode);

        if (!isDelete)
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("삭제 -실패-");

        return ResponseEntity.status(HttpStatus.OK).body("삭제 -완-");
    }

}
