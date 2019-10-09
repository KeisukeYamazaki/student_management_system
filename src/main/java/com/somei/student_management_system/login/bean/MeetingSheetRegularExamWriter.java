package com.somei.student_management_system.login.bean;

import com.somei.student_management_system.login.domain.model.RegularExam;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MeetingSheetRegularExamWriter {

    @Autowired
    PoiMethods poiMethods;

    /**
     * ３学期制の定期試験結果入力メソッド
     *
     * @param wb        ワークブック
     * @param sheet     シート
     * @param list      入力する定期試験結果のリスト
     * @param grade     面談シートを作成する生徒の現在の学年
     * @param listGrade 入力する定期試験結果のリストの学年
     */
    public void writeRegularExam3terms(XSSFWorkbook wb, XSSFSheet sheet, List<RegularExam> list, int grade, int listGrade) {

        // 開始行の初期化
        int startRow = 0;

        // 入力する学年によって開始行を指定
        switch (listGrade) {
            case 1:
                startRow = 3;
                break;
            case 2:
                startRow = 8;
                break;
            case 3:
                startRow = 13;
                break;
        }

        // データがあるかどうかで分岐
        if (list.size() != 0) {
            // リストにデータがある場合、当該学年の結果かどうかで分岐
            if (grade == listGrade) {
                // 当該学年の場合、リスト内のデータの数によって処理を分岐
                switch (list.size()) {
                    case 1:
                        writeCurrentRegularExamSwitch1(wb, sheet, list, startRow, 3);
                        break;
                    case 2:
                        writeCurrentRegularExamSwitch2(wb, sheet, list, startRow, 3);
                        break;
                    case 3:
                        writeCurrentRegularExamSwitch3(wb, sheet, list, startRow, 3);
                        break;
                    case 4:
                        writeCurrentRegularExamSwitch4(wb, sheet, list, startRow);
                    case 5:
                        // 当該学年のすべての結果があるので、それを入力する処理を行う
                        for (int i = 0; i < list.size(); i++) {
                            writeSubjectRegularExam(sheet, list, startRow, i);
                        }
                }
            } else {
                // 当該学年ではない場合（過去学年の場合）
                writePastRegularExamSwitch(wb, sheet, list, startRow, 3);
            }

        } else {

            // リストにデータがない場合
            if (grade - listGrade > 0) {
                // 過去学年の結果の場合は、過去学年の結果を塗りつぶす（現学年の場合は何もしない）
                poiMethods.fillBackground(wb, sheet, "S" + (startRow + 1) + ":AC" + (startRow + 5));
            }
        }
    }

    /**
     * ２学期制の定期試験結果入力メソッド
     *
     * @param wb        ワークブック
     * @param sheet     シート
     * @param list      入力する定期試験結果のリスト
     * @param grade     面談シートを作成する生徒の現在の学年
     * @param listGrade 入力する定期試験結果のリストの学年
     */
    public void writeRegularExam2terms(XSSFWorkbook wb, XSSFSheet sheet, List<RegularExam> list, int grade, int listGrade) {

        // 開始行の初期化
        int startRow = 0;

        // 入力する学年によって開始行を指定
        switch (listGrade) {
            case 1:
                startRow = 3;
                break;
            case 2:
                startRow = 7;
                break;
            case 3:
                startRow = 11;
                break;
        }

        // データがあるかどうかで分岐
        if (list.size() != 0) {
            // リストにデータがある場合、当該学年の結果かどうかで分岐
            if (grade == listGrade) {
                // 当該学年の場合、リスト内のデータの数によって処理を分岐
                switch (list.size()) {
                    case 1:
                        writeCurrentRegularExamSwitch1(wb, sheet, list, startRow, 2);
                        break;
                    case 2:
                        writeCurrentRegularExamSwitch2(wb, sheet, list, startRow, 2);
                        break;
                    case 3:
                        writeCurrentRegularExamSwitch3(wb, sheet, list, startRow, 2);
                        break;
                    case 4:
                        // 当該学年のすべての結果があるので、それを入力する処理を行う
                        for (int i = 0; i < list.size(); i++) {
                            writeSubjectRegularExam(sheet, list, startRow, i);
                        }
                }
            } else {
                // 当該学年ではない場合（過去学年の場合）
                writePastRegularExamSwitch(wb, sheet, list, startRow, 2);
            }

        } else {

            // リストにデータがない場合
            if (grade - listGrade > 0) {
                // 過去学年の結果の場合は、過去学年の結果を塗りつぶす（現学年の場合は何もしない）
                poiMethods.fillBackground(wb, sheet, "S" + (startRow + 1) + ":AC" + (startRow + 5));
            }
        }
    }

    /**
     * 科目の成績入力のメソッド
     *
     * @param sheet   シート
     * @param list    入力する定期試験結果のリスト
     * @param row     定期試験結果を入力する行
     * @param listNum 定期試験結果リスト取得用の番号
     */
    private void writeSubjectRegularExam(XSSFSheet sheet, List<RegularExam> list, int row, int listNum) {

        try {
            poiMethods.getCell(sheet, row, 18).setCellValue(Integer.parseInt(list.get(listNum).getEnglish()));
        } catch (NumberFormatException e) {
            poiMethods.getCell(sheet, row, 18).setCellValue(list.get(listNum).getEnglish());
        }

        try {
            poiMethods.getCell(sheet, row, 19).setCellValue(Integer.parseInt(list.get(listNum).getMath()));
        } catch (NumberFormatException e) {
            poiMethods.getCell(sheet, row, 19).setCellValue(list.get(listNum).getMath());
        }

        try {
            poiMethods.getCell(sheet, row, 20).setCellValue(Integer.parseInt(list.get(listNum).getJapanese()));
        } catch (NumberFormatException e) {
            poiMethods.getCell(sheet, row, 20).setCellValue(list.get(listNum).getJapanese());
        }

        try {
            poiMethods.getCell(sheet, row, 21).setCellValue(Integer.parseInt(list.get(listNum).getScience()));
        } catch (NumberFormatException e) {
            poiMethods.getCell(sheet, row, 21).setCellValue(list.get(listNum).getScience());
        }

        try {
            poiMethods.getCell(sheet, row, 22).setCellValue(Integer.parseInt(list.get(listNum).getSocialStudies()));
        } catch (NumberFormatException e) {
            poiMethods.getCell(sheet, row, 22).setCellValue(list.get(listNum).getSocialStudies());
        }

        try {
            poiMethods.getCell(sheet, row, 23).setCellValue(Integer.parseInt(list.get(listNum).getSumFive()));
        } catch (NumberFormatException e) {
            poiMethods.getCell(sheet, row, 23).setCellValue(list.get(listNum).getSumFive());
        }

        try {
            poiMethods.getCell(sheet, row, 24).setCellValue(Integer.parseInt(list.get(listNum).getMusic()));
        } catch (NumberFormatException e) {
            poiMethods.getCell(sheet, row, 24).setCellValue(list.get(listNum).getMusic());
        }

        try {
            poiMethods.getCell(sheet, row, 25).setCellValue(Integer.parseInt(list.get(listNum).getArt()));
        } catch (NumberFormatException e) {
            poiMethods.getCell(sheet, row, 25).setCellValue(list.get(listNum).getArt());
        }

        try {
            poiMethods.getCell(sheet, row, 26).setCellValue(Integer.parseInt(list.get(listNum).getPe()));
        } catch (NumberFormatException e) {
            poiMethods.getCell(sheet, row, 26).setCellValue(list.get(listNum).getPe());
        }

        try {
            poiMethods.getCell(sheet, row, 27).setCellValue(Integer.parseInt(list.get(listNum).getTech()));
        } catch (NumberFormatException e) {
            poiMethods.getCell(sheet, row, 27).setCellValue(list.get(listNum).getTech());
        }

        try {
            poiMethods.getCell(sheet, row, 28).setCellValue(Integer.parseInt(list.get(listNum).getHome()));
        } catch (NumberFormatException e) {
            poiMethods.getCell(sheet, row, 28).setCellValue(list.get(listNum).getHome());
        }
    }

    /**
     * <p>
     * 過去学年の定期試験結果の条件分岐メソッド
     * <p>
     * リストに入っている定期試験結果の数に応じて、入力、背景色の塗りつぶしを行う
     *
     * @param sheet    シート
     * @param list     入力する定期試験結果のリスト
     * @param startRow 入力の開始行
     */
    private void writePastRegularExamSwitch(XSSFWorkbook wb, XSSFSheet sheet, List<RegularExam> list, int startRow, int term) {

        // 学期制によって処理を分岐
        if (term == 3) {

            // ３学期制の場合
            switch (list.size()) {

                case 5:
                    // 結果が５つある場合：当該学年の結果がすべてがある場合
                    for (int i = startRow, j = 0; i <= startRow + 4; i++, j++) {
                        // 各科目の結果の入力
                        writeSubjectRegularExam(sheet, list, i, j);
                    }
                    break;
                case 4:
                    // 結果が４つある場合：当該学年の１学期期末から成績がある場合
                    for (int i = startRow + 1, j = 0; i <= startRow + 4; i++, j++) {
                        // 各科目の結果の入力
                        writeSubjectRegularExam(sheet, list, i, j);
                    }
                    // 塗りつぶしの処理
                    poiMethods.fillBackground(wb, sheet, "S" + (startRow + 1) + ":AC" + (startRow + 1));
                    break;
                case 3:
                    // 結果が３つある場合：当該学年の２学期中間から成績がある場合
                    for (int i = startRow + 2, j = 0; i <= startRow + 4; i++, j++) {
                        // 各科目の結果の入力
                        writeSubjectRegularExam(sheet, list, i, j);
                    }
                    // 塗りつぶしの処理
                    poiMethods.fillBackground(wb, sheet, "S" + (startRow + 1) + ":AC" + (startRow + 2));
                    break;
                case 2:
                    // 結果が２つある場合：当該学年の２学期期末から成績がある場合
                    for (int i = startRow + 3, j = 0; i <= startRow + 4; i++, j++) {
                        // 各科目の結果の入力
                        writeSubjectRegularExam(sheet, list, i, j);
                    }
                    // 塗りつぶしの処理
                    poiMethods.fillBackground(wb, sheet, "S" + (startRow + 1) + ":AC" + (startRow + 3));
                    break;
                case 1:
                    // 成績が１つある場合：当該学年の学年末の結果がある場合
                    // 各科目の結果の入力
                    writeSubjectRegularExam(sheet, list, startRow + 4, 0);
                    // 塗りつぶしの処理
                    poiMethods.fillBackground(wb, sheet, "S" + (startRow + 1) + ":AC" + (startRow + 4));
                    break;
            }

        } else {

            // ２学期制の場合
            switch (list.size()) {
                case 4:
                    // 結果が４つある場合：当該学年の前期中間から結果がある場合
                    for (int i = startRow, j = 0; i <= startRow + 3; i++, j++) {
                        // 各科目の結果の入力
                        writeSubjectRegularExam(sheet, list, i, j);
                    }
                    break;
                case 3:
                    // 結果が３つある場合：当該学年の前期期末から結果がある場合
                    for (int i = startRow + 1, j = 0; i <= startRow + 3; i++, j++) {
                        // 各科目の結果の入力
                        writeSubjectRegularExam(sheet, list, i, j);
                    }
                    // 塗りつぶしの処理
                    poiMethods.fillBackground(wb, sheet, "S" + (startRow + 1) + ":AC" + (startRow + 1));
                    break;
                case 2:
                    // 結果が２つある場合：当該学年の後期中間から結果がある場合
                    for (int i = startRow + 2, j = 0; i <= startRow + 3; i++, j++) {
                        // 各科目の結果の入力
                        writeSubjectRegularExam(sheet, list, i, j);
                    }
                    // 塗りつぶしの処理
                    poiMethods.fillBackground(wb, sheet, "S" + (startRow + 1) + ":AC" + (startRow + 2));
                    break;
                case 1:
                    // 結果が１つある場合：当該学年の学年末の結果がある場合
                    writeSubjectRegularExam(sheet, list, startRow + 3, 0);
                    // 塗りつぶしの処理
                    poiMethods.fillBackground(wb, sheet, "S" + (startRow + 1) + ":AC" + (startRow + 3));
                    break;
            }
        }
    }

    /**
     * <p>
     * 現学年の定期試験結果の条件分岐メソッド（リスト内のデータが１つの場合）
     * <p>
     * リストに入っている定期試験結果の数に応じて、入力、背景色の塗りつぶしを行う
     *
     * @param wb       ワークブック
     * @param sheet    シート
     * @param list     入力する定期試験結果のリスト
     * @param startRow 入力の開始行
     * @param term     学期制を示す数
     */
    private void writeCurrentRegularExamSwitch1(XSSFWorkbook wb, XSSFSheet sheet, List<RegularExam> list, int startRow, int term) {

        // ３学期制か２学期制かによって分岐
        if (term == 3) {

            // ３学期制の場合
            switch (list.get(0).getExamName()) {
                case "１学期中間":
                    writeSubjectRegularExam(sheet, list, startRow, 0);
                    break;
                case "１学期期末":
                    writeSubjectRegularExam(sheet, list, startRow + 1, 0);
                    // 塗りつぶしを行う
                    poiMethods.fillBackground(wb, sheet, "S" + (startRow + 1) + ":AC" + (startRow + 1));
                    break;
                case "２学期中間":
                    writeSubjectRegularExam(sheet, list, startRow + 2, 0);
                    // 塗りつぶしを行う
                    poiMethods.fillBackground(wb, sheet, "S" + (startRow + 1) + ":AC" + (startRow + 2));
                    break;
                case "２学期期末":
                    writeSubjectRegularExam(sheet, list, startRow + 3, 0);
                    // 塗りつぶしを行う
                    poiMethods.fillBackground(wb, sheet, "S" + (startRow + 1) + ":AC" + (startRow + 3));
                    break;
                case "学年末":
                    writeSubjectRegularExam(sheet, list, startRow + 4, 0);
                    // 塗りつぶしを行う
                    poiMethods.fillBackground(wb, sheet, "S" + (startRow + 1) + ":AC" + (startRow + 4));
                    break;
            }

        } else {

            // ２学期制の場合
            switch (list.get(0).getExamName()) {
                case "前期中間":
                    writeSubjectRegularExam(sheet, list, startRow, 0);
                    break;
                case "前期期末":
                    writeSubjectRegularExam(sheet, list, startRow + 1, 0);
                    // 塗りつぶしを行う
                    poiMethods.fillBackground(wb, sheet, "S" + (startRow + 1) + ":AC" + (startRow + 1));
                    break;
                case "後期中間":
                    writeSubjectRegularExam(sheet, list, startRow + 2, 0);
                    // 塗りつぶしを行う
                    poiMethods.fillBackground(wb, sheet, "S" + (startRow + 1) + ":AC" + (startRow + 2));
                    break;
                case "学年末":
                    writeSubjectRegularExam(sheet, list, startRow + 3, 0);
                    // 塗りつぶしを行う
                    poiMethods.fillBackground(wb, sheet, "S" + (startRow + 1) + ":AC" + (startRow + 3));
                    break;
            }
        }

    }

    /**
     * <p>
     * 現学年の定期試験結果の条件分岐メソッド（リスト内のデータが２つの場合）
     * <p>
     * リストに入っている定期試験結果の数に応じて、入力、背景色の塗りつぶしを行う
     *
     * @param wb       ワークブック
     * @param sheet    シート
     * @param list     入力する定期試験結果のリスト
     * @param startRow 入力の開始行
     * @param term     学期制を示す数
     */
    private void writeCurrentRegularExamSwitch2(XSSFWorkbook wb, XSSFSheet sheet, List<RegularExam> list, int startRow, int term) {

        // ３学期制か２学期制かによって分岐
        if (term == 3) {

            // ３学期制の場合
            // ２つのデータがあるということは、２つのデータが連続して入っていることなので、
            // その１つ目のデータによって条件を分岐させる
            switch (list.get(0).getExamName()) {
                case "１学期中間":
                    // １つ目のデータが１学期中間の場合（もう一つは１学期期末）
                    writeSubjectRegularExam(sheet, list, startRow, 0);
                    writeSubjectRegularExam(sheet, list, startRow + 1, 1);
                    break;
                case "１学期期末":
                    // １つ目のデータが１学期期末の場合（もう１つは２学期中間）
                    writeSubjectRegularExam(sheet, list, startRow + 1, 0);
                    writeSubjectRegularExam(sheet, list, startRow + 2, 1);
                    // 塗りつぶしを行う
                    poiMethods.fillBackground(wb, sheet, "S" + (startRow + 1) + ":AC" + (startRow + 1));
                    break;
                case "２学期中間":
                    // １つ目のデータが２学期中間の場合（もう１つは２学期期末）
                    writeSubjectRegularExam(sheet, list, startRow + 2, 0);
                    writeSubjectRegularExam(sheet, list, startRow + 3, 1);
                    // 塗りつぶしを行う
                    poiMethods.fillBackground(wb, sheet, "S" + (startRow + 1) + ":AC" + (startRow + 2));
                    break;
                case "２学期期末":
                    // １つ目のデータが２学期期末の場合（もうひとつは学年末）
                    writeSubjectRegularExam(sheet, list, startRow + 3, 0);
                    writeSubjectRegularExam(sheet, list, startRow + 4, 2);
                    // 塗りつぶしを行う
                    poiMethods.fillBackground(wb, sheet, "S" + (startRow + 1) + ":AC" + (startRow + 3));
                    break;
            }

        } else {

            // ２学期制の場合
            switch (list.get(0).getExamName()) {
                case "前期中間":
                    // １つ目のデータが前期中間の場合（もう一つは前期期末）
                    writeSubjectRegularExam(sheet, list, startRow, 0);
                    writeSubjectRegularExam(sheet, list, startRow + 1, 1);
                    break;
                case "前期期末":
                    // １つ目のデータが前期期末の場合（もう一つは後期中間）
                    writeSubjectRegularExam(sheet, list, startRow + 1, 0);
                    writeSubjectRegularExam(sheet, list, startRow + 2, 0);
                    // 塗りつぶしを行う
                    poiMethods.fillBackground(wb, sheet, "S" + (startRow + 1) + ":AC" + (startRow + 1));
                    break;
                case "後期中間":
                    // １つ目のデータが後期中間の場合（もう一つは学年末）
                    writeSubjectRegularExam(sheet, list, startRow + 2, 0);
                    writeSubjectRegularExam(sheet, list, startRow + 3, 0);
                    // 塗りつぶしを行う
                    poiMethods.fillBackground(wb, sheet, "S" + (startRow + 1) + ":AC" + (startRow + 2));
                    break;
            }
        }
    }

    /**
     * <p>
     * 現学年の定期試験結果の条件分岐メソッド（リスト内のデータが３つの場合）
     * <p>
     * リストに入っている定期試験結果の数に応じて、入力、背景色の塗りつぶしを行う
     *
     * @param wb       ワークブック
     * @param sheet    シート
     * @param list     入力する定期試験結果のリスト
     * @param startRow 入力の開始行
     * @param term     学期制を示す数
     */
    private void writeCurrentRegularExamSwitch3(XSSFWorkbook wb, XSSFSheet sheet, List<RegularExam> list, int startRow, int term) {

        // ３学期制か２学期制かによって分岐
        if (term == 3) {

            // ３学期制の場合
            // ３つのデータがあるということは、３つのデータが連続して入っていることなので、
            // その１つ目のデータによって条件を分岐させる
            // j は開始行の増加分を指定する変数
            switch (list.get(0).getExamName()) {
                case "１学期中間":
                    // １つ目のデータが１学期中間の場合（残りの２つは、順に１学期期末、２学期中間）
                    for (int i = 0, j = 0; i < list.size(); i++, j++) {
                        writeSubjectRegularExam(sheet, list, startRow + j, i);
                    }
                    break;
                case "１学期期末":
                    // １つ目のデータが１学期期末の場合（残りの２つは、順に２学期中間、２学期期末）
                    for (int i = 0, j = 1; i < list.size(); i++, j++) {
                        writeSubjectRegularExam(sheet, list, startRow + j, i);
                    }
                    // 塗りつぶしを行う
                    poiMethods.fillBackground(wb, sheet, "S" + (startRow + 1) + ":AC" + (startRow + 1));
                    break;
                case "２学期中間":
                    // １つ目のデータが２学期中間の場合（残りの２つは、順に２学期期末、学年末）
                    for (int i = 0, j = 2; i < list.size(); i++, j++) {
                        writeSubjectRegularExam(sheet, list, startRow + j, i);
                    }
                    // 塗りつぶしを行う
                    poiMethods.fillBackground(wb, sheet, "S" + (startRow + 1) + ":AC" + (startRow + 2));
                    break;
            }

        } else {

            // ２学期制の場合
            switch (list.get(0).getExamName()) {
                case "前期中間":
                    // １つ目のデータが前期中間の場合（残りの２つは、順に前期期末、後期中間）
                    for (int i = 0, j = 0; i < list.size(); i++, j++) {
                        writeSubjectRegularExam(sheet, list, startRow + j, i);
                    }
                    break;
                case "前期期末":
                    // １つ目のデータが前期期末の場合（残りの２つは、順に後期中間、学年末）
                    for (int i = 0, j = 1; i < list.size(); i++, j++) {
                        writeSubjectRegularExam(sheet, list, startRow + j, i);
                    }
                    // 塗りつぶしを行う
                    poiMethods.fillBackground(wb, sheet, "S" + (startRow + 1) + ":AC" + (startRow + 1));
                    break;
            }
        }
    }

    /**
     * <p>
     * 現学年の定期試験結果の条件分岐メソッド（リスト内のデータが４つの場合）
     * <p>
     * リストに入っている定期試験結果の数に応じて、入力、背景色の塗りつぶしを行う
     *
     * @param wb       ワークブック
     * @param sheet    シート
     * @param list     入力する定期試験結果のリスト
     * @param startRow 入力の開始行
     */
    private void writeCurrentRegularExamSwitch4(XSSFWorkbook wb, XSSFSheet sheet, List<RegularExam> list, int startRow) {

        // ３学期制の場合のみ
        // ４つのデータがあるということは、４つのデータが連続して入っていることなので、
        // 考えられるケースは「１学期中間」からはじまる４つか、「１学期期末」からはじまる４つのいずれか。
        // j は開始行の増加分を指定する変数
        switch (list.get(0).getExamName()) {
            case "１学期中間":
                // １つ目のデータが１学期中間の場合（学年末のデータがない）
                for (int i = 0, j = 0; i < list.size(); i++, j++) {
                    writeSubjectRegularExam(sheet, list, startRow + j, i);
                }
                break;
            case "１学期期末":
                // １つ目のデータが１学期期末の場合（１学期中間のデータがない）
                for (int i = 0, j = 1; i < list.size(); i++, j++) {
                    writeSubjectRegularExam(sheet, list, startRow + j, i);
                }
                // 塗りつぶしを行う
                poiMethods.fillBackground(wb, sheet, "S" + (startRow + 1) + ":AC" + (startRow + 1));
                break;
        }
    }
}
