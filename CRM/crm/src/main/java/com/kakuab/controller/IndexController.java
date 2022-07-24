package com.kakuab.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class IndexController {

    @RequestMapping("/")
    public String index(){
        /*springmvc已经配置了试图解释器，所以不用加/WEB-INF/pages*/
        return "index";
    }
}
