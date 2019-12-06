package com.somei.student_management_system;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.bridge.SLF4JBridgeHandler;

import com.google.api.services.drive.DriveScopes;
import com.google.api.services.sheets.v4.SheetsScopes;

/**
 * Googleスプレッドシートへの書き込みを行う。
 */
public class MyTestApp1 {
    private static final Logger LOGGER = LoggerFactory.getLogger(MyTestApp1.class);

    // Application name.
    private static final String APPLICATION_NAME = "MyTestApp1";

    // Return reader for Google client secret.
    private static Reader getClientSecretReader() {
        return new InputStreamReader(MyTestApp1.class.getResourceAsStream("/google_client_secret.json"));
    }

    // Google authorization scopes required by this application.
    // If modifying these scopes, delete your previously saved credentials.
    private static final List<String> AUTHORIZATION_SCOPE_LIST = Arrays.asList(DriveScopes.DRIVE, SheetsScopes.SPREADSHEETS);

    // Directory to store Google user credentials for this application.
    private static final String CREDENTIAL_STORE_DIRECTORY = System.getProperty("user.home") + "/.google_credentials/" + APPLICATION_NAME;

    public static void main(String[] args) throws IOException, GeneralSecurityException {
        SLF4JBridgeHandler.removeHandlersForRootLogger();
        SLF4JBridgeHandler.install();

        final String folderName = "フォルダ1";
        final String spreadsheetName = "2019_中学部_定期試験結果まとめ";
        final String worksheetName = "２学期期末・後期中間";

        // Build a new authorized API client service.
        GoogleService googleService = getGoogleService();
        SheetsWrapper sheetsWrapper = googleService.getSheetsWrapperWithWorksheet(folderName, spreadsheetName, worksheetName);
        int lastRowNumWithValue = sheetsWrapper.getLastRowNumberWithValue(worksheetName, 1);

        /*
        // 値の入っている最後の行の次の行から書き込み
        Object[][] values = { { 1, "A" }, { 2.1D, "B" }, { 2.50E-3, "C" } };
        sheetsWrapper.setValues(worksheetName, 1, lastRowNumWithValue + 1, values);
        LOGGER.info("書き込みました。");
        */

        //値を取得
        List<List<Object>> valueList1 = sheetsWrapper.getValues("２学期期末・後期中間", 4, 4, 27, 63);
        List<List<Object>> valueList2 = sheetsWrapper.getValues("２学期期末・後期中間", 32, 4, 55, 63);
        for(List<Object> list : valueList1) {
            System.out.println(list);
        }
        System.out.println(valueList1);
        List<String> list = new ArrayList<>();
        for (int i = 0; i < valueList1.get(0).size(); i++) {
            String str = String.valueOf(valueList1.get(0).get(i));
            if (str.contains(".")) {
                continue;
            }
            if (i == 0 || i >= 13) {
                list.add(str);
            }
        }
        System.out.println(list);
    }

    static GoogleService getGoogleService() {
        return new GoogleService(getClientSecretReader(), AUTHORIZATION_SCOPE_LIST, CREDENTIAL_STORE_DIRECTORY, APPLICATION_NAME);
    }
}