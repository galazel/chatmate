package com.demo_ai.demo_ai;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/ai/")
public class AIController {

    private final AIService service;
    public AIController(AIService service) {
        this.service = service;
    }

    @GetMapping
    public String greeting()
    {
        return "Hello World";
    }
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("request")
    public String request(@RequestBody AIDTO dto) {
        return service.askAI(dto);
    }


}
