package com.somei.student_management_system;

import java.io.IOException;
import java.io.Reader;
import java.security.GeneralSecurityException;
import java.util.Collection;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.auth.oauth2.CredentialRefreshListener;
import com.google.api.client.auth.oauth2.TokenErrorResponse;
import com.google.api.client.auth.oauth2.TokenResponse;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow.Builder;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import com.google.api.services.sheets.v4.Sheets;

public class GoogleService {
    private static final Logger LOGGER = LoggerFactory.getLogger(GoogleService.class);

    // Global instance of the JSON factory.
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

    // Global instance of the HTTP transport.
    private static HttpTransport HTTP_TRANSPORT = null;

    // Reader for client secret.
    private Reader clientSecretReader;

    // Collection of authorization scopes
    private Collection<String> authorizationScopes;

    // Directory to store user credentials
    private String credentialStoreDirectory;

    // application name
    private String applicationName;

    /**
     * Constructor.
     *
     * @param clientSecretReader       reader for client secret
     * @param authorizationScopes      collection of authorization scopes
     * @param credentialStoreDirectory directory to store user credentials
     * @param applicationName          application name
     */
    public GoogleService(Reader clientSecretReader, Collection<String> authorizationScopes, String credentialStoreDirectory, String applicationName) {
        this.clientSecretReader = clientSecretReader;
        this.authorizationScopes = authorizationScopes;
        this.credentialStoreDirectory = credentialStoreDirectory;
        this.applicationName = applicationName;
    }

    // Credential.
    private Credential credential = null;

    /**
     * Creates an authorized Credential object.
     *
     * @throws IOException
     * @throws GeneralSecurityException
     */
    public void authorize() throws IOException, GeneralSecurityException {
        if (credential != null) {
            return;
        }
        // Load client secrets.
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, clientSecretReader);
        // instance of the {@link FileDataStoreFactory}.
        FileDataStoreFactory dataStoreFactory = new FileDataStoreFactory(new java.io.File(credentialStoreDirectory));
        // ↑ Windowsでは「unable to change permissions～」ログが出力される
        if (HTTP_TRANSPORT == null) {
            HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        }
        // Build flow and trigger user authorization request.
        Builder builder = new GoogleAuthorizationCodeFlow.Builder(HTTP_TRANSPORT, JSON_FACTORY,
                clientSecrets, authorizationScopes);
        builder.setDataStoreFactory(dataStoreFactory).setAccessType("offline");
        // ↑
        // AccessType「offline」でRefreshTokenを得る(AccessTokenのexpire前60秒以後のAPI呼出時に自動refreshが行われるようになる)
        builder.addRefreshListener(new CredentialRefreshListener() {
            @Override
            public void onTokenResponse(Credential credential, TokenResponse tokenResponse) throws IOException {
                LOGGER.info("AccessTokenのrefreshが成功しました。(AccessToken=[{}], ExpiresInSeconds={}, RefreshToken=[{}])",
                        credential.getAccessToken(), credential.getExpiresInSeconds(), credential.getRefreshToken());
            }

            @Override
            public void onTokenErrorResponse(Credential credential, TokenErrorResponse tokenErrorResponse) throws IOException {
                LOGGER.error("AccessTokenのrefreshが失敗しました。(Error=[{}], ErrorDescription=[{}], ErrorUri=[{}])",
                        tokenErrorResponse.getError(), tokenErrorResponse.getErrorDescription(), tokenErrorResponse.getErrorUri());
            }
        });
        // ↑ AccessTokenのrefresh後のListner
        GoogleAuthorizationCodeFlow flow = builder.build();
        credential = new AuthorizationCodeInstalledApp(flow, new LocalServerReceiver()).authorize("user");
        // ↑ 初回はブラウザがGoogleの許可リクエスト画面を表示する(関連ログも出力される)
        // → 「許可」押下でローカルJettyにリダイレクトされ、Credentialがファイルに保存される
        // → 以後はCredentialファイルがある限りブラウザは起動しない(自動refreshのおかげ)
        // → サーバで実行する場合はローカルPCで作成したCredentialファイルをサーバに配置しておく
        // → 何らかのエラーでサーバ上のCredentialファイルが無効になった場合は当時のファイルを再度配置する
        LOGGER.info("AccessTokenを取得しました。(AccessToken=[{}], ExpiresInSeconds={}, RefreshToken=[{}])",
                credential.getAccessToken(), credential.getExpiresInSeconds(), credential.getRefreshToken());
    }

    /**
     * Build and return an authorized Drive client service.
     *
     * @return an authorized Drive client service
     * @throws GeneralSecurityException
     * @throws IOException
     */
    public Drive getDriveService() throws IOException, GeneralSecurityException {
        if (credential == null) {
            authorize();
        }
        return new Drive.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential).setApplicationName(applicationName).build();
    }

    /**
     * Build and return an authorized Sheets API client service.
     *
     * @return an authorized Sheets API client service
     * @throws GeneralSecurityException
     * @throws IOException
     */
    public Sheets getSheetsService() throws IOException, GeneralSecurityException {
        if (credential == null) {
            authorize();
        }
        return new Sheets.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential).setApplicationName(applicationName).build();
    }

    SheetsWrapper getSheetsWrapperWithWorksheet(String folderName, String spreadsheetName, String worksheetName) throws IOException, GeneralSecurityException {
        DriveWrapper driveWrapper = new DriveWrapper(getDriveService());
        Sheets sheetsService = getSheetsService();

        List<File> folderList = driveWrapper.searchFolder(folderName, DriveWrapper.ROOT_FOLDER_ID);
        // ↑ AccessTokenのexpire前60秒以後、ここでAccessTokenのrefreshが実行される
        String folderId = null;
        if (folderList.size() == 0) {
            folderId = driveWrapper.createFolder(folderName, DriveWrapper.ROOT_FOLDER_ID).getId();
            LOGGER.info("フォルダ '{}' を作成しました。", folderName);
        } else if (folderList.size() == 1) {
            folderId = folderList.get(0).getId();
        } else {
            throw new IOException(String.format("フォルダ '%s' が複数存在しています。", folderName));
        }

        List<File> spreadsheetList = driveWrapper.searchSpreadsheet(spreadsheetName, folderId);
        SheetsWrapper sheetsWrapper = null;
        if (spreadsheetList.size() == 0) {
            String spreadsheetId = driveWrapper.createSpreadsheet(spreadsheetName, folderId).getId();
            sheetsWrapper = new SheetsWrapper(sheetsService, spreadsheetId);
            LOGGER.info("スプレッドシート '{}' を作成しました。", spreadsheetName);
            sheetsWrapper.renameWorksheet(0, worksheetName);
            LOGGER.info("ワークシート '{}' を作成しました。", worksheetName);
        } else if (spreadsheetList.size() == 1) {
            String spreadsheetId = spreadsheetList.get(0).getId();
            sheetsWrapper = new SheetsWrapper(sheetsService, spreadsheetId);
            if (sheetsWrapper.getWorksheetProperties(worksheetName) == null) {
                sheetsWrapper.addWorksheet(worksheetName);
                LOGGER.info("ワークシート '{}' を作成しました。", worksheetName);
            }
        } else {
            throw new IOException(String.format("スプレッドシート '%s' が複数存在しています。", spreadsheetName));
        }

        return sheetsWrapper;
    }
}