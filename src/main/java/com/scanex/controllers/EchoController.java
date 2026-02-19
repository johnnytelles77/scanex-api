package com.scanex.controllers;


import com.scanex.dtos.EchoRequestDTO;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class EchoController {

    @PostMapping("/echo")
    public EchoRequestDTO ech0(@Valid @RequestBody EchoRequestDTO body){
        return body;
    }
}
