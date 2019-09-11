package com.example.controller;

import com.example.dto.HomeDto;
import com.example.exception.TestException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestExcepController {

    @GetMapping("/find2/{id}")
    public HomeDto find2(@PathVariable("id") Long id){
        if(9999L == id){
            throw new TestException();//这里抛出了异常
        }
        return new HomeDto("hello ni hao");
    }

}
