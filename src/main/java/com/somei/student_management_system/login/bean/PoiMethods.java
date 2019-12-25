package com.somei.student_management_system.login.bean;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;

@Component
public class PoiMethods {

    /**
     * <p>
     * 引数で指定されたシートの、行番号、列番号で指定したセルを取得して返却する
     * <p>
     * 行番号、列番号は0から開始する
     * <p>
     * Excelテンプレートで該当のセルを操作していない場合、NullPointerExceptionになる
     *
     * @param sheet    シート
     * @param rowIndex 行番号
     * @param colIndex 列番号
     * @return セル
     */
    public XSSFCell getCell(XSSFSheet sheet, int rowIndex, int colIndex) {
        XSSFRow row = sheet.getRow(rowIndex);
        return row.getCell(colIndex);
    }

    /**
     * <p>
     * 引数で指定されたシートの、行番号、列番号で指定したセルを取得して返却する
     * <p>
     * 行番号、列番号は0から開始する
     * <p>
     * Excelテンプレートで該当のセルを操作していない場合、NullPointerExceptionになる
     *
     * @param sheet    シート
     * @param rowIndex 行番号
     * @param colIndex 列番号
     * @return セル
     */
    public Cell getCell(Sheet sheet, int rowIndex, int colIndex) {
        Row row = sheet.getRow(rowIndex);
        return row.getCell(colIndex);
    }

    /**
     * 背景色塗りつぶしメソッド
     * <p>
     * 指定範囲のセルを灰色に塗りつぶす
     * </p>
     *
     * @param sheet            シート
     * @param cellRangeAddress 塗りつぶす対象のセル範囲
     */
    public void fillBackground(XSSFWorkbook wb, XSSFSheet sheet, String cellRangeAddress) {

        XSSFCellStyle cellStyle;

        // 塗りつぶしの処理
        CellRangeAddress range = CellRangeAddress.valueOf(cellRangeAddress);
        // 塗りつぶす
        for (int r = range.getFirstRow(); r <= range.getLastRow(); r++) {
            for (int c = range.getFirstColumn(); c <= range.getLastColumn(); c++) {
                cellStyle = getCell(sheet, r, c).getCellStyle();
                XSSFCellStyle newCellStyle = wb.createCellStyle();
                newCellStyle.cloneStyleFrom(cellStyle);
                newCellStyle.setFillForegroundColor(new XSSFColor(new java.awt.Color(216, 216, 216)));
                newCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND); // 塗りつぶし
                getCell(sheet, r, c).setCellStyle(newCellStyle);
            }
        }
    }

    /**
     * 指定行の削除メソッド
     * <p>
     *
     * </p>
     *
     * @param sheet    シート
     * @param startRow 削除開始行
     * @param endRow   削除終了行
     */
    public void deleteRows(XSSFSheet sheet, int startRow, int endRow) {
        for (int i = startRow; i <= endRow; i++) {
            sheet.shiftRows(startRow, sheet.getLastRowNum(), -1);
        }
    }

    /**
     * 行範囲における最終列番号を取得する。
     *
     * @param sheet         対象シート
     * @param firstRowIndex 開始行
     * @param lastRowIndex  終了行
     * @return 最終列番号
     */
    public int getLastColumnNum(Sheet sheet, int firstRowIndex, int lastRowIndex) {
        // 最終列の取得
        int rangeLastColumn = -1;
        for (int i = firstRowIndex; i <= lastRowIndex; i++) {
            Row row = sheet.getRow(i);
            if (row == null) {
                continue;
            }
            Iterator<Cell> rowIterator = row.iterator();
            while (rowIterator.hasNext()) {
                Cell cell = rowIterator.next();
                if (cell != null) {
                    if (rangeLastColumn < cell.getColumnIndex()) {
                        rangeLastColumn = cell.getColumnIndex();
                    }
                }
            }
        }
        return rangeLastColumn;
    }

    /**
     * Excelファイルを読み込み、2次元配列を返す
     *
     * @param sheet シート
     * @return 二次元配列
     */
    public Object[][] excelReader(Sheet sheet) {
        Object[][] table = new Object[sheet.getLastRowNum() + 1][];

        // 最終列の取得
        int lastColumn = getLastColumnNum(sheet, 0, sheet.getLastRowNum());

        Iterator<Row> rowIterator = sheet.rowIterator();
        int rowCount = 0;

        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();
            Object[] record = new Object[row.getLastCellNum()];

            for(int i = 0; i < lastColumn; i++) {
                Cell cell = getCell(sheet, rowCount, i);
                if(cell == null){
                    record[i] = "";
                    continue;
                }
                switch (cell.getCellTypeEnum()) {
                    case STRING:
                        record[i] = cell.getStringCellValue();
                        break;
                    case NUMERIC:
                        if (DateUtil.isCellDateFormatted(cell)) {
                            // 日付だった場合
                            java.util.Date date = cell.getDateCellValue();
                            // java.time.DateTimeFormatterで文字列化
                            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
                            LocalDateTime localDateTime = LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
                            record[i] = dateTimeFormatter.format(localDateTime);
                            break;
                        } else {
                            record[i] = String.valueOf((int) cell.getNumericCellValue());
                            break;
                        }
                }
            }
            table[rowCount] = record;
            rowCount++;
        }
        return table;
    }
}
