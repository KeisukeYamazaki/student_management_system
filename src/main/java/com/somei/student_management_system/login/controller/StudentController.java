package com.somei.student_management_system.login.controller;

import com.somei.student_management_system.login.bean.PdfView;
import com.somei.student_management_system.login.domain.model.FuturePath;
import com.somei.student_management_system.login.domain.model.FuturePathWithData;
import com.somei.student_management_system.login.domain.model.Highschools;
import com.somei.student_management_system.login.domain.model.PracticeExam;
import com.somei.student_management_system.login.domain.model.RegularExam;
import com.somei.student_management_system.login.domain.model.SchoolRecord;
import com.somei.student_management_system.login.domain.model.SignupForm;
import com.somei.student_management_system.login.domain.model.Student;
import com.somei.student_management_system.login.domain.service.NumericDataService;
import com.somei.student_management_system.login.domain.service.StudentService;
import com.somei.student_management_system.login.domain.service.ZenkenService;
import lombok.val;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Controller
public class StudentController {

    @Autowired
    StudentService studentService;

    @Autowired
    NumericDataService numericDataService;

    @Autowired
    ZenkenService zenkenService;

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


        // コンテンツ部分に生徒詳細を表示するための文字列を登録
        model.addAttribute("contents", "login/studentDetail :: studentDetail_contents");

        // 生徒IDのチェック
        if (studentId != null && studentId.length() > 0) {

            // 生徒情報を取得
            Student student = studentService.selectOne(studentId);

            // 生徒ID確認（デバッグ）
            System.out.println("Detail_student = " + student.getStudentName());

            // 成績一覧の生成（学年別に作成）
            List<SchoolRecord> schoolRecordList = numericDataService.selectRecordOne(studentId);
            List<SchoolRecord> schoolRecordList_1st = new ArrayList<>();
            List<SchoolRecord> schoolRecordList_2nd = new ArrayList<>();
            List<SchoolRecord> schoolRecordList_3rd = new ArrayList<>();
            for (SchoolRecord record : schoolRecordList) {
                if (record.getGrade().equals("中３")) {
                    schoolRecordList_3rd.add(record);
                } else if (record.getGrade().equals("中２")) {
                    schoolRecordList_2nd.add(record);
                } else {
                    schoolRecordList_1st.add(record);
                }
            }

            // 定期試験一覧の生成（学年別に作成）
            List<RegularExam> regularExamList = numericDataService.selectRegularOne(studentId);
            List<RegularExam> regularExamList_1st = new ArrayList<>();
            List<RegularExam> regularExamList_2nd = new ArrayList<>();
            List<RegularExam> regularExamList_3rd = new ArrayList<>();
            for (RegularExam rexam : regularExamList) {
                if (rexam.getGrade() == 3) {
                    regularExamList_3rd.add(rexam);
                } else if (rexam.getGrade() == 2) {
                    regularExamList_2nd.add(rexam);
                } else {
                    regularExamList_1st.add(rexam);
                }
            }

            // 模試一覧の生成（学年別に作成）
            List<PracticeExam> practiceExamList = numericDataService.selectPracticeOne(studentId);
            List<PracticeExam> practiceExamList_1st = new ArrayList<>();
            List<PracticeExam> practiceExamList_2nd = new ArrayList<>();
            List<PracticeExam> practiceExamList_3rd = new ArrayList<>();
            for (PracticeExam pexam : practiceExamList) {
                if (pexam.getGrade() == 3) {
                    practiceExamList_3rd.add(pexam);
                } else if (pexam.getGrade() == 2) {
                    practiceExamList_2nd.add(pexam);
                } else {
                    practiceExamList_1st.add(pexam);
                }
            }

            // 進路データの生成
            FuturePathWithData futurePathData = studentService.selectPathDataOne(studentId);

            // Modelに登録
            model.addAttribute("student", student);
            model.addAttribute("schoolRecordList_1st", schoolRecordList_1st);
            model.addAttribute("schoolRecordList_2nd", schoolRecordList_2nd);
            model.addAttribute("schoolRecordList_3rd", schoolRecordList_3rd);
            model.addAttribute("regularExamList_1st", regularExamList_1st);
            model.addAttribute("regularExamList_2nd", regularExamList_2nd);
            model.addAttribute("regularExamList_3rd", regularExamList_3rd);
            model.addAttribute("practiceExamList_1st", practiceExamList_1st);
            model.addAttribute("practiceExamList_2nd", practiceExamList_2nd);
            model.addAttribute("practiceExamList_3rd", practiceExamList_3rd);
            model.addAttribute("futurePathData", futurePathData);
        }

        return "login/homeLayout";
    }

    /**
     * 生徒編集画面のGETメソッド用処理.
     */
    @GetMapping("/studentEdit/{id:.+}")
    public String getStudentEdit(@ModelAttribute SignupForm form,
                                 @ModelAttribute FuturePathWithData futurePathData,
                                 Model model,
                                 @PathVariable("id") String studentId) {

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

            // 生徒名確認（デバッグ）
            System.out.println("Edit_student = " + student.getStudentName());

            // 姓と名を分ける
            String[] name = student.getStudentName().split("[ 　]");
            String lastName = name[0];
            String firstName = name[1];
            String[] ruby = student.getNameRuby().split("[ 　]");
            String lastRuby = ruby[0];
            String firstRuby = ruby[1];

            // Studentクラスをフォームクラスに変換
            form.setStudentId(student.getStudentId());
            form.setLastName(lastName);
            form.setFirstName(firstName);
            form.setLastRuby(lastRuby);
            form.setFirstRuby(firstRuby);
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

            // 進路データを取得
            FuturePathWithData data = studentService.selectPathDataOne(studentId);

            // 進路データを dataクラスに変換
            futurePathData.setStudentId(data.getStudentId());
            futurePathData.setFirstChoice(data.getFirstChoice());
            futurePathData.setSecondChoice(data.getSecondChoice());
            futurePathData.setThirdChoice(data.getThirdChoice());
            futurePathData.setPublicSchool1(data.getPublicSchool1());
            futurePathData.setPublicSchool2(data.getPublicSchool2());
            futurePathData.setPublicSchool3(data.getPublicSchool3());
            futurePathData.setPrivateSchool1(data.getPrivateSchool1());
            futurePathData.setPrivateSchool2(data.getPrivateSchool2());
            futurePathData.setPrivateSchool3(data.getPrivateSchool3());
            futurePathData.setInformation(data.getInformation());

            // Modelに登録
            model.addAttribute("signupForm", form);
            model.addAttribute("futurePathData", futurePathData);
        }

        return "login/homeLayout";
    }


    /**
     * 生徒編集用処理.
     */
    @PostMapping("/studentEdit")
    public String postStudentDetailUpdate(@ModelAttribute @Validated SignupForm form,
                                          @ModelAttribute FuturePathWithData futurePathData,
                                          BindingResult bindingResult,
                                          Model model) {

        // 入力チェックに引っかかった場合、生徒編集画面に戻る
        if (bindingResult.hasErrors()) {

            // GET用のメソッドを呼び出して、生徒登録画面に戻る
            return getStudentEdit(form, futurePathData, model, form.getStudentId());
        }

        // Studentの更新
        // Studentインスタンスの生成
        Student student = new Student();

        // 誕生日をフォーマット
        Date birthday;
        try {
            LocalDate date = LocalDate.of(form.getBirthdayYear(), form.getBirthdayMonth(), form.getBirthdayDay());
            birthday = Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant());
        } catch (NullPointerException e) {
            birthday = null;
        }

        // 名前を結合
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
        student.setInfo(form.getInfo());

        try {

            //更新実行
            boolean result = studentService.updateOne(student);

            if (result == true) {
                model.addAttribute("result", "生徒データを更新しました");
            } else {
                model.addAttribute("result", "生徒データ更新に失敗しました");
            }

        } catch (DataAccessException e) {

            model.addAttribute("result", "更新失敗(トランザクションテスト)");

        }

        // FuturePath の更新
        // FuturePathインスタンスの生成
        FuturePath futurePath = new FuturePath();

        // futurePathData を futurePathに変換
        futurePath.setStudentId(futurePathData.getStudentId());
        futurePath.setFirstChoice(futurePathData.getFirstChoice());
        futurePath.setSecondChoice(futurePathData.getSecondChoice());
        futurePath.setThirdChoice(futurePathData.getThirdChoice());
        futurePath.setPublicSchool1(futurePathData.getPublicSchool1());
        futurePath.setPublicSchool2(futurePathData.getPublicSchool2());
        futurePath.setPublicSchool3(futurePathData.getPublicSchool3());
        futurePath.setPrivateSchool1(futurePathData.getPrivateSchool1());
        futurePath.setPrivateSchool2(futurePathData.getPrivateSchool2());
        futurePath.setPrivateSchool3(futurePathData.getPrivateSchool3());
        futurePath.setInformation(futurePathData.getInformation());

        try {

            // 更新実行
            boolean result = studentService.updatePathOne(futurePath);

            if (result == true) {
                model.addAttribute("result", "更新しました");

                //ログ出力
                System.out.println("更新処理：" + form.getLastName() + form.getFirstName());

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

        String name = form.getLastName() + form.getFirstName();

        //Student削除実行
        boolean result = studentService.deleteOne(form.getStudentId());

        if (result == true) {

            model.addAttribute("result", "削除しました");

            // ログ出力
            System.out.println("削除処理：" + name);

        } else {

            model.addAttribute("result", "削除に失敗しました");

        }

        //生徒一覧画面を表示
        return getStudentList(model);
    }

    /**
     * クラス管理画面のGETメソッド.
     */
    @GetMapping("/classManagement")
    public String getClassManagement(Model model) {

        //コンテンツ部分に生徒一覧を表示するための文字列を登録
        model.addAttribute("contents", "login/classManagement :: classManagement_contents");

        return "login/homeLayout";
    }

    /**
     * クラス編集画面の表示メソッド.
     */
    @PostMapping("/classManagement")
    public String postClassManagement(@ModelAttribute SignupForm form,
                                      @RequestParam("grade") String grade,
                                      Model model) {

        // 学年のリストを生成
        List<Student> gradeList = studentService.selectManyByGrade(grade);

        //コンテンツ部分に生徒一覧を表示するための文字列を登録
        model.addAttribute("contents", "login/classManagement :: classManagement_contents");

        // ドロップダウンリストにクラスを表示
        model.addAttribute("homeRooms", signupController.getSelectedHomeRooms());

        //Modelに生徒リストを登録
        model.addAttribute("gradeList", gradeList);

        return "login/homeLayout";
    }

    /**
     * クラス編集処理.
     */
    @PostMapping(value = "/classManagement", params = "change")
    public String changeClassManagement(@ModelAttribute Student student, Model model) {


        // 送られてきたchangeの値をリストに格納
        List<String> idList = Arrays.asList(student.getStudentId().split(","));
        List<String> classList = Arrays.asList(student.getHomeRoom().split(","));

        // 渡すためのリストを作成
        List<Student> classChangeList = new ArrayList<>();

        if (idList.size() == classList.size()) {
            for (int i = 0; i < idList.size(); i++) {
                Student inList = new Student();

                // studentクラスにidとhomeRomeのデータを代入してリストに格納
                inList.setStudentId(idList.get(i));
                inList.setHomeRoom(classList.get(i));

                classChangeList.add(inList);
            }

        } else {

            System.out.println("クラス替え失敗");

            model.addAttribute("result", "全員のクラスが指定されているか確認してください");
        }

        try {

            // 更新実行
            boolean result = studentService.updateHomeRoom(classChangeList);

            if (result == true) {
                model.addAttribute("result", "クラスを変更しました");
            } else {
                model.addAttribute("result", "クラスを変更できませんでした");
            }
        } catch (DataAccessException e) {

            model.addAttribute("result", "更新失敗(トランザクションテスト)");
        }

        //クラス管理画面を表示
        return getClassManagement(model);
    }

    // 高校のリスト
    public Map<String, String> getSchoolList() {
        Map<String, String> selectMap = new LinkedHashMap<>();
        selectMap.put("1010", "鶴見");
        selectMap.put("1040", "横浜翠嵐");
        selectMap.put("1050", "城郷");
        selectMap.put("1060", "港北");
        selectMap.put("1070", "新羽");
        selectMap.put("1080", "岸根");
        selectMap.put("1090", "横浜市立東");
        selectMap.put("1100", "新栄");
        selectMap.put("1110", "川和");
        selectMap.put("1120", "市ヶ尾");
        selectMap.put("1130", "霧が丘");
        selectMap.put("1149", "白山 美術");
        selectMap.put("1160", "荏田");
        selectMap.put("1170", "元石川");
        selectMap.put("1180", "希望ヶ丘");
        selectMap.put("1190", "旭");
        selectMap.put("1220", "松陽");
        selectMap.put("1250", "瀬谷");
        selectMap.put("1260", "瀬谷西");
        selectMap.put("1270", "横浜平沼");
        selectMap.put("1280", "光陵");
        selectMap.put("1290", "保土ヶ谷");
        selectMap.put("1300", "舞岡");
        selectMap.put("1320", "上矢部");
        selectMap.put("1321", "上矢部 美術");
        selectMap.put("1330", "金井");
        selectMap.put("1350", "横浜市立戸塚");
        selectMap.put("1351", "横浜市立戸塚 音楽");
        selectMap.put("1360", "横浜市立桜丘");
        selectMap.put("1440", "柏陽");
        selectMap.put("1460", "横浜市立南");
        selectMap.put("1470", "横浜緑ヶ丘");
        selectMap.put("1540", "横浜市立金沢");
        selectMap.put("1640", "百合ヶ丘");
        selectMap.put("1830", "鎌倉");
        selectMap.put("1840", "七里ガ浜");
        selectMap.put("1850", "大船");
        selectMap.put("1860", "深沢");
        selectMap.put("1870", "湘南");
        selectMap.put("1890", "藤沢西");
        selectMap.put("1930", "湘南台");
        selectMap.put("2200", "厚木");
        selectMap.put("2210", "厚木東");
        selectMap.put("2250", "海老名");
        selectMap.put("2260", "有馬");
        selectMap.put("2280", "大和");
        selectMap.put("2290", "大和南");
        selectMap.put("2300", "大和西");
        selectMap.put("2320", "座間");
        selectMap.put("2350", "綾瀬");
        selectMap.put("2360", "綾瀬西");
        selectMap.put("2380", "上鶴間");
        selectMap.put("2530", "相原 畜産科学");
        selectMap.put("2531", "相原 食品科学");
        selectMap.put("2532", "相原 環境緑地");
        selectMap.put("2533", "相原 総合ビジネス");
        selectMap.put("2540", "中央農業 園芸科学");
        selectMap.put("2541", "中央農業 陸三科学");
        selectMap.put("2542", "中央農業 農業総合");
        selectMap.put("2550", "神奈川工業 機械");
        selectMap.put("2551", "神奈川工業 建設");
        selectMap.put("2552", "神奈川工業 電気");
        selectMap.put("2553", "神奈川工業 デザイン");
        selectMap.put("2560", "商工 総合技術");
        selectMap.put("2561", "商工 総合ビジネス");
        selectMap.put("2690", "横浜市立戸塚 音楽");
        selectMap.put("2691", "横浜市立桜丘");
        selectMap.put("2692", "柏陽");
        selectMap.put("2693", "横浜市立南");
        selectMap.put("2694", "横浜緑ヶ丘");
        selectMap.put("2695", "横浜市立金沢");
        selectMap.put("2740", "百合ヶ丘");
        selectMap.put("2741", "鎌倉");
        selectMap.put("2742", "七里ガ浜");
        selectMap.put("2790", "大船");
        selectMap.put("2791", "深沢");
        return selectMap;
    }
}
