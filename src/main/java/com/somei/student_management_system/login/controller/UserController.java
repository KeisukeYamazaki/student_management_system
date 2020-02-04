package com.somei.student_management_system.login.controller;

import com.somei.student_management_system.login.domain.model.User;
import com.somei.student_management_system.login.domain.model.UserForm;
import com.somei.student_management_system.login.domain.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Controller
public class UserController {

    @Autowired
    UserService userService;

    /**
     * ユーザー一覧画面のGETメソッド用処理（アドミン権限専用）.
     */
    @GetMapping("/admin")
    public String getUserList(Model model) {

        //コンテンツ部分にユーザー一覧を表示するための文字列を登録
        model.addAttribute("contents", "login/admin :: admin_contents");

        //ユーザー一覧の生成
        List<User> userList = userService.selectMany();

        //Modelにユーザーリストを登録
        model.addAttribute("userList", userList);

        //データ件数を取得
        //int count = userService.count();
        //model.addAttribute("userListCount", count);

        return "login/homeLayout";
    }

    /**
     * ユーザー編集画面のGETメソッド用処理.
     */
    @GetMapping("/userEdit/{id:.+}")
    public String getUserEdit(@ModelAttribute UserForm form,
                                 Model model,
                                 @PathVariable("id") String userId) {

        // ユーザーID確認（デバッグ）
        System.out.println("Edit_userId = " + userId);

        // コンテンツ部分に生徒詳細を表示するための文字列を登録
        model.addAttribute("contents", "login/userEdit :: userEdit_contents");

        // ドロップダウンリストの内容を登録
        model.addAttribute("roles", getSelectedRole());

        // 生徒IDのチェック
        if (userId != null && userId.length() > 0) {

            // 生徒情報を取得
            User user = userService.selectOne(userId);

            // Userクラスをフォームクラスに変換
            form.setUserId(user.getUserId());
            form.setUserName(user.getUserName());
            form.setPassword(user.getPassword());
            form.setRole(user.getRole());
            form.setReallyPass(user.getReallyPass());

            // Modelに登録
            model.addAttribute("userForm", form);
        }

        return "login/homeLayout";
    }

    /**
     * ユーザー新規登録画面のGETメソッド用処理.
     */
    @GetMapping("/userSignup")
    public String getUserSignup(@ModelAttribute UserForm form,
                                Model model) {

        // コンテンツ部分に生徒詳細を表示するための文字列を登録
        model.addAttribute("contents", "login/userSignup :: userSignup_contents");

        // ドロップダウンリストの内容を登録
        model.addAttribute("roles", getSelectedRole());

        return "login/homeLayout";
    }

    /**
     * ユーザー登録処理.
     */
    @PostMapping("/userSignup")
    public String postUserSignup(@ModelAttribute UserForm form,
                                 Model model) {

        System.out.print("ユーザー新規登録：");
        System.out.println(form);

        //Userインスタンスの生成
        User user = new User();

        //フォームクラスをUserクラスに変換
        user.setUserId(form.getUserId());
        user.setPassword(form.getPassword());
        user.setUserName(form.getUserName());
        user.setRole(form.getRole());
        user.setReallyPass(form.getReallyPass());

        // 登録処理
        boolean result = userService.insert(user);

        // 登録結果の判定
        if (result == true) {
            System.out.println("ユーザーinsert成功");
        } else {
            System.out.println("ユーザーinsert失敗");
        }

        //ユーザー一覧画面を表示
        return getUserList(model);
    }


    /**
     * ユーザー更新用処理.
     */
    @PostMapping(value = "/userEdit", params = "update")
    public String postUserDetailUpdate(@ModelAttribute UserForm form,
                                       Model model) {

        System.out.println("更新ボタンの処理");

        //Userインスタンスの生成
        User user = new User();

        //フォームクラスをUserクラスに変換
        user.setUserId(form.getUserId());
        user.setPassword(form.getPassword());
        user.setUserName(form.getUserName());
        user.setRole(form.getRole());
        user.setReallyPass(form.getReallyPass());

        try {

            //更新実行
            boolean result = userService.updateOne(user);

            if (result == true) {
                model.addAttribute("result", "更新成功");
            } else {
                model.addAttribute("result", "更新失敗");
            }

        } catch(DataAccessException e) {

            model.addAttribute("result", "更新失敗");

        }

        //ユーザー一覧画面を表示
        return getUserList(model);
    }

    /**
     * ユーザー削除用処理.
     */
    @PostMapping(value = "/userEdit", params = "delete")
    public String postUserDetailDelete(@ModelAttribute UserForm form,
                                       Model model) {

        System.out.println("削除ボタンの処理");

        //削除実行
        boolean result = userService.deleteOne(form.getUserId());

        if (result == true) {

            model.addAttribute("result", "削除しました");

        } else {

            model.addAttribute("result", "削除失敗");

        }

        //ユーザー一覧画面を表示
        return getUserList(model);
    }

    // 権限のドロップダウンリスト
    public Map<String, String> getSelectedRole() {
        Map<String, String> selectMap = new LinkedHashMap<>();
        selectMap.put("ROLE_GENERAL", "ROLE_GENERAL");
        selectMap.put("ROLE_ADMIN", "ROLE_ADMIN");
        return selectMap;
    }
}
