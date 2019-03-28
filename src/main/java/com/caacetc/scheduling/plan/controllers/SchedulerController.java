package com.caacetc.scheduling.plan.controllers;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class SchedulerController {

    @GetMapping
    public String users() {
        return "ssssss";
    }
}
