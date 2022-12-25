package com.backend.todolist.backend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@CrossOrigin("*")
@RequestMapping(ParentController.BASE_URI)
public class ParentController {
    protected static final String BASE_URI = "/todolist";

    @GetMapping("/healthCheck")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("OK");
    }

}
