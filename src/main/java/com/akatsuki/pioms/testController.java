package com.akatsuki.pioms;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
@Tag(name = "이름입니다.")
public class testController {
    @GetMapping("/")
    @Operation(summary = "test")
    public String  swaggerTest() {
        return "hi";
    }
}
