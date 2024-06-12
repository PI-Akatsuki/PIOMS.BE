package com.akatsuki.pioms.log.controller;

import com.akatsuki.pioms.log.aggregate.Log;
import com.akatsuki.pioms.log.service.LogService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/admin/log")
@Tag(name = "[관리자]이력관리 API")
public class LogController {

    private LogService logService;

    @Autowired
    public LogController(LogService logService) {
        this.logService = logService;
    }

    @GetMapping
    public ResponseEntity<List<Log>> getAllLogs() {
        List<Log> logs = logService.getAllLogs();
        return ResponseEntity.ok(logs);
    }
}
