package com.ChattingApp.ChattingAppBackend.Controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthyController {
    @GetMapping("/health")
    public String health() {
        return "health ";
    }
}
