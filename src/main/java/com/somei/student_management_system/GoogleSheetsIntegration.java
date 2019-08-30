package com.somei.student_management_system;

import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.AppendValuesResponse;
import com.google.api.services.sheets.v4.model.BatchUpdateSpreadsheetRequest;
import com.google.api.services.sheets.v4.model.BatchUpdateValuesRequest;
import com.google.api.services.sheets.v4.model.BatchUpdateValuesResponse;
import com.google.api.services.sheets.v4.model.CopyPasteRequest;
import com.google.api.services.sheets.v4.model.GridRange;
import com.google.api.services.sheets.v4.model.Request;
import com.google.api.services.sheets.v4.model.Spreadsheet;
import com.google.api.services.sheets.v4.model.SpreadsheetProperties;
import com.google.api.services.sheets.v4.model.UpdateSpreadsheetPropertiesRequest;
import com.google.api.services.sheets.v4.model.UpdateValuesResponse;
import com.google.api.services.sheets.v4.model.ValueRange;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GoogleSheetsIntegration {
    private static Sheets sheetsService;
    private static String SPREADSHEET__ID = "1HMaUkOBiQnw3UVybzQ5TDmLfBJIWqvSP9EPLWFhzJkw";


    public static void setup() throws GeneralSecurityException, IOException {
        sheetsService = SheetsServiceUtil.getSheetsService();
    }


    public void whenWriteSheet__thenReadSheetOk() throws IOException {
        ValueRange body = new ValueRange()
                .setValues(Arrays.asList(
                        Arrays.asList("Expenses January"),
                        Arrays.asList("books", "30"),
                        Arrays.asList("pens", "10"),
                        Arrays.asList("Expenses February"),
                        Arrays.asList("clothes", "20"),
                        Arrays.asList("shoes", "5")));
        UpdateValuesResponse result = sheetsService.spreadsheets().values()
                .update(SPREADSHEET__ID, "A1", body)
                .setValueInputOption("RAW")
                .execute();

        List<ValueRange> data = new ArrayList<>();
        data.add(new ValueRange()
                .setRange("D1")
                .setValues(Arrays.asList(
                        Arrays.asList("January Total", "=B2+B3"))));
        data.add(new ValueRange()
                .setRange("D4")
                .setValues(Arrays.asList(
                        Arrays.asList("February Total", "=B5+B6"))));

        BatchUpdateValuesRequest batchBody = new BatchUpdateValuesRequest()
                .setValueInputOption("USER__ENTERED")
                .setData(data);

        BatchUpdateValuesResponse batchResult = sheetsService.spreadsheets().values()
                .batchUpdate(SPREADSHEET__ID, batchBody)
                .execute();

        ValueRange appendBody = new ValueRange()
                .setValues(Arrays.asList(
                        Arrays.asList("Total", "=E1+E4")));
        AppendValuesResponse appendResult = sheetsService.spreadsheets().values()
                .append(SPREADSHEET__ID, "A1", appendBody)
                .setValueInputOption("USER__ENTERED")
                .setInsertDataOption("INSERT__ROWS")
                .setIncludeValuesInResponse(true)
                .execute();

        ValueRange total = appendResult.getUpdates().getUpdatedData();
    }


    public void test() throws IOException {
        Spreadsheet spreadSheet = new Spreadsheet().setProperties(
                new SpreadsheetProperties().setTitle("My Spreadsheet"));
        Spreadsheet result = sheetsService
                .spreadsheets()
                .create(spreadSheet).execute();
    }


    public void whenUpdateSpreadSheetTitle__thenOk() throws IOException {
        UpdateSpreadsheetPropertiesRequest updateSpreadSheetRequest
                = new UpdateSpreadsheetPropertiesRequest().setFields("** ")
                .setProperties(new SpreadsheetProperties().setTitle("Expenses"));

        CopyPasteRequest copyRequest = new CopyPasteRequest()
                .setSource(new GridRange().setSheetId(0)
                        .setStartColumnIndex(0).setEndColumnIndex(2)
                        .setStartRowIndex(0).setEndRowIndex(1))
                .setDestination(new GridRange().setSheetId(1)
                        .setStartColumnIndex(0).setEndColumnIndex(2)
                        .setStartRowIndex(0).setEndRowIndex(1))
                .setPasteType("PASTE__VALUES");

        List<Request> requests = new ArrayList<>();
        requests.add(new Request()
                .setCopyPaste(copyRequest));
        requests.add(new Request()
                .setUpdateSpreadsheetProperties(updateSpreadSheetRequest));

        BatchUpdateSpreadsheetRequest body
                = new BatchUpdateSpreadsheetRequest().setRequests(requests);

        sheetsService.spreadsheets().batchUpdate(SPREADSHEET__ID, body).execute();
    }

}