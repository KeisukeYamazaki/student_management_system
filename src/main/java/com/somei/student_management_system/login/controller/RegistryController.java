package com.somei.student_management_system.login.controller;

import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.opencsv.exceptions.CsvException;
import com.somei.student_management_system.MyTestApp1;
import com.somei.student_management_system.login.bean.ByOneStudentRegistryProcessing;
import com.somei.student_management_system.login.bean.IOCsv;
import com.somei.student_management_system.login.bean.RecordRegistry;
import com.somei.student_management_system.login.bean.excelProcessing;
import com.somei.student_management_system.login.domain.model.ImportPracticeExam;
import com.somei.student_management_system.login.domain.model.PracticeExam;
import com.somei.student_management_system.login.domain.model.RegularExam;
import com.somei.student_management_system.login.domain.model.SchoolRecord;
import com.somei.student_management_system.login.domain.model.SchoolRecordWithName;
import com.somei.student_management_system.login.domain.model.SessionData;
import com.somei.student_management_system.login.domain.model.Student;
import com.somei.student_management_system.login.domain.service.NumericDataService;
import com.somei.student_management_system.login.domain.service.StudentService;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Controller
public class RegistryController {

    @Autowired
    excelProcessing excelProcessing;

    @Autowired
    NumericDataService numericDataService;

    @Autowired
    IOCsv ioCsv;

    @Autowired
    RecordRegistry recordRegistry;

    @Autowired
    StudentService studentService;

    @Autowired
    ByOneStudentRegistryProcessing byOneStudentRegistryProcessing;

    @Autowired
    SessionData sessionData;

    @Autowired
    MyTestApp1 myTestApp1;


    /**
     * 各種登録画面のGETメソッド.
     *
     * @param model モデル
     * @return 各種登録画面に遷移
     */
    @GetMapping("/registry")
    public String getRegistry(Model model) {

        //コンテンツ部分に生徒一覧を表示するための文字列を登録
        model.addAttribute("contents", "login/registry :: registry_contents");

        return "login/homeLayout";
    }

    /**
     * 成績登録画面のGETメソッド.
     *
     * @param model モデル
     * @return 成績登録方法選択の画面に遷移
     */
    @GetMapping("/registry/schoolRecord")
    public String getRegistrySchoolRecord(Model model) {

        //コンテンツ部分に生徒一覧を表示するための文字列を登録
        model.addAttribute("contents", "login/registrySchoolRecord :: registrySchoolRecord_contents");

        return "login/homeLayout";
    }

    /**
     * 成績登録画面：登録方法のPOSTメソッド.
     *
     * @param way   学年別・ファイルで一括の方法のいずれか
     * @param model モデル
     * @return いずれかの方法を表示する画面に遷移
     */
    @PostMapping("/registry/schoolRecord/way")
    public String getRegistrySchoolRecordWay(@RequestParam("radioName") String way, Model model) {

        // 登録方法によってmodelに登録するものを変える
        if (way.equals("ByGrade")) {

            // 学年別の場合
            model.addAttribute("ByGrade", true);

        } else {

            // 一括別の場合
            model.addAttribute("AllatOnece", true);
        }

        //コンテンツ部分に生徒一覧を表示するための文字列を登録
        model.addAttribute("contents", "login/registrySchoolRecord :: registrySchoolRecord_contents");

        return "login/homeLayout";
    }

    /**
     * 学年別成績登録画面
     *
     * @param school   対象の校舎
     * @param grade    対象の学年
     * @param termName 対象の学期
     * @param model    モデル
     * @return 成績登録画面に遷移
     */
    @PostMapping("/registry/schoolRecord/way/ByGrade")
    public String getRegistrySchoolRecordWay(@RequestParam("school") String school,
                                             @RequestParam("grade") String grade,
                                             @RequestParam("termName") String termName,
                                             Model model) {

        List<SchoolRecordWithName> recordList = numericDataService.selectRecordMany(school, grade, termName);

        //コンテンツ部分に生徒一覧を表示するための文字列を登録
        model.addAttribute("contents", "login/registrySchoolRecord :: registrySchoolRecord_contents");

        // 学年別の場合
        model.addAttribute("ByGrade", true);

        // 校舎・学年・学期
        model.addAttribute("school", school);
        model.addAttribute("grade", grade);
        model.addAttribute("termName", termName);

        // 成績のリスト
        model.addAttribute("recordList", recordList);

        return "login/homeLayout";
    }

    /**
     * 成績登録確認画面表示.
     *
     * @param multipartFile アップロードされたエクセルファイル
     * @param model         モデル
     * @return 登録確認画面に遷移
     */
    @PostMapping("/upload")
    public String postRecordFile(@RequestParam("file") MultipartFile multipartFile,
                                 Model model) throws IOException {

        // ファイルが空の場合はスプレッドシートで登録
        if (multipartFile.isEmpty()) {
            // スプレッドシートでの登録処理
        } else {
            // エクセルファイルの処理
            try {

                InputStream is = multipartFile.getInputStream();

                // エクセルファイルを成績リストに変換して取得
                List<SchoolRecordWithName> schoolRecordList = excelProcessing.readExcelFile(is);

                //確認画面へ渡す
                model.addAttribute("schoolRecordList", schoolRecordList);

            } catch (InvalidFormatException e) {
                // 不正な形式のファイルだった場合
                e.printStackTrace();

                //メッセージを表示する
                model.addAttribute("result", "正しい形式のファイルをアップロードしてください");

                return getRegistry(model);
            }
        }

        //コンテンツ部分に生徒一覧を表示するための文字列を登録
        model.addAttribute("contents", "login/schoolRecordConfirm :: schoolRecordConfirm_contents");

        return "login/homeLayout";
    }

    /**
     * 成績登録処理.
     *
     * @param model モデル
     * @return 各種登録画面に遷移
     */
    @PostMapping("/schoolRecordRegistry")
    public String RecordProcessing(@ModelAttribute SchoolRecordWithName schoolRecordWithName, Model model) {

        // リストに登録処理するためのリストを作成
        List<SchoolRecordWithName> SchoolRecordWithNameList = recordRegistry.recordRegistration(schoolRecordWithName);

        // 成績の登録処理
        try {

            //更新実行
            boolean result = numericDataService.updateRecordMany(SchoolRecordWithNameList);

            if (result == true) {
                model.addAttribute("result", "成績を登録しました");
            } else {
                model.addAttribute("result", "成績の登録に失敗しました");
            }

        } catch (DataAccessException e) {

            model.addAttribute("result", "更新失敗(トランザクションテスト)");

        }

        if(sessionData.getStr3() == null) {
            return getRegistry(model);
        }
        return postRegistryByOneStudentKind("schoolRecord" ,model);
    }

    /**
     * 戻るボタンの処理.
     *
     * @param model モデル
     * @return
     */
    @PostMapping(value = "/schoolRecordConfirm", params = "back")
    public String RecordProcessingBack(Model model) {

        return getRegistry(model);
    }

    /**
     * 定期試験登録画面のGETメソッド.
     *
     * @param model
     */
    @GetMapping("/registry/regularExam")
    public String getRegistryRegularExam(Model model) {

        //コンテンツ部分に生徒一覧を表示するための文字列を登録
        model.addAttribute("contents", "login/registryRegularExam :: registryRegularExam_contents");

        return "login/homeLayout";
    }

    /**
     * 模擬試験登録画面のGETメソッド.
     *
     * @param model モデル
     * @return 模試登録画面へ遷移
     */
    @GetMapping("/registry/practiceExam")
    public String getRegistryPracticeExam(Model model) {

        //コンテンツ部分に生徒一覧を表示するための文字列を登録
        model.addAttribute("contents", "login/registryPracticeExam :: registryPracticeExam_contents");

        return "login/homeLayout";
    }

    /**
     * 模擬試験登録画面のPOSTメソッド.
     *
     * @param multipartFile 模試のCSVデータ
     * @param model         モデル
     * @return 模試登録確認画面へ遷移
     */
    @PostMapping("/practiceCsvUpload")
    public String postRegistryPracticeExam(@RequestParam("file") MultipartFile multipartFile,
                                           Model model) {

        ImportPracticeExam ipe = new ImportPracticeExam();

        try (InputStream is = multipartFile.getInputStream();
             InputStreamReader ireader = new InputStreamReader(is, "UTF-8");
             Reader reader = new BufferedReader(ireader);) {

            List<ImportPracticeExam> exams = ioCsv.read(reader);

            boolean result = numericDataService.insertPracticeMany(exams);

            if (result == true) {
                model.addAttribute("result", "模擬試験データを登録しました");
            } else {
                model.addAttribute("result", "模擬試験データの登録に失敗しました");
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (CsvException e) {
            e.printStackTrace();
        } catch (DataAccessException e) {

            model.addAttribute("result", "更新失敗");

        }

        return getRegistry(model);
    }

    /*
    // 定期試験のグーグルシートでの登録メソッド
    @GetMapping
    public String getRegistryRegularExamByGoogle(Model model) {

        List<RegularExam> regularExamList = new ArrayList<>();

        try {
            regularExamList = myTestApp1.getRegularByGoogle();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (GeneralSecurityException e2) {
            e2.printStackTrace();
        }

        boolean result = numericDataService.insertRegularMany(regularExamList);

        if (result == true) {
            model.addAttribute("result", "定期試験データを登録しました");
        } else {
            model.addAttribute("result", "定期試験データの登録に失敗しました");
        }

        return getRegistryRegularExam(model);
    }
    */

    @PostMapping("/registryRegularExam")
    public String postRegistryRegularExam(@RequestParam("regulars")String regulars, Model model) {

        List<String> list = Arrays.asList(regulars.split("\r\n"));
        List<List<String>> strList = list.stream().map(l -> Arrays.asList(l.split("\t"))).collect(Collectors.toList());

        // データ登録用のリストを作成
        List<RegularExam> regularExamList = new ArrayList<>();

        for(int i = 1; i < strList.size(); i++) {
            if (strList.get(i).size() == 0) {
                // 空のリストは飛ばす
                continue;
            } else {
                RegularExam regularExam = new RegularExam();
                // 生徒情報の取得
                Student student = studentService.selectOne(myTestApp1.getId(strList.get(i).get(1)));
                // 試験Idをセットする
                if (student.getLocalSchool().contains("東野")) {
                    regularExam.setRegular_id(myTestApp1.searchRegularExamId(strList.get(0).get(3)));
                } else {
                    regularExam.setRegular_id(myTestApp1.searchRegularExamId(strList.get(0).get(2)));
                }
                regularExam.setStudentId(myTestApp1.getId(strList.get(i).get(1)));
                regularExam.setGrade(Integer.parseInt(strList.get(i).get(0)));
                regularExam.setExamYear(Integer.parseInt(strList.get(0).get(1)));
                regularExam.setEnglish(strList.get(i).get(2));
                regularExam.setMath(strList.get(i).get(3));
                regularExam.setJapanese(strList.get(i).get(4));
                regularExam.setScience(strList.get(i).get(5));
                regularExam.setSocialStudies(strList.get(i).get(6));
                regularExam.setMusic(strList.get(i).get(7));
                regularExam.setArt(strList.get(i).get(8));
                regularExam.setPe(strList.get(i).get(9));
                regularExam.setTech(strList.get(i).get(10));
                regularExam.setHome(strList.get(i).get(11));
                regularExam.setSumFive(strList.get(i).get(12));

                // リストに格納する
                regularExamList.add(regularExam);
            }
        }

        boolean result = numericDataService.insertRegularMany(regularExamList);

        if (result == true) {
            model.addAttribute("result", "定期試験データを登録しました");
        } else {
            model.addAttribute("result", "定期試験データの登録に失敗しました");
        }

        return getRegistryRegularExam(model);
    }

    /**
     * 個人登録画面のGETメソッド.
     *
     * @param model モデル
     * @return 個人登録画面へ遷移
     */
    @GetMapping("/registry/byOneStudent")
    public String getRegistryByOneStudent(Model model) {

        //コンテンツ部分に生徒一覧を表示するための文字列を登録
        model.addAttribute("contents", "login/registryByOneStudent :: registryByOneStudent_contents");

        return "login/homeLayout";
    }

    /**
     * 個人登録の当該学年生徒の表示メソッド
     *
     * @param grade ダウンロードしたい生徒の学年
     * @param model モデル
     * @return 当該学年の生徒一覧画面を表示
     */
    @PostMapping("/registry/byOneStudent/grade")
    public String postRegistryByOneStudentGrade(@RequestParam("grade") String grade,
                                                Model model) {

        // 学年のリストを生成
        List<Student> gradeList = studentService.selectManyByGrade(grade);

        //コンテンツ部分に生徒一覧を表示するための文字列を登録
        model.addAttribute("contents", "login/registryByOneStudent :: registryByOneStudent_contents");

        // 学年選択のドロップダウンリストに表示させる学年を指定
        model.addAttribute("grade", grade);

        //Modelに生徒リストを登録
        model.addAttribute("gradeList", gradeList);

        return "login/homeLayout";
    }

    /**
     * 個人登録画面GETメソッド
     *
     * @param studentId 登録する生徒のID
     * @param model     モデル
     * @return 生徒の各種登録画面
     */
    @GetMapping("/registry/byOneStudent/{id:.+}")
    public String getRegistryByOneStudent(@PathVariable("id") String studentId, Model model) {

        // 生徒IDをセッションに入れておく
        sessionData.setStr3(studentId);

        // 生徒情報の取得
        Student student = studentService.selectOne(studentId);

        // 学年の取得
        String grade = null;
        if (student.getGrade().equals("中３")) {
            grade = "中３";
        } else if (student.getGrade().equals("中２")) {
            grade = "中２";
        } else {
            grade = "中１";
        }

        //コンテンツ部分に生徒一覧を表示するための文字列を登録
        model.addAttribute("contents", "login/registryByOneStudent :: registryByOneStudent_contents");

        // 画面表示に必要な要素を送る
        model.addAttribute("studentEdit", true);
        model.addAttribute("grade", grade);

        return "login/homeLayout";
    }

    /**
     * 個人登録画面の登録種別POSTメソッド
     *
     * @param kind  登録するデータの種類
     * @param model モデル
     * @return 生徒の各種登録画面
     */
    @PostMapping("/registry/byOneStudent/kind")
    public String postRegistryByOneStudentKind(@RequestParam("radioName") String kind, Model model) {

        // 生徒情報の取得
        Student student = studentService.selectOne(sessionData.getStr3());

        if (kind.equals("schoolRecord")) {
            // 成績登録の場合
            // schoolRecord を true にして画面表示させる
            model.addAttribute("schoolRecord", true);

            // ID・学年・名前を送る
            model.addAttribute("studentId", student.getStudentId());
            model.addAttribute("studentGrade", student.getGrade());
            model.addAttribute("studentName", student.getStudentName());

            // 年度(西暦)を送る
            List<Integer> yearList = Arrays.asList(2017, 2018, 2019);
            if (student.getGrade().equals("中３")) {
                // 中３の場合は
                model.addAttribute("year1", yearList.get(0));
                model.addAttribute("year2", yearList.get(1));
                model.addAttribute("year3", yearList.get(2));
            } else if (student.getGrade().equals("中２")) {
                // 中２の場合
                model.addAttribute("year1", yearList.get(1));
                model.addAttribute("year2", yearList.get(2));
            } else {
                // 中１の場合
                model.addAttribute("year1", yearList.get(2));
            }

            // ２学期制か３学期制かを送る
            if (student.getLocalSchool().equals("東野中")) {
                // ２学期制の場合
                model.addAttribute("secondTerm", true);
            } else {
                // ３学期制の場合
                model.addAttribute("thirdTerm", true);
            }

            // 学年、学期のListを作成
            List<String> gradeList = Arrays.asList("中１", "中２", "中３");
            List<String> termList;
            if (student.getLocalSchool().equals("東野中")) {
                termList = Arrays.asList("前期", "後期");
            } else {
                termList = Arrays.asList("１学期", "２学期", "３学期");
            }

            // 成績のリストを取得
            List<SchoolRecord> recordList = numericDataService.selectRecordOne(student.getStudentId());

            int num = 0;   // schoolRecordの識別番号
            for (int i = 0; i < gradeList.size(); i++) {
                for (int j = 0; j < termList.size(); j++) {
                    SchoolRecord record = byOneStudentRegistryProcessing
                            .recordClassification(recordList, gradeList.get(i), termList.get(j));
                    model.addAttribute("schoolRecord" + num, record);
                    num++;
                }
            }

        } else if (kind.equals("regularExam")) {
            // 定期試験の結果登録の場合
            // regularExam なら true にする
            model.addAttribute("regularExam", true);
        } else {
            // practiceExam を true にする
            model.addAttribute("practiceExam", true);

            // 模試結果リストの取得
            List<PracticeExam> practiceExamList = numericDataService.selectPracticeOne(student.getStudentId());

            // 送る
            model.addAttribute("practiceExam", practiceExamList);
        }

        //コンテンツ部分に生徒一覧を表示するための文字列を登録
        model.addAttribute("contents", "login/registryByOneStudent :: registryByOneStudent_contents");

        // 画面表示に必要な要素を送る
        model.addAttribute("studentEdit", true);
        model.addAttribute("grade", student.getGrade());

        return "login/homeLayout";
    }

    /**
     * 個人登録模試登録メソッド
     *
     * @param exam  模試結果
     * @param model モデル
     * @return 生徒の各種登録画面
     */
    @PostMapping("/practiceExam/ByOneStudent")
    public String postPracticeExamByOneStudent(@ModelAttribute ImportPracticeExam exam, Model model){

        List<ImportPracticeExam> practiceExamList = recordRegistry.practiceRegistration(exam);

        try {

            boolean result = numericDataService.insertPracticeMany(practiceExamList);

            if (result == true) {
                model.addAttribute("result", "模擬試験データを登録しました");
            } else {
                model.addAttribute("result", "模擬試験データの登録に失敗しました");
            }

        } catch (DataAccessException e) {
            model.addAttribute("result", "更新失敗");
        }
        return postRegistryByOneStudentKind("practiceExam" ,model);
    }
}
