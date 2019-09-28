package com.somei.student_management_system.login.controller;

import com.somei.student_management_system.login.domain.model.Student;
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
import java.util.List;

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

        // 今月と来月の取得
        String thisMonth = String.valueOf(time.getMonthValue());
        String nextMonth = String.valueOf(time.plusMonths(1).getMonthValue());

        // 今月と来月の誕生日の文字列
        String birthdayStr = thisMonth + "月と" + nextMonth + "月の誕生日";

        // 誕生日の生徒を取得
        List<Student> birthdayList = studentService.selectManyByBirthday(thisMonth, nextMonth);

        //コンテンツ部分にホーム画面を表示するための文字列を登録
        model.addAttribute("contents", "login/home :: home_contents");

        // 誕生日文字列の表示
        model.addAttribute("birthdayStr", birthdayStr);

        // 誕生日のリストを送る
        model.addAttribute("birthdayList", birthdayList);

        // ユーザーを表示
        model.addAttribute("user", userName);

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
