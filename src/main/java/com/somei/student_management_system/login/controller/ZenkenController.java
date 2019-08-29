package com.somei.student_management_system.login.controller;

import com.somei.student_management_system.login.domain.model.RecordExistence;
import com.somei.student_management_system.login.domain.model.Zenken;
import com.somei.student_management_system.login.domain.service.ZenkenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Controller
public class ZenkenController {

    @Autowired
    ZenkenService zenkenService;

    /**
     * 全県模試ファイル作成画面のGETメソッド.
     */
    @GetMapping("/zenken")
    public String getZenken(Model model) {

        //コンテンツ部分に生徒一覧を表示するための文字列を登録
        model.addAttribute("contents", "login/zenken :: zenken_contents");

        return "login/homeLayout";
    }

    /**
     * 全県模試ファイル作成一覧表示メソッド.
     */
    @PostMapping(value = "/zenken", params = "show")
    public String postZenkenList(@RequestParam("school") String school,
                                 @RequestParam("grade") String grade,
                                 Model model) {

        // 対象の全県模試のリストを取得
        List<Zenken> zenkenList = zenkenService.selectMany(school, grade);

        // 対象の成績有無リストを取得
        List<RecordExistence> recordList = zenkenService.selectRecordExsitenceMany(school, grade);

        model.addAttribute("school", school);
        model.addAttribute("grade", grade);
        model.addAttribute("zenkenList", zenkenList);
        model.addAttribute("recordList", recordList);

        //コンテンツ部分に生徒一覧を表示するための文字列を登録
        model.addAttribute("contents", "login/zenken :: zenken_contents");

        return "login/homeLayout";
    }

    /**
     * 全県模試データ編集メソッド.
     */
    @PostMapping(value = "/zenken/csv", params = "edit")
    public String getZenkenEdit(@ModelAttribute Zenken zenken,
                                @RequestParam("school") String school,
                                @RequestParam("grade") String grade,
                                Model model) {

        // 対象の全県模試のリストを取得
        List<Zenken> zenkenList = zenkenService.selectMany(school, grade);

        // 市区町村のドロップダウンのマップ
        Map<String, String> cityMap = new LinkedHashMap<>();
        cityMap.put("瀬谷区", "114");
        cityMap.put("泉区", "116");
        cityMap.put("旭区", "112");
        cityMap.put("大和市", "213");

        model.addAttribute("zenkenList", zenkenList);
        model.addAttribute("cityMap", cityMap);

        // 更新後の画面遷移のために school と grade を送っておく
        model.addAttribute("school", school);
        model.addAttribute("grade", grade);

        //コンテンツ部分に生徒一覧を表示するための文字列を登録
        model.addAttribute("contents", "login/zenkenEdit :: zenkenEdit_contents");

        return "login/homeLayout";
    }

    /**
     * 全県模試データ更新メソッド.
     */
    @PostMapping("/zenkenEdit")
    public String postZenkenEdit(@ModelAttribute Zenken zenken,
                                 @RequestParam("school") String school,
                                 @RequestParam("grade") String grade,
                                 Model model) {

        // 送られてきたchangeの値をリストに格納
        List<String> idList = Arrays.asList(zenken.getId().split(","));
        List<String> numberList = Arrays.asList(zenken.getNumber().split(","));
        List<String> genderList = Arrays.asList(zenken.getGender().split(","));
        List<String> cityList = Arrays.asList(zenken.getCity().split(","));
        List<String> termList = Arrays.asList(zenken.getRecordTerm().split(","));

        // 渡すためのリストを作成
        List<Zenken> changeList = new ArrayList<>();

        for (int i = 0; i < idList.size(); i++) {
            Zenken inList = new Zenken();

            // Zenkenクラスにデータを代入してリストに格納
            inList.setId(idList.get(i));
            inList.setNumber(numberList.get(i));
            inList.setGender(genderList.get(i));
            inList.setEnrollment("1");
            inList.setPrefecture("14");
            inList.setCity(cityList.get(i));
            inList.setRecordTerm(termList.get(i));

            changeList.add(inList);
        }

        try {

            // 更新実行
            boolean result = zenkenService.updateMany(changeList);

            if (result == true) {
                model.addAttribute("result", "データを更新しました");
            } else {
                model.addAttribute("result", "更新できませんでした");
            }
        } catch (DataAccessException e) {

            model.addAttribute("result", "更新失敗");
        }

        //クラス管理画面を表示
        return postZenkenList(school, grade, model);
    }

    /**
     * 全県模試一括登録CSV出力メソッド.
     */
    @PostMapping(value = "/zenken/csv", params = "download")
    public ResponseEntity<byte[]> getZenkenCsv(@RequestParam("school") String school,
                                               @RequestParam("grade") String grade,
                                               Model model) {
        String fileName = null;

        // CSVをサーバーに保存する
        try {

            // CSVファイルを保存、ファイル名を取得
            fileName = zenkenService.makeCsv(school, grade);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        byte[] bytes = null;

        try {

            // サーバーに保存されているCSVファイルをbyteで取得する
            bytes = zenkenService.getFile(fileName);

        } catch (IOException e) {
            e.printStackTrace();
        }

        try {

            //HTTPヘッダーの設定
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Type", "text/csv; charset=UTF-8");
            headers.setContentDispositionFormData("filename", fileName);

            // CSVファイルを戻す
            return new ResponseEntity<>(bytes, headers, HttpStatus.OK);

        } finally {

            // 作成したファイルを削除する
            File file = new File(fileName);
            file.delete();

        }
    }
}
