package com.pjh.community.controller;

import com.pjh.community.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/test")
public class TestController {

    @Autowired
    private TestService testService;

    @RequestMapping("/hello")
    @ResponseBody
    public String sayHello(){
        return "Hello SpringBoot.";
    }

    @RequestMapping("/find")
    @ResponseBody
    public String find(){
        return testService.find();
    }
}
