package com.somei.student_management_system.login.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {

    // ログイン画面のGET用コントローラー
    @GetMapping("/login")
    public String getLogin(Model model) {

        // login.htmlに画面遷移
        return "login/login";
    }

    @PostMapping("/login")
    public String postLogin(Model model) {

        // login.htmlに画面遷移
        return "login/login";
    }
}
