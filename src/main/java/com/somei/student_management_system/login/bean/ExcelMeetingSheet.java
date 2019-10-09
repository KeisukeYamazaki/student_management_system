package com.somei.student_management_system.login.bean;

import com.somei.student_management_system.login.domain.model.Student;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

@Component
public class ExcelMeetingSheet {

    @Autowired
    MeetingSheetWriter meetingSheetWriter;

    /**
     * エクセルの面談シート作成メソッド
     *
     * @param student  面談シートを作成する生徒のデータ
     * @param fileName 作成するファイル名
     */
    public void MakeExcelMeetingSheet(Student student, String fileName) {

        // カレントパスの取得
        String basePath = System.getProperty("user.dir");

        // 相対パスの初期化
        String relativePath = null;

        // 学期名の初期化
        String termName = null;

        // 学年・学期制により取得するファイルを選択する
        switch (student.getGrade()) {

            case "中３":
                if (student.getLocalSchool().equals("東野中") ||
                        student.getLocalSchool().equals("原中") ||
                        student.getLocalSchool().equals("万騎ケ原中")) {

                    // ２学期制の場合（東野中、原中、万騎が原中）
                    relativePath = "/src/main/resources/excel/meetingSheet_3rd2term.xlsx";
                    termName = "２学期制";
                    break;

                } else {

                    // ３学期制の場合
                    relativePath = "/src/main/resources/excel/meetingSheet_3rd3term.xlsx";
                    termName = "３学期制";
                    break;

                }
            case "中２":
                if (student.getLocalSchool().equals("東野中") ||
                        student.getLocalSchool().equals("原中") ||
                        student.getLocalSchool().equals("万騎ケ原中")) {

                    // ２学期制の場合（東野中、原中、万騎が原中）
                    relativePath = "/src/main/resources/excel/meetingSheet_2nd2term.xlsx";
                    termName = "２学期制";
                    break;

                } else {

                    // ３学期制の場合
                    relativePath = "/src/main/resources/excel/meetingSheet_2nd3term.xlsx";
                    termName = "３学期制";
                    break;

                }
            case "中１":
                if (student.getLocalSchool().equals("東野中") ||
                        student.getLocalSchool().equals("原中") ||
                        student.getLocalSchool().equals("万騎ケ原中")) {

                    // ２学期制の場合（東野中、原中、万騎が原中）
                    relativePath = "/src/main/resources/excel/meetingSheet_1st2term.xlsx";
                    termName = "２学期制";
                    break;

                } else {

                    // ３学期制の場合
                    relativePath = "/src/main/resources/excel/meetingSheet_1st3term.xlsx";
                    termName = "３学期制";
                    break;

                }

        }

        FileInputStream in = null;
        XSSFWorkbook wb = null;

        try {

            in = new FileInputStream(basePath + relativePath);
            wb = new XSSFWorkbook(in);

        } catch (IOException ioException) {

            ioException.printStackTrace();

        } finally {

            try {

                in.close();

            } catch (IOException e) {

                e.printStackTrace();

            }
        }

        // エクセルファイル作成処理を記述
        wb = meetingSheetWriter.writeMeetingSheet(student, wb, termName);

        FileOutputStream out = null;
        try {

            out = new FileOutputStream(fileName);
            wb.write(out);

        } catch (IOException e) {

            e.printStackTrace();

        } finally {

            try {

                out.close();

            } catch (IOException e) {

                e.printStackTrace();
            }
        }
    }

}
