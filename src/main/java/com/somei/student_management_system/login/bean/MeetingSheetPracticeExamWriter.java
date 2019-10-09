package com.somei.student_management_system.login.bean;

import com.somei.student_management_system.login.domain.model.PracticeExam;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MeetingSheetPracticeExamWriter {

    @Autowired
    PoiMethods poiMethods;

    /**
     * 中１の模試結果入力メソッド
     *
     * @param wb       ワークブック
     * @param sheet    シート
     * @param thisList 今学年の模試結果
     * @param term     学期制の数値
     */
    public void write1stPracticeExam(XSSFWorkbook wb, XSSFSheet sheet, List<PracticeExam> thisList, int term) {

        // 入力の開始行、最終行の初期化
        int startRow;

        // 学期制によって条件分岐
        if (term == 3) {
            // ３学期制の場合
            startRow = 13;
        } else {
            // ２学期制の場合
            startRow = 11;
        }

        // 今学年の成績の入力
        for (int i = 0; i < thisList.size(); i++) {

            // 成績時期によって処理を分岐
            switch (thisList.get(i).getMonthName()) {
                case "８月":
                    writeSubjectPracticeExam(sheet, thisList, startRow, i);
                    break;
                case "10月":
                    writeSubjectPracticeExam(sheet, thisList, startRow + 1, i);
                    // １つめの成績なら、それまで成績を塗りつぶす
                    if (i == 0) {
                        poiMethods.fillBackground(wb, sheet, "F" + (startRow + 1) + ":P" + (startRow + 1));
                    }
                    break;
                case "12月":
                    writeSubjectPracticeExam(sheet, thisList, startRow + 2, i);
                    // １つめの成績なら、それまで成績を塗りつぶす
                    if (i == 0) {
                        poiMethods.fillBackground(wb, sheet, "F" + (startRow + 1) + ":P" + (startRow + 2));
                    }
                    break;
                case "３月":
                    writeSubjectPracticeExam(sheet, thisList, startRow + 3, i);
                    // １つめの成績なら、それまで成績を塗りつぶす
                    if (i == 0) {
                        poiMethods.fillBackground(wb, sheet, "F" + (startRow + 1) + ":P" + (startRow + 3));
                    }
                    break;
            }
        }
    }

    /**
     * 中２・中３の模試結果入力メソッド
     *
     * @param wb         ワークブック
     * @param sheet      シート
     * @param thisList   今学年の模試結果
     * @param beforeList 全学年の模試結果
     * @param term       学期制の数値
     */
    public void write2nd3rdPracticeExam(XSSFWorkbook wb, XSSFSheet sheet, List<PracticeExam> beforeList,
                                        List<PracticeExam> thisList, int term) {

        // 入力の開始行、最終行の初期化
        int startRow;

        // 学期制によって条件分岐
        if (term == 3) {
            // ３学期制の場合
            startRow = 13;
        } else {
            // ２学期制の場合
            startRow = 11;
        }

        // 全学年の最終成績の入力
        if (beforeList.size() != 0) {

            // 全学年の最終結果がある場合、入力
            writeSubjectPracticeExam(sheet, beforeList, startRow, beforeList.size() - 1);

        } else {

            // 全学年の最終結果がない場合、塗りつぶす
            poiMethods.fillBackground(wb, sheet, "F" + (startRow + 1) + ":P" + (startRow + 1));
        }

        // 今学年の成績の入力
        for (int i = 0; i < thisList.size(); i++) {

            // 成績時期によって処理を分岐
            switch (thisList.get(i).getMonthName()) {
                case "７月":
                    writeSubjectPracticeExam(sheet, thisList, startRow + 1, i);
                    break;
                case "８月":
                    writeSubjectPracticeExam(sheet, thisList, startRow + 2, i);
                    // １つめの成績なら、それまで成績を塗りつぶす
                    if (i == 0) {
                        poiMethods.fillBackground(wb, sheet, "F" + (startRow + 2) + ":P" + (startRow + 2));
                    }
                    break;
                case "10月":
                    writeSubjectPracticeExam(sheet, thisList, startRow + 3, i);
                    // １つめの成績なら、それまで成績を塗りつぶす
                    if (i == 0) {
                        poiMethods.fillBackground(wb, sheet, "F" + (startRow + 2) + ":P" + (startRow + 3));
                    }
                    break;
                case "12月":
                    writeSubjectPracticeExam(sheet, thisList, startRow + 4, i);
                    // １つめの成績なら、それまで成績を塗りつぶす
                    if (i == 0) {
                        poiMethods.fillBackground(wb, sheet, "F" + (startRow + 2) + ":P" + (startRow + 4));
                    }
                    break;
                case "１月":
                    writeSubjectPracticeExam(sheet, thisList, startRow + 3, i);
                    // １つめの成績なら、それまで成績を塗りつぶす
                    if (i == 0) {
                        poiMethods.fillBackground(wb, sheet, "F" + (startRow + 2) + ":P" + (startRow + 5));
                    }
                    break;
                case "３月":
                    writeSubjectPracticeExam(sheet, thisList, startRow + 3, i);
                    // １つめの成績なら、それまで成績を塗りつぶす
                    if (i == 0) {
                        poiMethods.fillBackground(wb, sheet, "F" + (startRow + 2) + ":P" + (startRow + 5));
                    }
                    break;
            }
        }
    }

    /**
     * 模試結果の成績入力のメソッド
     *
     * @param sheet   シート
     * @param list    入力する成績のリスト
     * @param row     成績を入力する行
     * @param listNum 成績リスト取得用の番号
     */
    private void writeSubjectPracticeExam(XSSFSheet sheet, List<PracticeExam> list, int row, int listNum) {

        poiMethods.getCell(sheet, row, 5).setCellValue(Integer.parseInt(list.get(listNum).getEnglishScore()));
        poiMethods.getCell(sheet, row, 6).setCellValue(Integer.parseInt(list.get(listNum).getMathScore()));
        poiMethods.getCell(sheet, row, 7).setCellValue(Integer.parseInt(list.get(listNum).getJapaneseScore()));
        poiMethods.getCell(sheet, row, 8).setCellValue(Integer.parseInt(list.get(listNum).getScienceScore()));
        poiMethods.getCell(sheet, row, 9).setCellValue(Integer.parseInt(list.get(listNum).getSocialScore()));
        poiMethods.getCell(sheet, row, 10).setCellValue(Integer.parseInt(list.get(listNum).getSumAll()));
        poiMethods.getCell(sheet, row, 11).setCellValue(Integer.parseInt(list.get(listNum).getDevThree()));
        poiMethods.getCell(sheet, row, 12).setCellValue(Integer.parseInt(list.get(listNum).getDevFive()));

    }

}