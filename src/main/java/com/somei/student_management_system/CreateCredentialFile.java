package com.somei.student_management_system;

import java.io.IOException;
import java.security.GeneralSecurityException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.bridge.SLF4JBridgeHandler;

/**
 * Google認証を行い、Credentialファイルを作成する。
 */
public class CreateCredentialFile {
    private static final Logger LOGGER = LoggerFactory.getLogger(CreateCredentialFile.class);

    /**
     * Google認証を行い、Credentialファイルを作成する。<br>
     * ブラウザが起動し、Googleの許可リクエスト画面が表示される。
     *
     * @param args
     * @throws IOException
     * @throws GeneralSecurityException
     */
    public static void main(String[] args) throws IOException, GeneralSecurityException {
        SLF4JBridgeHandler.removeHandlersForRootLogger();
        SLF4JBridgeHandler.install();

        MyTestApp1.getGoogleService().authorize();
        LOGGER.info("Credentialファイルを保存しました。");
    }
}