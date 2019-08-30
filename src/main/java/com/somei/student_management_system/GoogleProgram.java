package com.somei.student_management_system;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.gdata.client.spreadsheet.SpreadsheetQuery;
import com.google.gdata.client.spreadsheet.SpreadsheetService;
import com.google.gdata.client.spreadsheet.WorksheetQuery;
import com.google.gdata.data.PlainTextConstruct;
import com.google.gdata.data.spreadsheet.CellEntry;
import com.google.gdata.data.spreadsheet.CellFeed;
import com.google.gdata.data.spreadsheet.ListEntry;
import com.google.gdata.data.spreadsheet.ListFeed;
import com.google.gdata.data.spreadsheet.SpreadsheetEntry;
import com.google.gdata.data.spreadsheet.SpreadsheetFeed;
import com.google.gdata.data.spreadsheet.WorksheetEntry;
import com.google.gdata.data.spreadsheet.WorksheetFeed;
import org.springframework.stereotype.Component;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Component
public class GoogleProgram {

    // アプリケーション名 (任意)
    private static final String APPLICATION_NAME = "student-management-system";

    // アカウント
    private static final String ACCOUNT_P12_ID = "spring-api@expanded-port-251401.iam.gserviceaccount.com";
    private static final File P12FILE = new File("src/main/resources/My First Project-3009cd2bacfd.p12");

    // 認証スコープ
    private static final List<String> SCOPES = Arrays.asList(
            "https://docs.google.com/feeds",
            "https://spreadsheets.google.com/feeds");

    // Spreadsheet API URL
    private static final String SPREADSHEET_URL = "https://spreadsheets.google.com/feeds/spreadsheets/private/full";

    private static final URL SPREADSHEET_FEED_URL;

    static {
        try {
            SPREADSHEET_FEED_URL = new URL(SPREADSHEET_URL);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 認証処理.
     *
     * @return
     */
    public static Credential authorize() throws Exception {
        System.out.println("authorize in");

        HttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
        JsonFactory jsonFactory = new JacksonFactory();

        GoogleCredential credential = new GoogleCredential.Builder()
                .setTransport(httpTransport)
                .setJsonFactory(jsonFactory)
                .setServiceAccountId(ACCOUNT_P12_ID)
                .setServiceAccountPrivateKeyFromP12File(P12FILE)
                .setServiceAccountScopes(SCOPES)
                .build();

        boolean ret = credential.refreshToken();
        // debug dump
        System.out.println("refreshToken:" + ret);

        // debug dump
        if (credential != null) {
            System.out.println("AccessToken:" + credential.getAccessToken());
        }

        System.out.println("authorize out");

        return credential;
    }

    /**
     * サービスの取得.
     *
     * @return
     */
    public static SpreadsheetService getService() throws Exception {
        System.out.println("service in");

        SpreadsheetService service = new SpreadsheetService(APPLICATION_NAME);
        service.setProtocolVersion(SpreadsheetService.Versions.V3);

        Credential credential = authorize();
        service.setOAuth2Credentials(credential);

        // debug dump
        System.out.println("Schema: " + service.getSchema().toString());
        System.out.println("Protocol: " + service.getProtocolVersion().getVersionString());
        System.out.println("ServiceVersion: " + service.getServiceVersion());

        System.out.println("service out");

        return service;
    }

    /**
     * スプレッドシート一覧.
     *
     * @param service
     *
     * @return
     */
    public static List<SpreadsheetEntry> findAllSpreadsheets(SpreadsheetService service) throws Exception {
        System.out.println("findAllSpreadsheets in");

        SpreadsheetFeed feed = service.getFeed(SPREADSHEET_FEED_URL, SpreadsheetFeed.class);

        List<SpreadsheetEntry> spreadsheets = feed.getEntries();

        // debug dump
        for (SpreadsheetEntry spreadsheet : spreadsheets) {
            System.out.println("title: " + spreadsheet.getTitle().getPlainText());
        }

        System.out.println("findAllSpreadsheets out");
        return spreadsheets;
    }

    /**
     * スプレッドシート名で検索.
     *
     * @param service
     * @param spreadsheetName
     *
     * @return
     */
    public static SpreadsheetEntry findSpreadsheetByName(SpreadsheetService service, String spreadsheetName) throws Exception {
        System.out.println("findSpreadsheetByName in");
        SpreadsheetQuery sheetQuery = new SpreadsheetQuery(SPREADSHEET_FEED_URL);
        sheetQuery.setTitleQuery(spreadsheetName);
        SpreadsheetFeed feed = service.query(sheetQuery, SpreadsheetFeed.class);
        SpreadsheetEntry ssEntry = null;
        if (feed.getEntries().size() > 0) {
            ssEntry = feed.getEntries().get(0);
        }
        System.out.println("findSpreadsheetByName out");
        return ssEntry;
    }

    /**
     * ワークシート名で検索.
     *
     * @param service
     * @param ssEntry
     * @param sheetName
     *
     * @return
     */
    public static WorksheetEntry findWorksheetByName(SpreadsheetService service, SpreadsheetEntry ssEntry, String sheetName) throws Exception {
        System.out.println("findWorksheetByName in");
        WorksheetQuery worksheetQuery = new WorksheetQuery(ssEntry.getWorksheetFeedUrl());
        worksheetQuery.setTitleQuery(sheetName);
        WorksheetFeed feed = service.query(worksheetQuery, WorksheetFeed.class);
        WorksheetEntry wsEntry = null;
        if (feed.getEntries().size() > 0){
            wsEntry = feed.getEntries().get(0);
        }
        System.out.println("findWorksheetByName out");
        return wsEntry;
    }

    /**
     * ワークシートを追加.
     *
     * @param service
     * @param ssEntry
     * @param sheetName
     * @param colNum
     * @param rowNum
     *
     * @return
     */
    public static WorksheetEntry addWorksheet(SpreadsheetService service, SpreadsheetEntry ssEntry, String sheetName, int colNum, int rowNum) throws Exception {
        System.out.println("addWorksheet in");
        WorksheetEntry wsEntry = new WorksheetEntry();
        wsEntry.setTitle(new PlainTextConstruct(sheetName));
        wsEntry.setColCount(colNum);
        wsEntry.setRowCount(rowNum);
        URL worksheetFeedURL = ssEntry.getWorksheetFeedUrl();
        System.out.println("addWorksheet out");
        return service.insert(worksheetFeedURL, wsEntry);
    }

    /**
     * ワークシートを削除.
     *
     * @param wsEntry
     *
     * @return
     */
    public static void deleteWorksheet(WorksheetEntry wsEntry) throws Exception {
        System.out.println("deleteWorksheet in");
        wsEntry.delete();
        System.out.println("deleteWorksheet out");
    }

    /**
     * ワークシートのタイトル行を追加.
     *
     * @param service
     * @param wsEntry
     * @param header
     * @param query
     *
     * @return
     */
    public static void insertHeadRow(SpreadsheetService service, WorksheetEntry wsEntry, List<String> header, String query) throws Exception {
        System.out.println("insertHeadRow in");

        URL cellFeedUrl = new URI(wsEntry.getCellFeedUrl().toString() + query).toURL();
        CellFeed cellFeed = service.getFeed(cellFeedUrl, CellFeed.class);

        for (int i=0; i<header.size(); i++) {
            cellFeed.insert(new CellEntry(1, i+1, header.get(i)));
        }

        System.out.println("insertHeadRow out");
    }

    /**
     * ワークシートに行を追加.
     *
     * @param service
     * @param wsEntry
     * @param values
     *
     * @return
     */
    public static void insertDataRow(SpreadsheetService service, WorksheetEntry wsEntry, Map<String, Object> values) throws Exception {
        System.out.println("insertDataRow in");

        ListEntry dataRow = new ListEntry();

        values.forEach((title,value)->{
            dataRow.getCustomElements().setValueLocal(title, value.toString());
        });

        URL listFeedUrl = wsEntry.getListFeedUrl();
        service.insert(listFeedUrl, dataRow);

        System.out.println("insertDataRow out");
    }

    /**
     * ワークシートの行を更新.
     *
     * @param service
     * @param wsEntry
     * @param values
     *
     * @return
     */
    public static void updateDataRow(SpreadsheetService service, WorksheetEntry wsEntry, int rowNum, Map<String, Object> values) throws Exception {
        System.out.println("updateDataRow in");

        URL listFeedUrl = wsEntry.getListFeedUrl();
        ListFeed listFeed = service.getFeed(listFeedUrl, ListFeed.class);

        ListEntry row = listFeed.getEntries().get(rowNum);

        values.forEach((title,value)->{
            row.getCustomElements().setValueLocal(title, value.toString());
        });

        row.update();

        System.out.println("updateDataRow out");
    }

    /**
     * 範囲指定クエリー.
     *
     * @param minrow
     * @param maxrow
     * @param mincol
     * @param maxcol
     *
     * @return
     */
    public static String makeQuery(int minrow, int maxrow, int mincol, int maxcol) {
        String base = "?min-row=MINROW&max-row=MAXROW&min-col=MINCOL&max-col=MAXCOL";
        return base.replaceAll("MINROW", String.valueOf(minrow))
                .replaceAll("MAXROW", String.valueOf(maxrow))
                .replaceAll("MINCOL", String.valueOf(mincol))
                .replaceAll("MAXCOL", String.valueOf(maxcol));
    }

}
