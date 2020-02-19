package com.somei.student_management_system;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.somei.student_management_system.login.bean.RegistryProcessing;
import com.somei.student_management_system.login.controller.RegistryController;
import com.somei.student_management_system.login.domain.model.RegularExam;
import com.somei.student_management_system.login.domain.model.Student;
import com.somei.student_management_system.login.domain.service.StudentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.bridge.SLF4JBridgeHandler;

import com.google.api.services.drive.DriveScopes;
import com.google.api.services.sheets.v4.SheetsScopes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;

/**
 * Googleスプレッドシートへの書き込みを行う。
 */
@Component
public class GoogleSpreadSheetMethods {

    @Autowired
    StudentService studentService;

    @Autowired
    CreateCredentialFile createCredentialFile;

    private static final Logger LOGGER = LoggerFactory.getLogger(GoogleSpreadSheetMethods.class);

    // Application name.
    private static final String APPLICATION_NAME = "GoogleSpreadSheetMethods";

    // Return reader for Google client secret.
    private static Reader getClientSecretReader() {
        return new InputStreamReader(GoogleSpreadSheetMethods.class.getResourceAsStream("/google_client_secret.json"));
    }

    // Google authorization scopes required by this application.
    // If modifying these scopes, delete your previously saved credentials.
    private static final List<String> AUTHORIZATION_SCOPE_LIST = Arrays.asList(DriveScopes.DRIVE, SheetsScopes.SPREADSHEETS);

    // Directory to store Google user credentials for this application.
    private static final String CREDENTIAL_STORE_DIRECTORY = System.getProperty("user.dir") + "/.google_credentials/" + APPLICATION_NAME;

    public List<RegularExam> getRegularByGoogle() throws IOException, GeneralSecurityException {
        SLF4JBridgeHandler.removeHandlersForRootLogger();
        SLF4JBridgeHandler.install();

        final String FOLDER_NAME = "フォルダ1";
        final String SPREADSHEET_NAME = "2019_中学部_定期試験結果まとめ";
        final String WORKSHEET_NAME = "学年末";

        // Build a new authorized API client service.
        GoogleService googleService = getGoogleService();
        SheetsWrapper sheetsWrapper = googleService.getSheetsWrapperWithWorksheet(FOLDER_NAME, SPREADSHEET_NAME, WORKSHEET_NAME);
        int lastRowNumWithValue = sheetsWrapper.getLastRowNumberWithValue(WORKSHEET_NAME, 1);

        /*
        // 値の入っている最後の行の次の行から書き込み
        Object[][] values = { { 1, "A" }, { 2.1D, "B" }, { 2.50E-3, "C" } };
        sheetsWrapper.setValues(worksheetName, 1, lastRowNumWithValue + 1, values);
        LOGGER.info("書き込みました。");
        */

        //値を取得
        List<List<Object>> valueList1 = sheetsWrapper.getValues("学年末", 4, 4, 27, 100);

        // Stringのリストに変換する
        List<List<String>> strList = valueList1.stream()
                .map(list -> list.stream().map(obj -> String.valueOf(obj)).collect(Collectors.toList()))
                .collect(Collectors.toList());

        // データ登録用のリストを作成
        List<RegularExam> regularExamList = new ArrayList<>();

        for (int i = 0; i < strList.size(); i++) {
            if (strList.get(i).size() == 0) {
                // 空のリストは飛ばす
                continue;
            } else {
                RegularExam regularExam = new RegularExam();
                // 生徒情報の取得
                Student student;
                try {
                    student = studentService.selectOne(getId(strList.get(i).get(0)));
                } catch (DataAccessException e) {
                    // 生徒情報を取得できなかった場合はループの最初に戻る
                    continue;
                }
                // 試験Idをセットする
                if (student.getLocalSchool().contains("東野")) {
                    try {
                        regularExam.setRegular_id(searchRegularExamId(WORKSHEET_NAME.split("・", 0)[1]));
                    } catch (IndexOutOfBoundsException e) {
                        regularExam.setRegular_id(searchRegularExamId("学年末"));
                    }
                } else {
                    try {
                        regularExam.setRegular_id(searchRegularExamId(WORKSHEET_NAME.split("・", 0)[0]));
                    } catch (IndexOutOfBoundsException e) {
                        regularExam.setRegular_id(searchRegularExamId("学年末"));
                    }
                }

                // 各科目の結果をセットする
                regularExam.setStudentId(student.getStudentId());
                regularExam.setGrade(changeNumFullToHalf(student.getGrade().substring(1, 2)));
                regularExam.setExamYear(String.valueOf(RegistryController.getSchoolYear()));
                List<String> sumFiveList = new ArrayList<>();  // ５科目合計を算出するためのリスト
                try {
                    String english = strList.get(i).get(13).equals("") ? null : strList.get(i).get(13);
                    regularExam.setEnglish(english);
                    sumFiveList.add(english);
                } catch (IndexOutOfBoundsException e) {
                    // 何もせずに飛ばす
                }
                try {
                    String math = strList.get(i).get(14).equals("") ? null : strList.get(i).get(14);
                    regularExam.setMath(math);
                    sumFiveList.add(math);
                } catch (IndexOutOfBoundsException e) {
                    // 何もせずに飛ばす
                }
                try {
                    String japanese = strList.get(i).get(15).equals("") ? null : strList.get(i).get(15);
                    regularExam.setJapanese(japanese);
                    sumFiveList.add(japanese);
                } catch (IndexOutOfBoundsException e) {
                    // 何もせずに飛ばす
                }
                try {
                    String science = strList.get(i).get(16).equals("") ? null : strList.get(i).get(16);
                    regularExam.setScience(science);
                    sumFiveList.add(science);
                } catch (IndexOutOfBoundsException e) {
                    // 何もせずに飛ばす
                }
                try {
                    String socialStudies = strList.get(i).get(17).equals("") ? null : strList.get(i).get(17);
                    regularExam.setSocialStudies(socialStudies);
                    sumFiveList.add(socialStudies);
                } catch (IndexOutOfBoundsException e) {
                    // 何もせずに飛ばす
                }
                try {
                    String music = strList.get(i).get(19).equals("") ? null : strList.get(i).get(19);
                    regularExam.setMusic(music);
                } catch (IndexOutOfBoundsException e) {
                    // 何もせずに飛ばす
                }
                try {
                    String art = strList.get(i).get(20).equals("") ? null : strList.get(i).get(20);
                    regularExam.setArt(art);
                } catch (IndexOutOfBoundsException e) {
                    // 何もせずに飛ばす
                }
                try {
                    String pe = strList.get(i).get(21).equals("") ? null : strList.get(i).get(21);
                    regularExam.setPe(pe);
                } catch (IndexOutOfBoundsException e) {
                    // 何もせずに飛ばす
                }
                try {
                    String tech = strList.get(i).get(22).equals("") ? null : strList.get(i).get(22);
                    regularExam.setTech(tech);
                } catch (IndexOutOfBoundsException e) {
                    // 何もせずに飛ばす
                }
                try {
                    String home = strList.get(i).get(23).equals("") ? null : strList.get(i).get(23);
                    regularExam.setHome(home);
                } catch (IndexOutOfBoundsException e) {
                    // 何もせずに飛ばす
                }
                try {
                    String sumFive = String.valueOf(RegistryProcessing.sumSubjects(sumFiveList));
                    regularExam.setSumFive(sumFive);
                } catch (RuntimeException e) {
                    // 何もせずに飛ばす
                }

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

        for (Student student : idNameList) {
            String name1 = student.getStudentName().replaceAll(" ", "").replaceAll("　", "");
            String name2 = name1.replace("﨑", "崎").replace("髙", "高");
            String name3 = name2.replace("国", "國");
            if (name1.equals(replacedName) || name2.equals(replacedName) || name3.equals(replacedName)) {
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

    /**
     * <p>[概 要] 全角数字⇒半角数字への変換</p>
     * <p>[詳 細] </p>
     * <p>[備 考] </p>
     * @param  str 変換対象文字列
     * @return 変換後文字列
     */
    public static String changeNumFullToHalf(String str) {
        String result = null;
        if(str != null) {
            StringBuilder sb = new StringBuilder(str);
            for (int i = 0; i < sb.length(); i++) {
                int c = (int) sb.charAt(i);
                if (c >= 0xFF10 && c <= 0xFF19) {
                    sb.setCharAt(i, (char) (c - 0xFEE0));
                }
            }
            result = sb.toString();
        }
        return result;
    }
}