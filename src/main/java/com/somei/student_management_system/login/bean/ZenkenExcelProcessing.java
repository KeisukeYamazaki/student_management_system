package com.somei.student_management_system.login.bean;

import com.somei.student_management_system.GoogleSpreadSheetMethods;
import com.somei.student_management_system.login.domain.model.ZenkenExcelData;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class ZenkenExcelProcessing {

    @Autowired
    PoiMethods poiMethods;

    @Autowired
    GoogleSpreadSheetMethods googleSpreadSheetMethods;

    /**
     * 受け取ったエクセルファイルを成績リストに変換する
     *
     * @param is エクセルファイルのストリーム
     * @return List<ZenkenExcelData> 入力された模試結果のリスト
     */
    public List<ZenkenExcelData> readExcelFile(InputStream is) throws IOException, InvalidFormatException {

        // ワークブックを取得
        Workbook workbook = WorkbookFactory.create(is);

        // 先頭のシートを指定
        Sheet sheet = workbook.getSheetAt(0);

        // データを取得
        Object[][] table = poiMethods.excelReader(sheet);

        // 必要なデータのヘッダー名をMapで作成
        Map<String, Integer> columnMap = new HashMap<String, Integer>() {
            {
                put("実施年度", null);
                put("学年", null);
                put("実施日", null);
                put("氏名", null);
                put("得点英語", null);
                put("得点数学", null);
                put("得点国語", null);
                put("得点理科", null);
                put("得点社会", null);
                put("得点3科", null);
                put("得点5科", null);
                put("偏差値英語", null);
                put("偏差値数学", null);
                put("偏差値国語", null);
                put("偏差値理科", null);
                put("偏差値社会", null);
                put("偏差値3科", null);
                put("偏差値5科", null);
                put("公立1", null);
                put("可能性1", null);
                put("公立2", null);
                put("可能性2", null);
                put("公立3", null);
                put("可能性3", null);
                put("国私立4", null);
                put("国私立5", null);
                put("国私立6", null);
            }
        };

        // ヘッダー行からデータを取得する列番号を取得
        Object[] firstRow = table[0];
        for (String key : columnMap.keySet()) {
            Integer i = 0;
            for (Object obj : firstRow) {
                if (key.equals(obj)) {
                    columnMap.put(key, i);
                    i++;
                } else {
                    i++;
                }
            }
        }

        // 返却用のListを作成
        List<ZenkenExcelData> returnList = new ArrayList<>();

        // データをリストに入れて返す
        for (int j = 1; j < table.length - 1; j++) {
            ZenkenExcelData zkeDate = new ZenkenExcelData();
            // データをセットしていく
            zkeDate.setExamYear((String) table[j][columnMap.get("実施年度")]);
            zkeDate.setMonthName(extractMonth(table[j][columnMap.get("実施日")]));
            zkeDate.setGrade((String) table[j][columnMap.get("学年")]);
            String id = googleSpreadSheetMethods.getId((String) table[j][columnMap.get("氏名")]);
            zkeDate.setStudentId(id);
            zkeDate.setEnglishScore((String) table[j][columnMap.get("得点英語")]);
            zkeDate.setMathScore((String) table[j][columnMap.get("得点数学")]);
            zkeDate.setJapaneseScore((String) table[j][columnMap.get("得点国語")]);
            zkeDate.setScienceScore((String) table[j][columnMap.get("得点理科")]);
            zkeDate.setSocialScore((String) table[j][columnMap.get("得点社会")]);
            zkeDate.setSumThree((String) table[j][columnMap.get("得点3科")]);
            zkeDate.setSumAll((String) table[j][columnMap.get("得点5科")]);
            zkeDate.setEnglishDeviation((String) table[j][columnMap.get("偏差値英語")]);
            zkeDate.setMathDeviation((String) table[j][columnMap.get("偏差値数学")]);
            zkeDate.setJapaneseDeviation((String) table[j][columnMap.get("偏差値国語")]);
            zkeDate.setScienceDeviation((String) table[j][columnMap.get("偏差値理科")]);
            zkeDate.setSocialDeviation((String) table[j][columnMap.get("偏差値社会")]);
            zkeDate.setDevThree((String) table[j][columnMap.get("偏差値3科")]);
            zkeDate.setDevFive((String) table[j][columnMap.get("偏差値5科")]);
            zkeDate.setFirstChoice((String) table[j][columnMap.get("公立1")]);
            zkeDate.setFirstSituation((String) table[j][columnMap.get("可能性1")]);
            zkeDate.setSecondChoice((String) table[j][columnMap.get("公立2")]);
            zkeDate.setSecondSituation((String) table[j][columnMap.get("可能性2")]);
            zkeDate.setThirdChoice((String) table[j][columnMap.get("公立3")]);
            zkeDate.setThirdSituation((String) table[j][columnMap.get("可能性3")]);
            zkeDate.setPrivateSchool1((String) table[j][columnMap.get("国私立4")]);
            zkeDate.setPrivateSchool2((String) table[j][columnMap.get("国私立5")]);
            zkeDate.setPrivateSchool3((String) table[j][columnMap.get("国私立6")]);

            returnList.add(zkeDate);
        }
        return returnList;
    }

    /**
     * 実施日から月だけ抜き出す
     *
     * @param date 年月日の日付
     * @return 月の数字(文字列)
     */
    public String extractMonth(Object date) {
        String newDate = (String) date;
        newDate = newDate.substring(5, 7);
        if(newDate.equals("07") || newDate.equals("08")) {
            newDate = newDate.replaceAll("0", "");
        }
        return newDate;
    }
}
