package com.example.demo;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class GitlabServiceController {

    /**
     * 一覧を表示（トップページ）
     */
    @RequestMapping(value = "/")
    public String index(Model model) {
        //トップページに遷移する
        return "list";
    }

}
