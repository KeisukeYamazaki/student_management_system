package com.somei.student_management_system.login.bean;

import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Component;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Component
public class CreateZip {

    public String createZip(File[] files) {

        // zipファイルのファイル名作成用に日付を取得
        LocalDate data = LocalDate.now();

        // ファイル名を決める
        String zipFilename = DateTimeFormatter.ofPattern("yyyyMMdd").format(data) + "List.zip";

        // zip処理
        ZipOutputStream zos = null;
        try {
            zos = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(new File(zipFilename))));
            createZip(zos, files);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeQuietly(zos);
        }

        return zipFilename;

    }

    private void createZip(ZipOutputStream zos, File[] files) throws IOException {
        byte[] buf = new byte[1024];
        InputStream is = null;
        try {
            for (File file : files) {
                ZipEntry entry = new ZipEntry(file.getName());
                zos.putNextEntry(entry);
                is = new BufferedInputStream(new FileInputStream(file));
                int len = 0;
                while ((len = is.read(buf)) != -1) {
                    zos.write(buf, 0, len);
                }
            }
        } finally {
            IOUtils.closeQuietly(is);
        }
    }
}
