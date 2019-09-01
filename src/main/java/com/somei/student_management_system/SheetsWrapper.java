package com.somei.student_management_system;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.AddSheetRequest;
import com.google.api.services.sheets.v4.model.BatchUpdateSpreadsheetRequest;
import com.google.api.services.sheets.v4.model.GridProperties;
import com.google.api.services.sheets.v4.model.Request;
import com.google.api.services.sheets.v4.model.Sheet;
import com.google.api.services.sheets.v4.model.SheetProperties;
import com.google.api.services.sheets.v4.model.Spreadsheet;
import com.google.api.services.sheets.v4.model.UpdateSheetPropertiesRequest;
import com.google.api.services.sheets.v4.model.ValueRange;

public class SheetsWrapper {
    private Sheets sheetsService;
    private Spreadsheet spreadsheet;

    /**
     * コンストラクタ.
     *
     * @param sheetsService
     * @param fileId
     */
    public SheetsWrapper(Sheets sheetsService, String fileId) throws IOException {
        this.sheetsService = sheetsService;
        this.spreadsheet = RequestUtil.executeWithRetry(sheetsService.spreadsheets().get(fileId).setIncludeGridData(false));
    }

    public void renameWorksheet(int index, String newName) throws IOException {
        SheetProperties sheetProperties = new SheetProperties().setIndex(index).setTitle(newName);
        UpdateSheetPropertiesRequest updateSheetPropertiesRequest = new UpdateSheetPropertiesRequest().setProperties(sheetProperties).setFields("title");
        List<Request> requests = new ArrayList<>();
        requests.add(new Request().setUpdateSheetProperties(updateSheetPropertiesRequest));
        RequestUtil.executeWithRetry(sheetsService.spreadsheets().batchUpdate(spreadsheet.getSpreadsheetId(), new BatchUpdateSpreadsheetRequest().setRequests(requests)));
        spreadsheet = RequestUtil.executeWithRetry(sheetsService.spreadsheets().get(spreadsheet.getSpreadsheetId()).setIncludeGridData(false));
        // ↑
        // rename後のgetSheets().get().getProperties().getTitle()がrename前の値を返す問題の対応
    }

    // 見つからないときはnullを返す
    public SheetProperties getWorksheetProperties(String worksheetName) {
        for (int index = 0; index < spreadsheet.getSheets().size(); index++) {
            Sheet ws = spreadsheet.getSheets().get(index);
            if (ws.getProperties().getTitle().equals(worksheetName)) {
                return ws.getProperties();
            }
        }
        return null;
    }

    public void addWorksheet(String worksheetName) throws IOException {
        SheetProperties sheetProperties = new SheetProperties().setTitle(worksheetName);
        AddSheetRequest addSheetRequest = new AddSheetRequest().setProperties(sheetProperties);
        List<Request> requests = new ArrayList<>();
        requests.add(new Request().setAddSheet(addSheetRequest));
        RequestUtil.executeWithRetry(sheetsService.spreadsheets().batchUpdate(spreadsheet.getSpreadsheetId(), new BatchUpdateSpreadsheetRequest().setRequests(requests)));
        spreadsheet = RequestUtil.executeWithRetry(sheetsService.spreadsheets().get(spreadsheet.getSpreadsheetId()).setIncludeGridData(false));
        // ↑ add後のgetSheets()がaddしたシートを返さない問題の対応
    }

    /**
     * 値の取得.
     *
     * @param worksheetName
     * @param startColNum
     * @param startRowNum
     * @param endColNum
     * @param endRowNum
     * @return
     */
    public List<List<Object>> getValues(String worksheetName, int startColNum, int startRowNum, int endColNum, int endRowNum) throws IOException {
        boolean specifyColRow = startColNum > 0 || startRowNum > 0 || endColNum > 0 || endRowNum > 0;
        StringBuilder rangeBuf = new StringBuilder();
        rangeBuf.append(worksheetName);
        rangeBuf.append(specifyColRow ? '!' : "");
        rangeBuf.append((startColNum > 0) ? bijectiveBase26(startColNum) : "");
        rangeBuf.append((startRowNum > 0) ? startRowNum : "");
        rangeBuf.append(specifyColRow ? ':' : "");
        rangeBuf.append((endColNum > 0) ? bijectiveBase26(endColNum) : "");
        rangeBuf.append((endRowNum > 0) ? endRowNum : "");
        return RequestUtil.executeWithRetry(sheetsService.spreadsheets().values().get(spreadsheet.getSpreadsheetId(), rangeBuf.toString())).getValues();
    }

    /**
     * 指定列の最終行番号を取得.
     *
     * @param worksheetName
     * @param colNum
     * @return
     */
    public int getLastRowNumberWithValue(String worksheetName, int colNum) throws IOException {
        // 指定列で値の入っている最後の行の行番号を取得
        // (ワークシート名のみ指定すれば「全列で」となるがメモリ節約のため列指定をできるようにしておく)
        List<List<Object>> values = getValues(worksheetName, colNum, 0, colNum, 0);
        if (values != null) {
            return values.size();
        }
        return 0;
    }

    /**
     * 値の入力.
     *
     * @param worksheetName
     * @param startColNum
     * @param startRowNum
     * @param values
     */
    public void setValues(String worksheetName, int startColNum, int startRowNum, Object[][] values) throws IOException {
        // 指定列・行が現在のワークシートの大きさを超える場合にはワークシートを必要なだけ拡張する
        // 指定位置に既存の値があれば上書きする
        List<List<Object>> valueList = new ArrayList<>();
        for (Object[] row : values) {
            valueList.add(Arrays.asList(row));
        }
        setValues(worksheetName, startColNum, startRowNum, valueList);
    }

    public void setValues(String worksheetName, int startColNum, int startRowNum, Object[] values) throws IOException {
        setValues(worksheetName, startColNum, startRowNum, new Object[][]{values});
    }

    public void setValue(String worksheetName, int colNum, int rowNum, Object value) throws IOException {
        setValues(worksheetName, colNum, rowNum, new Object[][]{{value}});
    }

    public void setValues(String worksheetName, int startColNum, int startRowNum, List<List<Object>> valueList) throws IOException {
        ValueRange valueRange = new ValueRange().setValues(valueList).setMajorDimension("ROWS");
        String range = worksheetName + "!" + bijectiveBase26(startColNum) + startRowNum;
        // 開始位置が範囲内ならupdate、範囲外ならappend
        // (updateは開始位置が範囲外だとエラー、開始位置さえ範囲内ならその後は必要に応じて列・行を追加してくれる)
        // (appendは開始位置が範囲外でも必要に応じて列・行を追加してくれるが、開始位置が空がない場合に上書きしてくれない)
        GridProperties gridProperties = getWorksheetProperties(worksheetName).getGridProperties();
        if (startColNum <= gridProperties.getColumnCount() && startRowNum <= gridProperties.getRowCount()) {
            RequestUtil.executeWithRetry(sheetsService.spreadsheets().values().update(spreadsheet.getSpreadsheetId(), range, valueRange).setValueInputOption("USER_ENTERED"));
        } else {
            RequestUtil.executeWithRetry(sheetsService.spreadsheets().values().append(spreadsheet.getSpreadsheetId(), range, valueRange).setValueInputOption("USER_ENTERED"));
        }
    }

    /**
     * 列番号を列番号文字列に変換.
     *
     * @param n
     * @return
     */
    private String bijectiveBase26(int n) {
        // https://gist.github.com/theazureshadow/4a5a032944f1c9bc0f4a より
        StringBuilder buf = new StringBuilder();
        while (n != 0) {
            buf.append((char) ((n - 1) % 26 + 'A'));
            n = (n - 1) / 26;
        }
        return buf.reverse().toString();
    }
}