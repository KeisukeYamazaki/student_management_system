package com.somei.student_management_system;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;

public class DriveWrapper {
    private Drive driveService;
    private static final String QUERY_FORMAT = "name = '%s' and '%s' in parents and mimeType = '%s' and trashed = false";
    private static final String MIMETYPE_FOLDER = "application/vnd.google-apps.folder";
    private static final String MIMETYPE_SPREADSHEET = "application/vnd.google-apps.spreadsheet";
    public static final String ROOT_FOLDER_ID = "root";

    public DriveWrapper(Drive driveService) {
        this.driveService = driveService;
    }

    public List<File> searchFile(String name, String parentFolderId, String mimeType) throws IOException {
        String query = String.format(QUERY_FORMAT, name, parentFolderId, mimeType);
        return RequestUtil.executeWithRetry(driveService.files().list().setQ(query)).getFiles();
    }

    public List<File> searchFolder(String name, String parentFolderId) throws IOException {
        return searchFile(name, parentFolderId, MIMETYPE_FOLDER);
    }

    public List<File> searchSpreadsheet(String name, String parentFolderId) throws IOException {
        return searchFile(name, parentFolderId, MIMETYPE_SPREADSHEET);
    }

    public File createFile(String name, String parentFolderId, String mimeType) throws IOException {
        File metadata = new File().setName(name).setMimeType(mimeType).setParents(Arrays.asList(parentFolderId));
        return RequestUtil.executeWithRetry(driveService.files().create(metadata));
    }

    public File createFolder(String name, String parentFolderId) throws IOException {
        return createFile(name, parentFolderId, MIMETYPE_FOLDER);
    }

    public File createSpreadsheet(String name, String parentFolderId) throws IOException {
        return createFile(name, parentFolderId, MIMETYPE_SPREADSHEET);
    }
}
