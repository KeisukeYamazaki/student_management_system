package com.somei.student_management_system.login.bean;

import com.somei.student_management_system.login.domain.model.NameList;
import com.somei.student_management_system.login.domain.service.StudentService;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

@Component
public class MakeExcelNameSheet {

    @Autowired
    StudentService studentService;

    @Autowired
    PoiMethods poiMethods;

    /**
     * エクセルの名簿作成メソッド
     *
     * @param classList 名簿を作成するクラス名のリスト
     * @param fileName  作成するファイル名
     */
    public void MakeExcelNameSheet(List<String> classList, String fileName) {
        // カレントパスの取得
        String basePath = System.getProperty("user.dir");

        // 名簿のテンプレートファイルの取得
        FileInputStream in = null;
        XSSFWorkbook wb = null;
        try {
            in = new FileInputStream(basePath + "/src/main/resources/excel/meiboTemplate.xlsx");
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

        // テンプレートファイルに名前を入力したものを取得する
        wb = excelNameSheetWriter(wb, classList);

        // 名簿ファイルの作成
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

    /**
     * エクセルテンプレートに名前を入力するメソッド
     *
     * @param wb        名簿テンプレートのワークブック
     * @param classList 名簿を作成するクラス名のリスト
     */
    private XSSFWorkbook excelNameSheetWriter(XSSFWorkbook wb, List<String> classList) {

        // １ページの行数
        int sheetRows = 32;

        // シートの取得
        XSSFSheet sheet = wb.getSheetAt(0);

        // 最終行の取得
        int lastRow = sheet.getLastRowNum();

        // 名簿に名前を記入していく
        for (int i = 0; i < classList.size(); i++) {

            // 引数で渡されたクラスのリストを取得
            List<NameList> list = studentService.selectManyByHomeRoom(classList.get(i));

            // クラス名の入力（２箇所）
            poiMethods.getCell(sheet, 1 + sheetRows * (i * 2), 0).setCellValue(classList.get(i));
            poiMethods.getCell(sheet, 1 + sheetRows + sheetRows * (i * 2), 0).setCellValue(classList.get(i));

            // 生徒名の入力（２箇所）
            for (int j = 0, row = 3; j < list.size(); j++) {
                poiMethods.getCell(sheet, row + sheetRows * (i * 2), 1).setCellValue(list.get(j).getStudentName());
                poiMethods.getCell(sheet, row + sheetRows + sheetRows * (i * 2), 1).setCellValue(list.get(j).getStudentName());
                row++;
            }
        }

        // 使用しなかった行は削除する
        if(classList.size() != 8) {
            poiMethods.deleteRows(sheet, ( sheetRows * classList.size() * 2 ) , lastRow);
        }

        // ワークブックを返す
        return wb;
    }
}
