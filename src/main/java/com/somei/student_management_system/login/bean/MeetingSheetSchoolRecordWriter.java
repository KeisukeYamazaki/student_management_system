package com.somei.student_management_system.login.bean;

import com.somei.student_management_system.login.domain.model.SchoolRecord;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MeetingSheetSchoolRecordWriter {

    @Autowired
    PoiMethods poiMethods;

    /**
     * 中１の成績入力メソッド（３学期制）
     *
     * @param sheet シート
     * @param list  入力する成績のリスト
     */
    public void write1stSchoolRecord3terms(XSSFWorkbook wb, XSSFSheet sheet, List<SchoolRecord> list, int grade) {

        // 中１の成績の入力
        // 中１の場合と、中２，３の場合によって処理を分岐
        if (grade == 1) {

            // 中１の場合の処理
            switch (list.size()) {
                case 3:
                    // リストの中の成績が３つある場合
                    for (int i = 3, j = 0; i <= 5; i++, j++) {
                        // 各科目の成績の入力
                        writeSubjectSchoolRecord(sheet, list, i, j);
                    }
                    break;
                case 2:
                    // リストの中の成績が２つある場合
                    if (list.get(0).getTermName().equals("１学期")) {
                        // １つ目の成績が１学期の場合、もう１つは２学期の成績
                        for (int i = 3, j = 0; i <= 4; i++, j++) {
                            // 各科目の成績の入力
                            writeSubjectSchoolRecord(sheet, list, i, j);
                        }
                        break;
                    } else {
                        // １つ目の成績が２学期の場合、もうひとつの成績は３学期（中１途中から入塾）
                        for (int i = 4, j = 0; i <= 5; i++, j++) {
                            // 各科目の成績の入力
                            writeSubjectSchoolRecord(sheet, list, i, j);
                        }
                        // １学期の成績は塗りつぶす
                        poiMethods.fillBackground(wb, sheet, "F4:P4");
                        break;
                    }
                case 1:
                    // リストの中の成績が１つの場合は、成績の学期名から条件分岐
                    if (list.get(0).getTermName().equals("１学期")) {
                        // 各科目の成績の入力
                        writeSubjectSchoolRecord(sheet, list, 3, 0);
                        break;
                    } else if (list.get(0).getTermName().equals("２学期")) {
                        // 各科目の成績の入力
                        writeSubjectSchoolRecord(sheet, list, 4, 0);
                        // １学期の成績は塗りつぶす
                        poiMethods.fillBackground(wb, sheet, "F4:P4");
                        break;
                    } else {
                        // 各科目の成績の入力
                        writeSubjectSchoolRecord(sheet, list, 5, 0);
                        // １学期と２学期の成績は塗りつぶす
                        poiMethods.fillBackground(wb, sheet, "F4:P5");
                        break;
                    }
            }

        } else {

            // 中２・中３の場合の処理
            if (list.size() > 0) {

                // リストの中の成績の数によって条件を分岐
                writePastSchoolRecordSwitch(wb, sheet, list, 3, 3);

            } else {
                // 中１の成績が１つも無い場合は背景を灰色に塗りつぶす
                poiMethods.fillBackground(wb, sheet, "F4:P6");
            }
        }
    }

    /**
     * 中１の成績入力メソッド（２学期制）
     *
     * @param sheet シート
     * @param list  入力する成績のリスト
     */
    public void write1stSchoolRecord2terms(XSSFWorkbook wb, XSSFSheet sheet, List<SchoolRecord> list, int grade) {

        // 中１の成績の入力
        // 中１の場合と、中２，３の場合によって処理を分岐
        if (grade == 1) {

            // 中１の場合の処理
            switch (list.size()) {
                case 2:
                    // リストの中の成績が２つある場合
                    for (int i = 3, j = 0; i <= 4; i++, j++) {
                        // 各科目の成績の入力
                        writeSubjectSchoolRecord(sheet, list, i, j);
                    }
                    break;
                case 1:
                    // リストの中の成績が１つの場合は、成績の学期名から条件分岐
                    if (list.get(0).getTermName().equals("前期")) {
                        // 各科目の成績の入力
                        writeSubjectSchoolRecord(sheet, list, 3, 0);
                        break;
                    } else {
                        // 各科目の成績の入力
                        writeSubjectSchoolRecord(sheet, list, 4, 0);
                        // 前期の成績は塗りつぶす
                        poiMethods.fillBackground(wb, sheet, "F4:P4");
                        break;
                    }
            }

        } else {

            // 中２・中３の場合の処理
            if (list.size() > 0) {

                // リストの中の成績の数によって条件を分岐
                writePastSchoolRecordSwitch(wb, sheet, list, 3, 2);

            } else {
                // 中１の成績が１つも無い場合は背景を灰色に塗りつぶす
                poiMethods.fillBackground(wb, sheet, "F4:P5");
            }
        }
    }

    /**
     * 中２の成績入力メソッド（３学期制）
     *
     * @param sheet シート
     * @param list  入力する成績のリスト
     */
    public void write2ndSchoolRecord3terms(XSSFWorkbook wb, XSSFSheet sheet, List<SchoolRecord> list, int grade) {

        // 中２の成績の入力
        // 中２の場合と、中３の場合によって処理を分岐
        if (grade == 2) {

            // 中２の場合の処理
            switch (list.size()) {
                case 3:
                    // リストの中の成績が３つある場合
                    for (int i = 6, j = 0; i <= 8; i++, j++) {
                        // 各科目の成績の入力
                        writeSubjectSchoolRecord(sheet, list, i, j);
                    }
                    break;
                case 2:
                    // リストの中の成績が２つある場合
                    if (list.get(0).getTermName().equals("１学期")) {
                        // １つ目の成績が１学期の場合、もう１つは２学期の成績
                        for (int i = 6, j = 0; i <= 7; i++, j++) {
                            // 各科目の成績の入力
                            writeSubjectSchoolRecord(sheet, list, i, j);
                        }
                        break;
                    } else {
                        // １つ目の成績が２学期の場合、もうひとつの成績は３学期（中２途中から入塾）
                        for (int i = 7, j = 0; i <= 8; i++, j++) {
                            // 各科目の成績の入力
                            writeSubjectSchoolRecord(sheet, list, i, j);
                        }
                        // １学期の成績は塗りつぶす
                        poiMethods.fillBackground(wb, sheet, "F7:P7");
                        break;
                    }
                case 1:
                    // リストの中の成績が１つの場合は、成績の学期名から条件分岐
                    if (list.get(0).getTermName().equals("１学期")) {
                        // 各科目の成績の入力
                        writeSubjectSchoolRecord(sheet, list, 6, 0);
                        break;
                    } else if (list.get(0).getTermName().equals("２学期")) {
                        // 各科目の成績の入力
                        writeSubjectSchoolRecord(sheet, list, 7, 0);
                        // １学期の成績は塗りつぶす
                        poiMethods.fillBackground(wb, sheet, "F7:P7");
                        break;
                    } else {
                        // 各科目の成績の入力
                        writeSubjectSchoolRecord(sheet, list, 8, 0);
                        // １学期と２学期の成績は塗りつぶす
                        poiMethods.fillBackground(wb, sheet, "F7:P8");
                        break;
                    }
            }

        } else {

            // 中３の場合の処理
            if (list.size() > 0) {

                // リストの中の成績の数によって条件を分岐
                writePastSchoolRecordSwitch(wb, sheet, list, 6, 3);

            } else {
                // 中２の成績が１つも無い場合は背景を灰色に塗りつぶす
                poiMethods.fillBackground(wb, sheet, "F7:P9");
            }
        }
    }

    /**
     * 中２の成績入力メソッド（２学期制）
     *
     * @param sheet シート
     * @param list  入力する成績のリスト
     */
    public void write2ndSchoolRecord2terms(XSSFWorkbook wb, XSSFSheet sheet, List<SchoolRecord> list, int grade) {

        // 中２の成績の入力
        // 中２の場合と、中３の場合によって処理を分岐
        if (grade == 2) {

            // 中２の場合の処理
            switch (list.size()) {
                case 2:
                    // リストの中の成績が２つある場合
                    for (int i = 5, j = 0; i <= 6; i++, j++) {
                        // 各科目の成績の入力
                        writeSubjectSchoolRecord(sheet, list, i, j);
                    }
                    break;
                case 1:
                    // リストの中の成績が１つの場合は、成績の学期名から条件分岐
                    if (list.get(0).getTermName().equals("前期")) {
                        // 各科目の成績の入力
                        writeSubjectSchoolRecord(sheet, list, 5, 0);
                        break;
                    } else {
                        // 各科目の成績の入力
                        writeSubjectSchoolRecord(sheet, list, 6, 0);
                        // 前期の成績は塗りつぶす
                        poiMethods.fillBackground(wb, sheet, "F6:P6");
                        break;
                    }
            }

        } else {

            // 中３の場合の処理
            if (list.size() > 0) {

                // リストの中の成績の数によって条件を分岐
                writePastSchoolRecordSwitch(wb, sheet, list, 5, 2);

            } else {
                // 中２の成績が１つも無い場合は背景を灰色に塗りつぶす
                poiMethods.fillBackground(wb, sheet, "F6:P7");
            }
        }
    }

    /**
     * 中３の成績入力メソッド
     *
     * @param sheet シート
     * @param list  入力する成績のリスト
     * @param term  学期制の数字
     */
    public void write3rdSchoolRecord(XSSFWorkbook wb, XSSFSheet sheet, List<SchoolRecord> list, int term) {

        // 入力の開始行、最終行の初期化
        int startRow;

        // 学期制によって条件分岐
        if (term == 3) {
            // ３学期制の場合
            startRow = 9;
        } else {
            // ２学期制の場合
            startRow = 7;
        }

        // 中３の成績の入力（中３の成績が１つもない場合は、何もしない）
        if (list.size() > 0) {

            // リストの中の成績の数によって条件を分岐
            if (list.size() == 2) {

                // 成績が２つの場合：中３の成績がすべてある場合
                for (int i = startRow, j = 0; i <= startRow + 1; i++, j++) {
                    // 各科目の成績の入力
                    writeSubjectSchoolRecord(sheet, list, i, j);
                }

            } else {

                // 成績が１つの場合：１学期の成績として入力
                // 各科目の成績の入力
                writeSubjectSchoolRecord(sheet, list, startRow, 0);

            }

        }
    }

    /**
     * 科目の成績入力のメソッド
     *
     * @param sheet   シート
     * @param list    入力する成績のリスト
     * @param row     成績を入力する行
     * @param listNum 成績リスト取得用の番号
     */
    private void writeSubjectSchoolRecord(XSSFSheet sheet, List<SchoolRecord> list, int row, int listNum) {

        poiMethods.getCell(sheet, row, 5).setCellValue(list.get(listNum).getEnglish());
        poiMethods.getCell(sheet, row, 6).setCellValue(list.get(listNum).getMath());
        poiMethods.getCell(sheet, row, 7).setCellValue(list.get(listNum).getJapanese());
        poiMethods.getCell(sheet, row, 8).setCellValue(list.get(listNum).getScience());
        poiMethods.getCell(sheet, row, 9).setCellValue(list.get(listNum).getSocialStudies());
        poiMethods.getCell(sheet, row, 10).setCellValue(list.get(listNum).getSumFive());
        poiMethods.getCell(sheet, row, 11).setCellValue(list.get(listNum).getMusic());
        poiMethods.getCell(sheet, row, 12).setCellValue(list.get(listNum).getArt());
        poiMethods.getCell(sheet, row, 13).setCellValue(list.get(listNum).getPe());
        poiMethods.getCell(sheet, row, 14).setCellValue(list.get(listNum).getTechHome());
        poiMethods.getCell(sheet, row, 15).setCellValue(list.get(listNum).getSumAll());

    }

    /**
     * <p>
     * 過去学年の成績入力の条件分岐メソッド
     * <p>
     * リストに入っている成績の数に応じて、成績の入力、背景色の塗りつぶしを行う
     *
     * @param sheet    シート
     * @param list     入力する成績のリスト
     * @param startRow 入力の開始行
     */
    private void writePastSchoolRecordSwitch(XSSFWorkbook wb, XSSFSheet sheet, List<SchoolRecord> list, int startRow, int term) {

        // 学期制によって処理を分岐
        if (term == 3) {

            // ３学期制の場合
            switch (list.size()) {

                case 3:
                    // 成績が３つある場合：当該学年の１学期から成績がある場合
                    for (int i = startRow, j = 0; i <= startRow + 2; i++, j++) {
                        // 各科目の成績の入力
                        writeSubjectSchoolRecord(sheet, list, i, j);
                    }
                    break;
                case 2:
                    // 成績が２つある場合：当該学年の２学期から成績がある場合
                    for (int i = startRow + 1, j = 0; i <= startRow + 2; i++, j++) {
                        // 各科目の成績の入力
                        writeSubjectSchoolRecord(sheet, list, i, j);
                    }
                    // 塗りつぶしの処理
                    poiMethods.fillBackground(wb, sheet, "F" +  (startRow + 1) + ":P" +  (startRow + 1));
                    break;
                case 1:
                    // 成績が１つある場合：当該学年の３学期から成績がある場合
                    // 各科目の成績の入力
                    writeSubjectSchoolRecord(sheet, list, startRow + 2, 0);
                    // 塗りつぶしの処理
                    poiMethods.fillBackground(wb, sheet, "F" + (startRow + 1) + ":P" + (startRow + 2));
                    break;
            }

        } else {

            // ２学期制の場合
            switch (list.size()) {
                case 2:
                    // 成績が２つある場合：当該学年の前期から成績がある場合
                    for (int i = startRow, j = 0; i <= startRow + 1; i++, j++) {
                        // 各科目の成績の入力
                        writeSubjectSchoolRecord(sheet, list, i, j);
                    }
                    break;
                case 1:
                    // 成績が１つある場合：当該学年の後期の成績がある場合
                    for (int i = startRow + 1, j = 0; i <= startRow + 1; i++, j++) {
                        // 各科目の成績の入力
                        writeSubjectSchoolRecord(sheet, list, i, j);
                    }
                    // 塗りつぶしの処理
                    poiMethods.fillBackground(wb, sheet, "F" + (startRow + 1) + ":P" + (startRow + 1));
                    break;
            }
        }
    }

}
