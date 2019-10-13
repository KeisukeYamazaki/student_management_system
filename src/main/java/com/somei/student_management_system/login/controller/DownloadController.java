package com.somei.student_management_system.login.controller;

import com.ibm.icu.text.Transliterator;
import com.somei.student_management_system.login.bean.MakeExcelNameSheet;
import com.somei.student_management_system.login.bean.CreateZip;
import com.somei.student_management_system.login.bean.ExcelMeetingSheet;
import com.somei.student_management_system.login.domain.model.FuturePath;
import com.somei.student_management_system.login.domain.model.FuturePathWithData;
import com.somei.student_management_system.login.domain.model.NameList;
import com.somei.student_management_system.login.domain.model.SessionData;
import com.somei.student_management_system.login.domain.model.Student;
import com.somei.student_management_system.login.domain.service.StudentService;
import com.somei.student_management_system.login.domain.service.ZenkenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

@Controller
public class DownloadController {

    @Autowired
    StudentService studentService;

    @Autowired
    CreateZip createZip;

    @Autowired
    ZenkenService zenkenService;

    @Autowired
    signupController signupController;

    @Autowired
    ExcelMeetingSheet excelMeetingSheet;

    @Autowired
    SessionData sessionData;

    @Autowired
    MakeExcelNameSheet makeExcelNameSheet;

    /**
     * 各種ダウンロード画面のGETメソッド.
     */
    @GetMapping("/downloads")
    public String getDownloads(Model model) {

        //コンテンツ部分に生徒一覧を表示するための文字列を登録
        model.addAttribute("contents", "login/downloads :: downloads_contents");

        return "login/homeLayout";
    }

    /**
     * 面談シートダウンロード画面のGETメソッド.
     *
     * @param model モデル
     * @return 学年の選択リストを表示
     *
     */
    @GetMapping("/downloads/meetingSheet")
    public String getMeetingSheetDownload(Model model) {

        // 学年選択のドロップダウンリストを表示させる
        model.addAttribute("meetingSheet", true);

        //コンテンツ部分に生徒一覧を表示するための文字列を登録
        model.addAttribute("contents", "login/downloads :: downloads_contents");

        return "login/homeLayout";
    }

    /**
     * 面談シートダウンロード学年の表示メソッド
     *
     * @param grade ダウンロードしたい生徒の学年
     * @param model モデル
     * @return 当該学年の生徒一覧画面を表示
     *
     */
    @PostMapping("/meetingSheet/grade")
    public String postMeetingSheetGrade(@RequestParam("grade") String grade,
                                        Model model) {

        // 学年のリストを生成
        List<Student> gradeList = studentService.selectManyByGrade(grade);

        //コンテンツ部分に生徒一覧を表示するための文字列を登録
        model.addAttribute("contents", "login/downloads :: downloads_contents");

        // 学年選択のドロップダウンリストを表示させる
        model.addAttribute("meetingSheet", true);

        // 学年選択のドロップダウンリストの学年を指定
        if (grade.equals("中３")) {
            grade = "3rd";
        } else if (grade.equals("中２")) {
            grade = "2nd";
        } else {
            grade = "1st";
        }
        model.addAttribute("grade", grade);

        //Modelに生徒リストを登録
        model.addAttribute("gradeList", gradeList);

        return "login/homeLayout";
    }

    /**
     * 面談シート進路情報編集メソッド
     *
     * @param studentId 面談シートに記載する生徒のID
     * @param model モデル
     * @return 生徒の進路情報編集画面
     */
    @GetMapping("/meetingSheetEdit/{id:.+}")
    public String getmeetingSheetEdit(@PathVariable("id")String studentId,  Model model) {

        // 生徒情報の取得
        Student student = studentService.selectOne(studentId);

        // 学年の取得
        String grade = null;
        if(student.getGrade().equals("中３")) {
            grade = "3rd";
        } else if(student.getGrade().equals("中２")) {
            grade = "2nd";
        } else {
            grade = "1st";
        }

        // 進路データの生成
        FuturePathWithData futurePathData = studentService.selectPathDataOne(studentId);

        //コンテンツ部分に生徒一覧を表示するための文字列を登録
        model.addAttribute("contents", "login/downloads :: downloads_contents");

        // 進路情報を送る
        model.addAttribute("futurePathData", futurePathData);

        // 画面表示に必要な要素を送る
        model.addAttribute("studentEdit", true);
        model.addAttribute("grade", grade);

        return "login/homeLayout";
    }

    /**
     * 面談シート進路情報から戻るメソッド
     *
     * @param futurePathData 生徒の進路情報
     * @param model          モデル
     * @return 当該学年の生徒一覧画面を表示
     */
    @PostMapping(value = "/meetingSheetEdit", params = "back")
    public String postMeetingSheetEditBack(@ModelAttribute FuturePathWithData futurePathData, Model model) {

        // 生徒情報の取得
        Student student = studentService.selectOne(futurePathData.getStudentId());

        return postMeetingSheetGrade(student.getGrade(), model);
    }

    /**
     * 面談シート編集画面からダウンロードするメソッド
     *
     * @param futurePathData 生徒の進路情報
     * @param model モデル
     * @param schoolId1 追加で記載する私立高校１
     * @param schoolId2 追加で記載する私立高校２
     * @return 当該学年の生徒一覧画面を表示
     */
    @PostMapping(value = "/meetingSheetEdit", params = "download")
    public ResponseEntity<byte[]> postMeetingSheetEditDownload(@ModelAttribute FuturePathWithData futurePathData,
                                                               @RequestParam(name = "additionalPrivateSchool1", required = false) String schoolId1,
                                                               @RequestParam(name = "additionalPrivateSchool2", required = false) String schoolId2,
                                                               Model model) {

        // セッションに２つの高校を加える
        sessionData.setStr1(schoolId1);
        sessionData.setStr2(schoolId2);

        // 生徒情報の取得
        Student student = studentService.selectOne(futurePathData.getStudentId());

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
                System.out.println("進路更新処理：" + student.getStudentName());

            } else {
                model.addAttribute("result", "更新に失敗しました");
            }

        } catch (DataAccessException e) {

            model.addAttribute("result", "更新失敗(トランザクションテスト)");

        }

        // 面談シートダウンロードメソッドを実行
        return getMeetingSheetDownload(futurePathData.getStudentId(), model);
    }


    /**
     * 面談シートダウンロードメソッド
     *
     * @param studentId 面談シートに記載する生徒のID
     * @param model モデル
     * @return エクセル面談シート
     */
    @GetMapping("/meetingSheetDownload/{id:.+}")
    public ResponseEntity<byte[]> getMeetingSheetDownload(@PathVariable("id") String studentId, Model model) {
        // 生徒情報の取得
        Student student = studentService.selectOne(studentId);

        // 生徒名（カタカナ）をひらながなに変換
        String reName = student.getNameRuby().replace("　", " ");
        Transliterator transliterator = Transliterator.getInstance("Katakana-Hiragana");
        String name = transliterator.transliterate(reName);

        // 生徒名（ひらがな）をローマ字に変換
        transliterator = Transliterator.getInstance("Hiragana-Latin");
        String latinName = transliterator.transliterate(name);

        // ファイル名作成用に日付を取得
        LocalDate date = LocalDate.now();

        // ファイル名を決める
        String fileName = DateTimeFormatter.ofPattern("yyyyMMdd").format(date) + latinName + ".xlsx";

        // エクセルファイルの作成
        excelMeetingSheet.MakeExcelMeetingSheet(student, fileName);

        // サーバーに保存されているエクセルファイルをbyteで取得する
        byte[] bytes = null;

        try {

            bytes = zenkenService.getFile(fileName);

        } catch (IOException e) {
            e.printStackTrace();
        }

        try {

            //HTTPヘッダーの設定
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Type", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet; charset=UTF-8");
            headers.setContentDispositionFormData("filename", fileName);

            // ファイルをダウンロード
            return new ResponseEntity<>(bytes, headers, HttpStatus.OK);

        } finally {

            // ファイルの削除
            File file = new File(fileName);
            file.delete();
        }
    }

    /**
     * 中学生名簿ダウンロード画面のGETメソッド
     */
    @GetMapping("/downloads/nameListJunior")
    public String getNameListJuniorDownload(Model model) {

        // クラス別の名前リストを取得
        List<List<NameList>> nameList = studentService.makeClassNameList();

        // クラス名のリストを作成
        List<String> classNameList = Arrays.asList("class3A", "class3B", "class3C", "class2A", "class2B", "class2C", "class1A", "class1B");


        //コンテンツ部分に表示するクラスを登録
        for (int i = 0; i < classNameList.size(); i++) {

            model.addAttribute(classNameList.get(i), nameList.get(i));
        }

        //コンテンツ部分に生徒一覧を表示するための文字列を登録
        model.addAttribute("contents", "login/downloads :: downloads_contents");

        return "login/homeLayout";
    }

    /**
     * 小学生名簿ダウンロード画面のGETメソッド
     */
    @GetMapping("/downloads/nameListElementary")
    public String getNameListElementaryDownload(Model model) {

        // クラス別の名前リストを取得
        List<List<NameList>> nameList = studentService.makeClassNameList();

        // クラス名のリストを作成
        List<String> classNameList = Arrays.asList("class3A", "class3B", "class3C", "class2A", "class2B", "class2C", "class1A", "class1B",
                "classE6", "classE6h", "classE6s", "classE5", "classE5h", "classE5s", "classE4h", "classE4s", "classE3h", "classE3s");

        //コンテンツ部分に表示するクラスを登録
        for (int i = 8; i < classNameList.size(); i++) {

            model.addAttribute(classNameList.get(i), nameList.get(i));
        }


        //コンテンツ部分に生徒一覧を表示するための文字列を登録
        model.addAttribute("contents", "login/downloads :: downloads_contents");

        return "login/homeLayout";
    }

    /**
     * PDF名簿ダウンロードメソッド
     *
     * @param className
     * @param model
     * @return
     */
    @PostMapping(value = "/downloads", params = "pdf")
    public ResponseEntity<byte[]> downloadNameList(@RequestParam("className") String className,
                                                   Model model) {

        // 送られてきたクラスを分割してリスト化
        List<String> classList = Arrays.asList(className.split(","));

        // ZIPファイルのファイル名作成用に日付を取得
        LocalDate date = LocalDate.now();

        // ZIPファイル名を決める
        String zipFilename = DateTimeFormatter.ofPattern("yyyyMMdd").format(date) + "NameList.zip";

        // Zipファイルを作成・削除リストの受け取り
        List<String> deleteList = createZip.createZip(classList, zipFilename);

        // サーバーに保存されているZIPファイルをbyteで取得する
        byte[] bytes = null;

        try {

            bytes = zenkenService.getFile(zipFilename);

        } catch (IOException e) {
            e.printStackTrace();
        }

        try {

            //HTTPヘッダーの設定
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Type", "application/zip; charset=UTF-8");
            headers.setContentDispositionFormData("filename", zipFilename);

            // ZIPファイルを戻す
            return new ResponseEntity<>(bytes, headers, HttpStatus.OK);

        } finally {

            // サーバに保存したファイルを削除する
            // PDFファイルの削除
            for (String name : deleteList) {

                File file = new File(name);

                // ファイルが存在していたら削除
                if (file.exists()) {
                    file.delete();
                }
            }

            // ZIPファイルの削除
            File zipFile = new File(zipFilename);
            zipFile.delete();
        }
    }

    /**
     * エクセル名簿ダウンロードメソッド
     *
     * @param className
     * @param model
     * @return
     */
    @PostMapping(value = "/downloads", params = "excel")
    public ResponseEntity<byte[]> downloadExcelNameSheet(@RequestParam("className") String className,
                                                         Model model) {
        // 送られてきたクラスを分割してリスト化
        List<String> classList = Arrays.asList(className.split(","));

        // ファイル名作成用に日付を取得
        LocalDate date = LocalDate.now();

        // ファイル名を決める
        String fileName = DateTimeFormatter.ofPattern("yyyyMMdd").format(date) + "NameSheet.xlsx";

        // エクセル名簿の作成
        makeExcelNameSheet.MakeExcelNameSheet(classList, fileName);

        // サーバーに保存されているエクセルファイルをbyteで取得する
        byte[] bytes = null;

        try {
            bytes = zenkenService.getFile(fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            //HTTPヘッダーの設定
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Type", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet; charset=UTF-8");
            headers.setContentDispositionFormData("filename", fileName);
            // ファイルをダウンロード
            return new ResponseEntity<>(bytes, headers, HttpStatus.OK);
        } finally {
            // ファイルの削除
            File file = new File(fileName);
            file.delete();
        }
    }

    @PostMapping(value = "downloads", params = "spreadsheet")
    public String writeNameListInSpreadsheet() throws GeneralSecurityException, IOException {

        return null;
    }

}


