package com.example.pasir_musial_konrad.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class samodzielnyController {
    @GetMapping("api/info")
    public String samodzielne(){
        return "appName: Aplikacja budzetowa \n" +
                "appVersion: 1.0.0 \n";
    }

}
