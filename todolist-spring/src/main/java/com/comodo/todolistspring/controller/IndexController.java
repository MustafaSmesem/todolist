package com.comodo.todolistspring.controller;

import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {
    @GetMapping("/")
    @ApiOperation(value = "Get the home page")
    public String index() {
        return "index.html";
    }
}
