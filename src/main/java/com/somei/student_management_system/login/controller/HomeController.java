package com.somei.student_management_system.login.controller;

import com.somei.student_management_system.login.domain.service.StudentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Controller
public class HomeController {

    private static final Logger LOGGER = LoggerFactory.getLogger(HomeController.class);

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

        // 時間の取得
        LocalDateTime time = LocalDateTime.now();

        // ログ出力
        LOGGER.info("ログイン中：" + DateTimeFormatter.ofPattern("yyyy/M/d HH:mm").format(time) + ' ' + userName);

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
