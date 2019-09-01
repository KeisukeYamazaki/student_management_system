package com.somei.student_management_system;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.googleapis.services.json.AbstractGoogleJsonClientRequest;

public class RequestUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(RequestUtil.class);
    private static final int RETRY_COUNT = 10;
    private static final int RETRY_INTERVAL_SECONDS = 10;

    static <R extends AbstractGoogleJsonClientRequest<T>, T> T executeWithRetry(R request) throws IOException {
        for (int i = 0; i < (RETRY_COUNT - 1); i++) {
            try {
                return request.execute();
            } catch (GoogleJsonResponseException e) {
                if (e.getStatusCode() == 503) {
                    LOGGER.warn("Googleからリトライ可能なエラーが返却されました。", e);
                    LOGGER.warn("{}秒後にリトライを行います。({}/{}回目)", RETRY_INTERVAL_SECONDS * (i + 1), i + 1, RETRY_COUNT);
                    try {
                        Thread.sleep(RETRY_INTERVAL_SECONDS * (i + 1) * 1000L);
                    } catch (InterruptedException e2) {
                        // do nothing
                    }
                }
            }
        }
        return request.execute();
    }
}