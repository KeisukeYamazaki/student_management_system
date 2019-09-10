package com.somei.student_management_system.login.bean;

import com.somei.student_management_system.login.domain.model.SchoolRecordWithName;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Component
public class excelProcessing {

    /**
     * 受け取ったエクセルファイルを成績リストに変換する
     *
     * @param is エクセルファイルのストリーム
     * @return List<SchoolRecord> 入力された成績のリスト
     */
    public List<SchoolRecordWithName> readExcelFile(InputStream is) throws IOException, InvalidFormatException {

        // 成績格納リストを作成
        List<SchoolRecordWithName> list = new ArrayList<>();

        // ワークブックを取得
        Workbook workbook = WorkbookFactory.create(is);

        // 先頭のシートを指定
        Sheet sheet = workbook.getSheetAt(0);

        // 登録人数の取得
        int count = getIntFormulaValue(sheet.getRow(0).getCell(15));

        for (int j = 5; j < count + 5; j++) {

            SchoolRecordWithName srwn = new SchoolRecordWithName();

            Label:
            // セルの値を取得していく
            for (int i = 0; i <= 15; i++) {

                Cell cell = sheet.getRow(j).getCell(i);

                // i の値に従って入力する項目を分岐
                switch (i) {
                    case 0:
                        srwn.setStudentName(cell.getStringCellValue());
                        break;
                    case 1:
                        srwn.setStudentId(String.valueOf((int)cell.getNumericCellValue()));
                        break;
                    case 2:
                        srwn.setRecordYear(String.valueOf((int)cell.getNumericCellValue()));
                        break;
                    case 3:
                        srwn.setGrade(cell.getStringCellValue());
                        break;
                    case 4:
                        srwn.setTermName(cell.getStringCellValue());
                        break;
                    case 5:
                        srwn.setEnglish(String.valueOf((int)cell.getNumericCellValue()));
                        break;
                    case 6:
                        srwn.setMath(String.valueOf((int)cell.getNumericCellValue()));
                        break;
                    case 7:
                        srwn.setJapanese(String.valueOf((int)cell.getNumericCellValue()));
                        break;
                    case 8:
                        srwn.setScience(String.valueOf((int)cell.getNumericCellValue()));
                        break;
                    case 9:
                        srwn.setSocialStudies(String.valueOf((int)cell.getNumericCellValue()));
                        break;
                    case 10:
                        srwn.setMusic(String.valueOf((int)cell.getNumericCellValue()));
                        break;
                    case 11:
                        srwn.setArt(String.valueOf((int)cell.getNumericCellValue()));
                        break;
                    case 12:
                        srwn.setPe(String.valueOf((int)cell.getNumericCellValue()));
                        break;
                    case 13:
                        srwn.setTechHome(String.valueOf((int)cell.getNumericCellValue()));
                        break;
                    case 14:
                        srwn.setSumFive(String.valueOf(getIntFormulaValue(cell)));
                        break;
                    case 15:
                        srwn.setSumAll(String.valueOf(getIntFormulaValue(cell)));
                        break;
                }

            }

            list.add(srwn);

        }

        return list;

    }

    // セルの数式を計算し、intとして取得
    public int getIntFormulaValue(Cell cell) {
        assert cell.getCellType() == Cell.CELL_TYPE_FORMULA;

        Workbook book = cell.getSheet().getWorkbook();
        CreationHelper helper = book.getCreationHelper();
        FormulaEvaluator evaluator = helper.createFormulaEvaluator();
        CellValue value = evaluator.evaluate(cell);

        return (int)value.getNumberValue();

    }

}
