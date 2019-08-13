package com.somei.student_management_system.login.controller;

import com.somei.student_management_system.login.domain.model.PracticeExam;
import com.somei.student_management_system.login.domain.model.RegularExam;
import com.somei.student_management_system.login.domain.model.SchoolRecord;
import com.somei.student_management_system.login.domain.model.SignupForm;
import com.somei.student_management_system.login.domain.model.Student;
import com.somei.student_management_system.login.domain.service.NumericDataService;
import com.somei.student_management_system.login.domain.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
public class StudentController {

    @Autowired
    StudentService studentService;

    @Autowired
    NumericDataService numericDataService;

    @Autowired
    HomeController homeController;

    @Autowired
    signupController signupController;

    /**
     * 生徒一覧画面のGETメソッド用処理.
     */
    @GetMapping("/studentList")
    public String getStudentList(Model model) {

        //コンテンツ部分に生徒一覧を表示するための文字列を登録
        model.addAttribute("contents", "login/studentList :: studentList_contents");

        //生徒一覧の生成
        List<Student> studentList = studentService.selectMany();

        //Modelに生徒リストを登録
        model.addAttribute("studentList", studentList);

        //データ件数を取得
        ArrayList<Integer> counts = studentService.count();
        model.addAttribute("studentListCount", counts.get(0));
        model.addAttribute("junior3rdCount", counts.get(1));
        model.addAttribute("junior2ndCount", counts.get(2));
        model.addAttribute("junior1stCount", counts.get(3));
        model.addAttribute("elementary6thCount", counts.get(4));
        model.addAttribute("elementary5thCount", counts.get(5));
        model.addAttribute("elementary4thCount", counts.get(6));
        model.addAttribute("elementary3rdCount", counts.get(7));

        return "login/homeLayout";
    }

    /**
     * 生徒詳細画面のGETメソッド用処理.
     */
    @GetMapping("/studentDetail/{id:.+}")
    public String getStudentDetail(@ModelAttribute SignupForm form,
                                   Model model,
                                   @PathVariable("id") String studentId) {

        // 生徒ID確認（デバッグ）
        System.out.println("Detail_studentId = " + studentId);

        // コンテンツ部分に生徒詳細を表示するための文字列を登録
        model.addAttribute("contents", "login/studentDetail :: studentDetail_contents");

        // 生徒IDのチェック
        if (studentId != null && studentId.length() > 0) {

            // 生徒情報を取得
            Student student = studentService.selectOne(studentId);

            // 成績一覧の生成
            List<SchoolRecord> schoolRecordList = numericDataService.selectRecordOne(studentId);

            // 定期試験一覧の生成
            List<RegularExam> regularExamList = numericDataService.selectRegularOne(studentId);

            // 模試一覧の生成
            List<PracticeExam> practiceExamList = numericDataService.selectPracticeOne(studentId);

            // Modelに登録
            model.addAttribute("student", student);
            model.addAttribute("schoolRecordList", schoolRecordList);
            model.addAttribute("regularExamList", regularExamList);
            model.addAttribute("practiceExamList", practiceExamList);
        }

        return "login/homeLayout";
    }

    /**
     * 生徒編集画面のGETメソッド用処理.
     */
    @GetMapping("/studentEdit/{id:.+}")
    public String getStudentEdit(@ModelAttribute SignupForm form,
                                 Model model,
                                 @PathVariable("id") String studentId) {

        // ユーザーID確認（デバッグ）
        System.out.println("Edit_studentId = " + studentId);

        // コンテンツ部分に生徒詳細を表示するための文字列を登録
        model.addAttribute("contents", "login/studentEdit :: studentEdit_contents");

        // ドロップダウンリストの内容を登録
        model.addAttribute("grades", signupController.getSelectedGrades());
        model.addAttribute("schools", signupController.getSelectedSchools());
        model.addAttribute("homeRooms", signupController.getSelectedHomeRooms());
        model.addAttribute("entryTimes", signupController.getSelectedEntryTimes());
        model.addAttribute("years", signupController.getSelectedYears());
        model.addAttribute("months", signupController.getSelectedMonths());
        model.addAttribute("days", signupController.getSelectedDays());

        // 生徒IDのチェック
        if (studentId != null && studentId.length() > 0) {

            // 生徒情報を取得
            Student student = studentService.selectOne(studentId);

            // 誕生日の年・月・日を取得
            int year;
            int month;
            int day;
            try {
                LocalDate birthday = student.getBirthday().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                year = birthday.getYear();
                month = birthday.getMonthValue();
                day = birthday.getDayOfMonth();
            } catch (NullPointerException e) {
                year = 0;
                month = 0;
                day = 0;
            }

            // Studentクラスをフォームクラスに変換
            form.setStudentId(student.getStudentId());
            form.setStudentName(student.getStudentName());
            form.setNameRuby(student.getNameRuby());
            form.setGrade(student.getGrade());
            form.setSchool(student.getSchool());
            form.setHomeRoom(student.getHomeRoom());
            form.setLocalSchool(student.getLocalSchool());
            form.setEntryTime(student.getEntryTime());
            form.setBirthdayYear(year);
            form.setBirthdayMonth(month);
            form.setBirthdayDay(day);
            form.setClub(student.getClub());
            form.setParents(student.getParents());
            form.setSiblings(student.getSiblings());
            form.setAddress(student.getAddress());
            form.setInfo(student.getInfo());

            // Modelに登録
            model.addAttribute("signupForm", form);
        }

        return "login/homeLayout";
    }


    /**
     * 生徒編集用処理.
     */
    @PostMapping(value = "/studentEdit", params = "update")
    public String postStudentDetailUpdate(@ModelAttribute @Validated SignupForm form,
                                          BindingResult bindingResult,
                                          Model model) {

        // 入力チェックに引っかかった場合、生徒編集画面に戻る
        if (bindingResult.hasErrors()) {

            // GET用のメソッドを呼び出して、生徒登録画面に戻る
            return getStudentEdit(form, model, form.getStudentId());
        }

        System.out.println("更新処理");

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

        //フォームクラスをStudentクラスに変換
        student.setStudentId(form.getStudentId());
        student.setStudentName(form.getStudentName());
        student.setNameRuby(form.getNameRuby());
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
        student.setInfo(form.getInfo());

        try {

            //更新実行
            boolean result = studentService.updateOne(student);

            if (result == true) {
                model.addAttribute("result", "更新しました");
            } else {
                model.addAttribute("result", "更新に失敗しました");
            }

        } catch (DataAccessException e) {

            model.addAttribute("result", "更新失敗(トランザクションテスト)");

        }

        //生徒一覧画面を表示
        return getStudentList(model);
    }

    /**
     * 生徒削除用処理.
     */
    @PostMapping("/studentDetail")
    public String postStudentDetailDelete(@ModelAttribute SignupForm form,
                                          Model model) {

        System.out.println("削除処理");

        //削除実行
        boolean result = studentService.deleteOne(form.getStudentId());

        if (result == true) {

            model.addAttribute("result", "削除しました");

        } else {

            model.addAttribute("result", "削除に失敗しました");

        }

        //生徒一覧画面を表示
        return getStudentList(model);
    }
}
