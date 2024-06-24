package com.jh.security1.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

// View를 리턴하겠다.
@Controller
public class IndexController {

    @GetMapping({"", "/"})
    public String index() {
        // 머스테치 기본폴더 src/main/resources
        // 뷰리졸버 설정: templates(prefix), .mustache (suffix) 생략 가능!
        return "index";
    }
}
