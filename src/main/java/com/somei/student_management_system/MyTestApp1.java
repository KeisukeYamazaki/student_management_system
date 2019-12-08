package com.somei.student_management_system;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.somei.student_management_system.login.domain.model.RegularExam;
import com.somei.student_management_system.login.domain.model.Student;
import com.somei.student_management_system.login.domain.service.StudentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.bridge.SLF4JBridgeHandler;

import com.google.api.services.drive.DriveScopes;
import com.google.api.services.sheets.v4.SheetsScopes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Googleスプレッドシートへの書き込みを行う。
 */
@Component
public class MyTestApp1 {

    @Autowired
    StudentService studentService;

    private static final Logger LOGGER = LoggerFactory.getLogger(MyTestApp1.class);

    // Application name.
    private static final String APPLICATION_NAME = "MyTestApp1";

    // Return reader for Google client secret.
    private static Reader getClientSecretReader() {
        return new InputStreamReader(MyTestApp1.class.getResourceAsStream("/google_client_secret.json"));
    }

    // Google authorization scopes required by this application.
    // If modifying these scopes, delete your previously saved credentials.
    private static final List<String> AUTHORIZATION_SCOPE_LIST = Arrays.asList(DriveScopes.DRIVE, SheetsScopes.SPREADSHEETS);

    // Directory to store Google user credentials for this application.
    private static final String CREDENTIAL_STORE_DIRECTORY = System.getProperty("user.home") + "/.google_credentials/" + APPLICATION_NAME;

    public List<RegularExam> getRegularByGoogle() throws IOException, GeneralSecurityException {
        SLF4JBridgeHandler.removeHandlersForRootLogger();
        SLF4JBridgeHandler.install();

        final String folderName = "フォルダ1";
        final String spreadsheetName = "2019_中学部_定期試験結果まとめ";
        final String worksheetName = "２学期期末・後期中間";

        // Build a new authorized API client service.
        GoogleService googleService = getGoogleService();
        SheetsWrapper sheetsWrapper = googleService.getSheetsWrapperWithWorksheet(folderName, spreadsheetName, worksheetName);
        int lastRowNumWithValue = sheetsWrapper.getLastRowNumberWithValue(worksheetName, 1);

        /*
        // 値の入っている最後の行の次の行から書き込み
        Object[][] values = { { 1, "A" }, { 2.1D, "B" }, { 2.50E-3, "C" } };
        sheetsWrapper.setValues(worksheetName, 1, lastRowNumWithValue + 1, values);
        LOGGER.info("書き込みました。");
        */

        //値を取得
        List<List<Object>> valueList1 = sheetsWrapper.getValues("印刷用(２学期期末・後期中間)", 17, 2, 30, 100);

        // Stringのリストに変換する
        List<List<String>> strList = valueList1.stream()
                .map(list -> list.stream().map(obj -> String.valueOf(obj)).collect(Collectors.toList()))
                .collect(Collectors.toList());

        // データ登録用のリストを作成
        List<RegularExam> regularExamList = new ArrayList<>();

        for(int i = 1; i < strList.size(); i++) {
            if(strList.get(i).size() == 0) {
                // 空のリストは飛ばす
                continue;
            } else {
                RegularExam regularExam = new RegularExam();
                // 生徒情報の取得
                Student student = studentService.selectOne(getId(strList.get(i).get(1)));
                // 試験Idをセットする
                if(student.getLocalSchool().contains("東野")) {
                    regularExam.setRegular_id(searchRegularExamId(strList.get(0).get(3)));
                } else {
                    regularExam.setRegular_id(searchRegularExamId(strList.get(0).get(2)));
                }
                regularExam.setStudentId(getId(strList.get(i).get(1)));
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
        return regularExamList;
    }

    static GoogleService getGoogleService() {
        return new GoogleService(getClientSecretReader(), AUTHORIZATION_SCOPE_LIST, CREDENTIAL_STORE_DIRECTORY, APPLICATION_NAME);
    }

    /**
     * 全生徒のIDと名前のリストの取得メソッド
     *
     * @param studentName 生徒の名前
     * @return id 生徒のid
     */
    public String getId(String studentName) {
        String replacedName = studentName.replaceAll(" ", "").replaceAll("　", "");
        String id = null;
        List<Student> idNameList = studentService.selectIdName();

        for(Student student : idNameList) {
            String name1 = student.getStudentName().replaceAll(" ", "").replaceAll("　", "");
            String name2 = name1.replace("﨑", "崎").replace( "髙",  "高");
            if(name1.equals(replacedName) || name2.equals(replacedName)) {
                id = student.getStudentId();
                break;
            }
        }
        return id;
    }

    public String searchRegularExamId(String testName) {
        String regularExamId = null;
        switch (testName) {
            case "1学期中間":
                regularExamId = "1";
                break;
            case "前期中間":
                regularExamId = "2";
                break;
            case "1学期期末":
                regularExamId = "3";
                break;
            case "前期期末":
                regularExamId = "4";
                break;
            case "2学期中間":
                regularExamId = "5";
                break;
            case "後期中間":
                regularExamId = "6";
                break;
            case "2学期期末":
                regularExamId = "7";
                break;
            case "学年末":
                regularExamId = "8";
                break;
        }
        return regularExamId;
    }
}