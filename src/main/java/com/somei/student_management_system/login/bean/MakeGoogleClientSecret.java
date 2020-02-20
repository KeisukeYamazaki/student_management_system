package com.somei.student_management_system.login.bean;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.somei.student_management_system.login.domain.model.GoogleSheetJson;
import com.somei.student_management_system.login.domain.model.Installed;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;

public class MakeGoogleClientSecret {

    private static final String GOOGLE_CLIENT_SECRET_PATH =
            System.getProperty("user.dir") + "/src/main/resources/google_client_secret.json";

    public static void makeGoogleClientSecret() {

        // google_client_secretの生成
        try {
            FileWriter fw = new FileWriter(GOOGLE_CLIENT_SECRET_PATH);
            try (PrintWriter pw = new PrintWriter(new BufferedWriter(fw))) {
                // 環境変数から取得して設定する
                List<String> redirectUrisList = Arrays.asList("urn:ietf:wg:oauth:2.0:oob","http://localhost");
                GoogleSheetJson googleSheetJson = new GoogleSheetJson();
                googleSheetJson.installed = new Installed();
                googleSheetJson.installed.clientId = System.getenv("CLIENT_ID");
                googleSheetJson.installed.projectId = System.getenv("PROJECT_ID");
                googleSheetJson.installed.authUri = "https://accounts.google.com/o/oauth2/auth";
                googleSheetJson.installed.authProviderX509CertUrl = "https://oauth2.googleapis.com/token";
                googleSheetJson.installed.clientSecret = System.getenv("CLIENT_SECRET");
                googleSheetJson.installed.redirectUris = redirectUrisList;
                // MapをJson形式の文字列に変換
                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                String json = gson.toJson(googleSheetJson);
                // ファイルに書き込み
                pw.println(json);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
