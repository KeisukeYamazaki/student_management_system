package com.somei.student_management_system.login.bean;

import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

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
     * 背景色塗りつぶしメソッド
     * <p>
     * 指定範囲のセルを灰色に塗りつぶす
     * </p>
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
     * @param sheet            シート
     * @param startRow 削除開始行
     * @param endRow 削除終了行
     */
    public void deleteRows(XSSFSheet sheet,  int startRow, int endRow) {
        for(int i = startRow; i <= endRow; i++) {
            sheet.shiftRows(startRow, sheet.getLastRowNum(),-1);
        }
    }
}
