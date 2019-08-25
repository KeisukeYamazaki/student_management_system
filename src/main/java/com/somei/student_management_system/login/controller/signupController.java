package com.somei.student_management_system.login.controller;

import com.somei.student_management_system.login.domain.model.SignupForm;
import com.somei.student_management_system.login.domain.model.Student;
import com.somei.student_management_system.login.domain.model.Zenken;
import com.somei.student_management_system.login.domain.service.StudentService;
import com.somei.student_management_system.login.domain.service.ZenkenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

@Controller
public class signupController {

    @Autowired
    StudentService studentService;

    @Autowired
    StudentController studentController;

    @Autowired
    ZenkenService zenkenService;

    /**
     * 生徒登録用のGETメソッド.
     */
    @GetMapping("/signup")
    public String getSignup(@ModelAttribute SignupForm form, Model model) {

        // コンテンツ部分に新規登録を表示するための文字列を登録
        model.addAttribute("contents", "login/signup :: studentRegister_contents");

        // ドロップダウンリストの内容を登録
        model.addAttribute("grades", getSelectedGrades());
        model.addAttribute("schools", getSelectedSchools());
        model.addAttribute("homeRooms", getSelectedHomeRooms());
        model.addAttribute("entryTimes", getSelectedEntryTimes());
        model.addAttribute("years", getSelectedYears());
        model.addAttribute("months", getSelectedMonths());
        model.addAttribute("days", getSelectedDays());

        return "login/homeLayout";
    }

    /**
     * 登録確認画面の表示.
     */
    @PostMapping("/signupConfirm")
    public String postSignupConfirm(@ModelAttribute @Validated SignupForm form,
                                    BindingResult bindingResult,
                                    Model model) {

        // 入力チェックに引っかかった場合、生徒登録画面に戻る
        if (bindingResult.hasErrors()) {

            // GET用のメソッドを呼び出して、生徒登録画面に戻る
            return getSignup(form, model);
        }

        // コンテンツ部分に新規登録を表示するための文字列を登録
        model.addAttribute("contents", "login/signupConfirm :: signupConfirm_contents");

        return "login/homeLayout";
    }

    /**
     * 生徒登録用処理
     */
    @PostMapping(value = "/signupConfirm", params = "register")
    public String postStudentInsert(@ModelAttribute SignupForm form, Model model) {

        System.out.print("新規登録：");
        System.out.println(form);

        //Studentインスタンスの生成
        Student student = new Student();

        // 誕生日をフォーマット
        Date birthday;
        try {
            LocalDate date = LocalDate.of(form.getBirthdayYear(), form.getBirthdayMonth(), form.getBirthdayDay());
            birthday = Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant());
        } catch (NullPointerException e) {
            birthday = null;
        }

        String name = form.getLastName() + "　" + form.getFirstName();
        String ruby = form.getLastRuby() + "　" + form.getFirstRuby();

        //フォームクラスをStudentクラスに変換
        student.setStudentId(form.getStudentId());
        student.setStudentName(name);
        student.setNameRuby(ruby);
        student.setGrade(form.getGrade());
        student.setSchool(form.getSchool());
        student.setHomeRoom(form.getHomeRoom());
        student.setLocalSchool(form.getLocalSchool());
        student.setEntryTime(form.getEntryTime());
        student.setBirthday(birthday);
        student.setClub(form.getClub());
        student.setParents(form.getParents());
        student.setSiblings(form.getSiblings());
        student.setAddress(form.getAddress());

        // Zenken インスタンスの生成
        Zenken zenken = new Zenken();

        //フォームクラスを Zenkenインスタンスに変換
        zenken.setId(form.getStudentId());
        zenken.setEnrollment("1");
        zenken.setPrefecture("14");

        // 生徒登録処理
        boolean result = studentService.insert(student);

        // 全県登録処理
        boolean zenkenResult = zenkenService.insertOne(zenken);

        // 生徒登録結果の判定
        if (result == true && zenkenResult == true) {
            System.out.println("insert成功");
            model.addAttribute("result", "登録しました");
        } else {
            System.out.println("insert失敗");
            model.addAttribute("result", "登録できませんでした");
        }

        //生徒一覧画面を表示
        return studentController.getStudentList(model);
    }

    /**
     * 戻るボタンの処理
     */
    @PostMapping(value = "/signupConfirm", params = "back")
    public String postSignupConfirmBack(@ModelAttribute SignupForm form, Model model) {

        // コンテンツ部分に新規登録を表示するための文字列を登録
        model.addAttribute("contents", "login/signup :: studentRegister_contents");

        // ドロップダウンリストの内容を登録
        model.addAttribute("grades", getSelectedGrades());
        model.addAttribute("schools", getSelectedSchools());
        model.addAttribute("homeRooms", getSelectedHomeRooms());
        model.addAttribute("entryTimes", getSelectedEntryTimes());
        model.addAttribute("years", getSelectedYears());
        model.addAttribute("months", getSelectedMonths());
        model.addAttribute("days", getSelectedDays());

        return "login/homeLayout";
    }

    @ExceptionHandler(DataAccessException.class)
    public String dataAccessExceptionHandler(DataAccessException e, Model model) {

        // 例外クラスのメッセージをModelに登録
        model.addAttribute("error", " 内部サーバーエラー（DB）：ExceptionHandler");

        // 例外クラスのメッセージをModelに登録
        model.addAttribute("message", "SignupControllerでDataAccessExceptionが発生しました");

        // HTTPのエラーコード（500）をModelに登録
        model.addAttribute("status", HttpStatus.INTERNAL_SERVER_ERROR);

        return "error";
    }

    @ExceptionHandler(Exception.class)
    public String exceptionHandler(Exception e, Model model) {

        // 例外クラスのメッセージをModelに登録
        model.addAttribute("error", " 内部サーバーエラー：ExceptionHandler");

        // 例外クラスのメッセージをModelに登録
        model.addAttribute("message", "SignupControllerでExceptionが発生しました");

        // HTTPのエラーコード（500）をModelに登録
        model.addAttribute("status", HttpStatus.INTERNAL_SERVER_ERROR);

        return "error";
    }

    // 学年のドロップダウンリスト
    public Map<String, String> getSelectedGrades() {
        Map<String, String> selectMap = new LinkedHashMap<>();
        selectMap.put("中３", "中３");
        selectMap.put("中２", "中２");
        selectMap.put("中１", "中１");
        selectMap.put("小６", "小６");
        selectMap.put("小５", "小５");
        selectMap.put("小４", "小４");
        selectMap.put("小３", "小３");
        return selectMap;
    }

    // 校舎のドロップダウンリスト
    public Map<String, String> getSelectedSchools() {
        Map<String, String> selectMap = new LinkedHashMap<>();
        selectMap.put("瀬谷校", "瀬谷校");
        selectMap.put("橋戸校", "橋戸校");
        selectMap.put("大和校", "大和校");
        return selectMap;
    }

    // クラスのドロップダウンリスト
    public Map<String, String> getSelectedHomeRooms() {
        Map<String, String> selectMap = new LinkedHashMap<>();
        selectMap.put("３特", "３特");
        selectMap.put("３Ａ", "３Ａ");
        selectMap.put("３Ｂ", "３Ｂ");
        selectMap.put("３Ｃ", "３Ｃ");
        selectMap.put("２Ａ", "２Ａ");
        selectMap.put("２Ｂ", "２Ｂ");
        selectMap.put("２Ｃ", "２Ｃ");
        selectMap.put("１Ａ", "１Ａ");
        selectMap.put("１Ｂ", "１Ｂ");
        selectMap.put("小６", "小６");
        selectMap.put("小５", "小５");
        selectMap.put("小４", "小４");
        selectMap.put("小３", "小３");
        return selectMap;
    }


    // 入塾時期のドロップダウンリスト
    public Map<String, String> getSelectedEntryTimes() {
        Map<String, String> selectMap = new LinkedHashMap<>();
        selectMap.put("中３", "中３");
        selectMap.put("中２", "中２");
        selectMap.put("中１", "中１");
        selectMap.put("小６", "小６");
        selectMap.put("小５", "小５");
        selectMap.put("小４", "小４");
        selectMap.put("小３", "小３");

        return selectMap;
    }

    // 誕生日（年）のドロップダウンリスト
    public Map<String, Integer> getSelectedYears() {
        Map<String, Integer> selectMap = new LinkedHashMap<>();
        for (int i = 2018; i >= 2002; i--) {
            selectMap.put(i + "年", i);
        }
        return selectMap;
    }

    // 誕生日（月）のドロップダウンリスト
    public Map<String, Integer> getSelectedMonths() {
        Map<String, Integer> selectMap = new LinkedHashMap<>();
        for (int i = 1; i <= 12; i++) {
            selectMap.put(i + "月", i);
        }
        return selectMap;
    }

    // 誕生日（日）のドロップダウンリスト
    public Map<String, Integer> getSelectedDays() {
        Map<String, Integer> selectMap = new LinkedHashMap<>();
        for (int i = 1; i <= 31; i++) {
            selectMap.put(i + "日", i);
        }
        return selectMap;
    }

}
