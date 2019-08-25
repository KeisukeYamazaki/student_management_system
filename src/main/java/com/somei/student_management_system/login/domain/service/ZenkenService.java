package com.somei.student_management_system.login.domain.service;

import com.somei.student_management_system.login.bean.ZenkenProcessing;
import com.somei.student_management_system.login.domain.model.RecordExistence;
import com.somei.student_management_system.login.domain.model.SchoolRecord;
import com.somei.student_management_system.login.domain.model.Student;
import com.somei.student_management_system.login.domain.model.Zenken;
import com.somei.student_management_system.login.domain.repository.NumericDataDao;
import com.somei.student_management_system.login.domain.repository.ZenkenDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class ZenkenService {

    @Autowired
    ZenkenDao zenkendao;

    @Autowired
    NumericDataDao numericDataDao;

    @Autowired
    ZenkenProcessing zenPro;

    /**
     * １件insert.
     */
    public boolean insertOne(Zenken zenken) {

        // insert実行
        int rowNumber = zenkendao.insertOne(zenken);

        // 判定用変数
        boolean result = false;

        if (rowNumber > 0) {
            // insert成功
            result = true;
        }

        return result;
    }

    /**
     * 模試リスト取得用メソッド.
     */
    public List<Zenken> selectMany(String school, String grade) {

        //全件取得
        return zenkendao.selectMany(school, grade);
    }

    /**
     * 模試表示用成績の有無リスト取得用メソッド.
     */
    public List<RecordExistence> selectRecordExsitenceMany(String school, String grade) {

        // 対象の成績リストを取得
        List<SchoolRecord> recordList = numericDataDao.selectRecordMany(school, grade);

        // 対象の生徒のリストを取得
        List<Zenken> zenkenList = zenkendao.selectMany(school, grade);

        // 返却用の成績の有無のリストを作成
        List<RecordExistence> existenceList = new ArrayList<>();

        // zenkenListの人数分だけ返却リストに格納する
        for (int i = 0; i < zenkenList.size(); i++) {
            String id = zenkenList.get(i).getId();

            RecordExistence existence = grade.equals("中３") ? zenPro.recordExistence3rd(id, recordList) :
                                        grade.equals("中２") ? zenPro.recordExistence2nd(id, recordList) :
                                        zenPro.recordExistence1st(id, recordList);

            existenceList.add(existence);
        }

        return existenceList;

    }

    /**
     * 模試データ複数更新メソッド
     */
    public boolean updateMany(List<Zenken> list) {

        //判定用変数
        boolean result = false;

        // 複数更新
        List<Integer> rowNumbers = zenkendao.updateMany(list);

        for(Integer rowNumber : rowNumbers) {

            if (rowNumber > 0) {

                //update成功
                result = true;

            } else {
                // もし更新できない行があったら、falseを返す
                return false;
            }

        }

        return result;
    }

    /**
     * 模試データ１件削除メソッド
     */
    public boolean deleteOne(String id) {

        //１件削除
        int rowNumber = zenkendao.deleteOne(id);

        //判定用変数
        boolean result = false;

        if (rowNumber > 0) {
            //delete成功
            result = true;
        }
        return result;
    }

    /**
     * CSVファイル作成メソッド.
     */
    public String makeCsv(String school, String grade) throws SQLException {

        // ファイル名を決める
        LocalDate data = LocalDate.now();
        String schoolName = school.equals("橋戸校") ? "hashido" : school.equals("瀬谷校") ? "seya" : "yamato";
        int grade_num = grade.equals("中３") ? 3 : grade.equals("中２") ? 2 : 1;
        String fileName = DateTimeFormatter.ofPattern("yyyyMMdd").format(data) + schoolName + grade_num + ".csv";

        // CSVの１行目を作成
        int csvSchool = school.equals("橋戸校") ? 1 : school.equals("瀬谷校") ? 2 : 3;
        int csvGrade = grade.equals("中３") ? 23 : grade.equals("中２") ? 22 : 21;
        String firstLine = csvGrade + "," + 128 + "," + csvSchool + "," + "2a";

        List<Zenken> studentList = zenkendao.selectMany(school, grade);
        List<SchoolRecord> recordList = numericDataDao.selectRecordMany(school, grade);

        try {

            // ファイルの書き込み準備
            File file = new File(fileName);
            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);

            // 先頭行の書き込み
            bw.write(firstLine);
            bw.newLine();

            // １人分の処理
            for (int i = 0; i < studentList.size(); i++) {
                Zenken zen = studentList.get(i);
                String str = zen.getNumber() + ","
                        + zen.getGender() + ","
                        + zen.getEnrollment() + ","
                        + "\"" + zen.getStudentName() + "\"" + ","
                        + "\"" + zen.getNameRuby() + "\"" + ","
                        + zen.getPrefecture() + ","
                        + zen.getCity();

                bw.write(str);

                String record = grade.equals("中３") ?
                        zenPro.recordProcessing3rd(zen.getId(), recordList, zen.getRecordTerm()) : grade.equals("中２") ?
                        zenPro.recordProcessing2nd(zen.getId(), recordList, zen.getRecordTerm()) :
                        zenPro.recordProcessing1st(zen.getId(), recordList, zen.getRecordTerm());

                bw.write(String.valueOf(record));
                bw.newLine();
            }

            // 強制的に書き込み&ファイルクローズ
            bw.flush();
            bw.close();

        } catch (IOException e) {
            e.printStackTrace();
            throw new SQLException(e);
        }

        return fileName;
    }

    /**
     * サーバーに保存されたCSVを取得、byte列に変換.
     */
    public byte[] getFile(String fileName) throws IOException {

        // ファイルシステム（デフォルト）の取得
        FileSystem fs = FileSystems.getDefault();

        // ファイル取得
        Path p = fs.getPath(fileName);

        byte[] bytes = Files.readAllBytes(p);

        return bytes;
    }
}
