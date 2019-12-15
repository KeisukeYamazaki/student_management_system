package com.somei.student_management_system.login.bean;

import com.somei.student_management_system.login.domain.model.ZenkenExcelData;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.bbreak.excella.core.util.PoiUtil;

import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Component
public class ZenkenExcelProcessing {

    /**
     * 受け取ったエクセルファイルを成績リストに変換する
     *
     * @param is エクセルファイルのストリーム
     * @return List<ZenkenExcelData> 入力されたもし結果のリスト
     */
    public List<ZenkenExcelData> readExcelFile(InputStream is) throws IOException, InvalidFormatException {

        // 成績格納リストを作成
        List<ZenkenExcelData> list = new ArrayList<>();

        // ワークブックを取得
        Workbook workbook = WorkbookFactory.create(is);

        // 先頭のシートを指定
        Sheet sheet = workbook.getSheetAt(0);

        // 最終行の取得
        int count = PoiUtil.getLastRowNum(sheet, 0, 0);

        return null;


    }
}
