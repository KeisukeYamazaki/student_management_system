package com.somei.student_management_system.login.controller;

import com.opencsv.exceptions.CsvException;
import com.somei.student_management_system.login.bean.IOCsv;
import com.somei.student_management_system.login.bean.RecordRegistry;
import com.somei.student_management_system.login.bean.excelProcessing;
import com.somei.student_management_system.login.domain.model.ImportPracticeExam;
import com.somei.student_management_system.login.domain.model.SchoolRecord;
import com.somei.student_management_system.login.domain.model.SchoolRecordWithName;
import com.somei.student_management_system.login.domain.service.NumericDataService;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

    /**
     * 各種登録画面のGETメソッド.
     *
     * @param model モデル
     * @return 各種登録画面に遷移
     */
    @GetMapping("registry")
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
    @GetMapping("registry/schoolRecord")
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
    @PostMapping("registry/schoolRecord/way")
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
     * @param school 対象の校舎
     * @param grade 対象の学年
     * @param termName 対象の学期
     * @param model モデル
     * @return 成績登録画面に遷移
     */
    @PostMapping("registry/schoolRecord/way/ByGrade")
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
    @PostMapping("upload")
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
    @PostMapping("schoolRecordRegistry")
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

        return getRegistry(model);
    }

    /**
     * 戻るボタンの処理.
     *
     * @param model モデル
     * @return
     */
    @PostMapping(value = "schoolRecordConfirm", params = "back")
    public String RecordProcessingBack(Model model) {

        return getRegistry(model);
    }

    /**
     * 定期試験登録画面のGETメソッド.
     *
     * @param model
     */
    @GetMapping("registry/regularExam")
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
    @GetMapping("registry/practiceExam")
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
    @PostMapping("practiceCsvUpload")
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

            model.addAttribute("result", "更新失敗(トランザクションテスト)");

        }

        return getRegistry(model);
    }
}
