package com.somei.student_management_system.login.controller;

import com.somei.student_management_system.login.bean.CreateZip;
import com.somei.student_management_system.login.bean.MakePdf;
import com.somei.student_management_system.login.domain.model.NameList;
import com.somei.student_management_system.login.domain.service.StudentService;
import com.somei.student_management_system.login.domain.service.ZenkenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Controller
public class DownloadController {

    @Autowired
    StudentService studentService;

    @Autowired
    MakePdf makePdf;

    @Autowired
    CreateZip createZip;

    @Autowired
    ZenkenService zenkenService;

    /**
     * 各種ダウンロード画面のGETメソッド.
     */
    @GetMapping("downloads")
    public String getDownloads(Model model) {

        //コンテンツ部分に生徒一覧を表示するための文字列を登録
        model.addAttribute("contents", "login/downloads :: downloads_contents");

        return "login/homeLayout";
    }

    /**
     * 中学生名簿ダウンロード画面のGETメソッド
     */
    @GetMapping("downloads/nameListJunior")
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
    @GetMapping("downloads/nameListElementary")
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
     * 名簿ダウンロードメソッド
     *
     * @param className
     * @param model
     * @return
     */
    @PostMapping("downloads")
    public ResponseEntity<byte[]> downloadNameList(@RequestParam("className") String className,
                                   Model model) {

        // 送られてきたクラスを分割してリスト化
        List<String> classList = Arrays.asList(className.split(","));

        // リストの長さの分のFile配列を作成
        File[] files = new File[classList.size()];

        // PDFを作成して保存
        for (int i = 0; i < classList.size(); i++) {

            String filename = makePdf.makePdf(classList.get(i));

            files[i] = new File(filename);
        }

        // zipファイルをサーバに保存、ファイル名を取得
        String zipFileName = createZip.createZip(files);

        byte[] bytes = null;

        try {

            // サーバーに保存されているZIPファイルをbyteで取得する
            bytes = zenkenService.getFile(zipFileName);

        } catch (IOException e) {
            e.printStackTrace();
        }

        try {

            //HTTPヘッダーの設定
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Type", "application/zip; charset=UTF-8");
            headers.setContentDispositionFormData("filename", zipFileName);

            // ZIPファイルを戻す
            return new ResponseEntity<>(bytes, headers, HttpStatus.OK);

        } finally {

            // サーバに保存したファイルを削除する
            for(File file : files) {

                // PDFファイルの削除
                file.delete();
            }

            // ZIPファイルの削除
            File zipFile = new File(zipFileName);
            zipFile.delete();

        }
    }

}


