package com.somei.student_management_system.login.controller;

import com.somei.student_management_system.login.domain.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class HomeController {

    @Autowired
    StudentService studentService;

    /**
     * ホーム画面のGET用メソッド
     */
    @GetMapping("/home")
    public String getHome(Model model) {

        // ユーザー名を取得
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String userName = auth.getName();

        // ログ出力
        System.out.println("ログイン中：" + userName);

        //コンテンツ部分にホーム画面を表示するための文字列を登録
        model.addAttribute("contents", "login/home :: home_contents");

        // ユーザーを表示
        //model.addAttribute("user", userName);

        return "login/homeLayout";
    }

    /**
     * ログアウト用処理.
     */
    @PostMapping("/logout")
    public String postLogout() {

        //ログイン画面にリダイレクト
        return "redirect:/login";
    }

}
