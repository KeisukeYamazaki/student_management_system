package com.somei.student_management_system.login.bean;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Component
public class CreateZip {

    @Autowired
    MakePdf makePdf;

    /**
     * クラス名のリスト、ZIPファイル名を受け取りPDFを格納したZIPファイルを返す
     *
     * @param classList クラス名が格納されたリスト
     * @param zipFilename 返すZIPファイルの名前
     * @return deleteList 削除用のリスト
     */
    public List<String> createZip(List<String> classList, String zipFilename) {

        // PDFファイル名
        String pdfName = null;

        // 削除用のPDFファイルの名前リスト
        List<String> deleteList = new ArrayList<>();

        // ZIPファイルを作成
        FileOutputStream fos = null;
        ZipOutputStream zos = null;
        try {
            // アーカイブファイルを用意する。
            byte[] buffer = new byte[1024];
            fos = new FileOutputStream(zipFilename);
            zos = new ZipOutputStream(fos);

            //PDFファイルを作成し、ZIPファイルに入れていく
            for (int i = 0; i < classList.size(); i++) {

                pdfName = makePdf.makePdf(classList.get(i));

                // 削除用のリストに追加
                deleteList.add(pdfName);

                FileInputStream fin = new FileInputStream(pdfName);

                // エントリーを追加
                zos.putNextEntry(new ZipEntry(pdfName));

                int length;
                while ((length = fin.read(buffer)) > 0) {
                    zos.write(buffer, 0, length);
                }

                // アーカイブに含めるファイルの中身
                byte[] data = pdfName.getBytes(StandardCharsets.UTF_8);
                zos.write(data);
                zos.closeEntry();
                fin.close();
            }

        } catch (
                IOException e) {
            e.printStackTrace();
        } finally {
            if (zos != null) {
                try {
                    zos.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }

        return deleteList;
    }
}
