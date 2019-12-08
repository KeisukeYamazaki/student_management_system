package com.somei.student_management_system.login.bean;

import com.somei.student_management_system.login.domain.model.RecordExistence;
import com.somei.student_management_system.login.domain.model.SchoolRecord;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class ZenkenProcessing {

    /**
     * 中３内申処理（CSV作成用）.
     */
    public String recordProcessing3rd(String studentId, List<SchoolRecord> recordList, String term) {

        List<String> term1 = Arrays.asList("中２", "３学期", "後期");
        List<String> term2 = Arrays.asList("中３", "１学期", "前期");
        List<String> term3 = Arrays.asList("中３", "２学期", "後期");
        List<List<String>> terms = Arrays.asList(term1, term2, term3);

        // 返却用
        StringBuilder records = new StringBuilder();

        // CSV作成処理
        recordProcessing(studentId, recordList, term, terms, records, 3);

        // 返却
        return new String(records);
    }

    /**
     * 中２内申処理（CSV作成用）.
     */
    public String recordProcessing2nd(String studentId, List<SchoolRecord> recordList, String term) {

        // 現在の月をintで取得(ex. 11月なら 11)
        LocalDate now = LocalDate.now();
        int month = now.getMonthValue();

        // 学期のリストを作っておく
        List<String> term1 = new ArrayList<>();
        List<String> term2 = new ArrayList<>();
        List<String> term3 = new ArrayList<>();

        if (month >= 4 && month <= 10) {
            // ４月〜10月の場合
            term1 = Arrays.asList("中１", "３学期", "後期");
            term2 = Arrays.asList("中２", "１学期");
            term3 = Arrays.asList("中２", "２学期", "前期");
        } else {
            // 11月〜3月の場合
            term1 = Arrays.asList("中２", "１学期");
            term2 = Arrays.asList("中２", "２学期", "前期");
            term3 = Arrays.asList("中２", "３学期", "後期");
        }

        List<List<String>> terms = Arrays.asList(term1, term2, term3);

        // 返却用
        StringBuilder records = new StringBuilder();

        // CSV作成処理
        recordProcessing(studentId, recordList, term, terms, records, 2);

        return new String(records);
    }

    /**
     * 中１内申処理（CSV作成用）.
     */
    public String recordProcessing1st(String studentId, List<SchoolRecord> recordList, String term) {

        List<String> term1 = Arrays.asList("中１", "１学期");
        List<String> term2 = Arrays.asList("中１", "２学期", "前期");
        List<String> term3 = Arrays.asList("中１", "３学期", "後期");

        List<List<String>> terms = Arrays.asList(term1, term2, term3);

        // 返却用
        StringBuilder records = new StringBuilder();
        int count = records.length();

        // CSV作成処理
        recordProcessing(studentId, recordList, term, terms, records, 1);

        return new String(records);
    }

    /**
     * 中３内申処理（画面表示用）.
     */
    public RecordExistence recordExistence3rd(String studentId, List<SchoolRecord> recordList) {

        // 返却用
        RecordExistence existence = new RecordExistence();

        // Idをセット
        existence.setStudentId(studentId);

        // １つ目の成績をセットする
        for (int i = 0; i < recordList.size(); i++) {

            // リストから i 番目の成績データを取り出す
            SchoolRecord record = recordList.get(i);
            // Idを文字列に変換して変数に代入
            String strId = String.valueOf(record.getStudentId());

            // Id・学年・学期が合致した成績データを見つけたら、recordsに代入
            if (strId.equals(studentId) && record.getGrade().equals("中２") && (record.getTermName().equals("３学期") || record.getTermName().equals("後期"))) {

                // 対象の成績があれば true をセット
                existence.setFirstRecord(true);

            }
        }

        // ２つ目の成績をセットする
        for (int j = 0; j < recordList.size(); j++) {

            SchoolRecord record = recordList.get(j);
            String strId = String.valueOf(record.getStudentId());

            if (strId.equals(studentId) && record.getGrade().equals("中３") && (record.getTermName().equals("１学期") || record.getTermName().equals("前期"))) {

                existence.setSecondRecord(true);

            }
        }

        // ３つめの成績をセットする
        for (int k = 0; k < recordList.size(); k++) {

            SchoolRecord record = recordList.get(k);
            String strId = String.valueOf(record.getStudentId());

            if (strId.equals(studentId) && record.getGrade().equals("中３") && (record.getTermName().equals("２学期") || record.getTermName().equals("後期"))) {

                existence.setThirdRecord(true);

            }
        }

        return existence;
    }

    /**
     * 中２内申処理（画面表示用）.
     */
    public RecordExistence recordExistence2nd(String studentId, List<SchoolRecord> recordList) {

        // 返却用
        RecordExistence existence = new RecordExistence();

        // Idをセット
        existence.setStudentId(studentId);

        // １つ目の成績をセットする
        for (int i = 0; i < recordList.size(); i++) {

            // リストから i 番目の成績データを取り出す
            SchoolRecord record = recordList.get(i);
            // Idを文字列に変換して変数に代入
            String strId = String.valueOf(record.getStudentId());

            // Id・学年・学期が合致した成績データを見つけたら、recordsに代入
            if (strId.equals(studentId) && record.getGrade().equals("中１") && (record.getTermName().equals("３学期") || record.getTermName().equals("後期"))) {
                // 対象の成績があれば true をセット
                existence.setFirstRecord(true);
            }
        }
        // ２つ目の成績をセットする
        for (int j = 0; j < recordList.size(); j++) {

            SchoolRecord record = recordList.get(j);
            String strId = String.valueOf(record.getStudentId());

            if (strId.equals(studentId) && record.getGrade().equals("中２") && (record.getTermName().equals("１学期"))) {
                existence.setSecondRecord(true);
            }
        }
        // ３つめの成績をセットする
        for (int k = 0; k < recordList.size(); k++) {

            SchoolRecord record = recordList.get(k);
            String strId = String.valueOf(record.getStudentId());

            if (strId.equals(studentId) && record.getGrade().equals("中２") && (record.getTermName().equals("２学期") || record.getTermName().equals("前期"))) {
                existence.setThirdRecord(true);
            }
        }
        return existence;
    }

    /**
     * 中１内申処理（画面表示用）.
     */
    public RecordExistence recordExistence1st(String studentId, List<SchoolRecord> recordList) {

        // 返却用
        RecordExistence existence = new RecordExistence();

        // Idをセット
        existence.setStudentId(studentId);

        // １つ目の成績をセットする
        for (int i = 0; i < recordList.size(); i++) {

            // リストから i 番目の成績データを取り出す
            SchoolRecord record = recordList.get(i);
            // Idを文字列に変換して変数に代入
            String strId = String.valueOf(record.getStudentId());

            // Id・学年・学期が合致した成績データを見つけたら、recordsに代入
            if (strId.equals(studentId) && record.getGrade().equals("中１") && (record.getTermName().equals("１学期"))) {
                // 対象の成績があれば true をセット
                existence.setFirstRecord(true);
            }
        }
        // ２つ目の成績をセットする
        for (int j = 0; j < recordList.size(); j++) {

            SchoolRecord record = recordList.get(j);
            String strId = String.valueOf(record.getStudentId());

            if (strId.equals(studentId) && record.getGrade().equals("中１") && (record.getTermName().equals("２学期") || record.getTermName().equals("前期"))) {
                existence.setSecondRecord(true);
            }
        }

        // ３つめの成績をセットする
        for (int k = 0; k < recordList.size(); k++) {

            SchoolRecord record = recordList.get(k);
            String strId = String.valueOf(record.getStudentId());

            if (strId.equals(studentId) && record.getGrade().equals("中１") && (record.getTermName().equals("３学期") || record.getTermName().equals("後期"))) {
                existence.setThirdRecord(true);
            }
        }

        return existence;
    }

    private void appendRecords(StringBuilder records, SchoolRecord record, String term) {

        records.append("," + term + ",");
        records.append(record.getEnglish() + ",");
        records.append(record.getMath() + ",");
        records.append(record.getJapanese() + ",");
        records.append(record.getScience() + ",");
        records.append(record.getSocialStudies() + ",");
        records.append(record.getMusic() + ",");
        records.append(record.getArt() + ",");
        records.append(record.getPe() + ",");
        records.append(record.getTechHome());
    }

    private void recordProcessing(String studentId, List<SchoolRecord> recordList,
                                  String term, List<List<String>> terms, StringBuilder records, int grade) {

        // 現在の月をintで取得(ex. 11月なら 11)
        LocalDate now = LocalDate.now();
        int month = now.getMonthValue();

        int count = records.length();

        // １つ目の成績処理
        for (int i = 0; i < recordList.size(); i++) {

            // リストから i 番目の成績データを取り出す
            SchoolRecord record = recordList.get(i);
            // Idを文字列に変換して変数に代入
            String strId = String.valueOf(record.getStudentId());

            if ((grade == 2 && month <= 3 || month >= 11) || grade == 1) {
                // Id・学年・学期が合致した成績データを見つけたら、recordsに代入
                if (strId.equals(studentId) &&
                        record.getGrade().equals(terms.get(0).get(0)) &&
                        (record.getTermName().equals(terms.get(0).get(1)))) {
                    //recordsに代入
                    appendRecords(records, record, term);
                }
            } else {
                if (strId.equals(studentId) &&
                        record.getGrade().equals(terms.get(0).get(0)) &&
                        (record.getTermName().equals(terms.get(0).get(1)) ||
                                record.getTermName().equals(terms.get(0).get(2)))) {
                    //recordsに代入
                    appendRecords(records, record, term);
                }
            }
        }
        // 増えていない場合（対象の成績データが無かった場合）
        if (count == records.length()) {
            records.append(",,,,,,,,,,");
        }
        count = records.length();

        // ２つ目の成績処理
        for (int j = 0; j < recordList.size(); j++) {
            SchoolRecord record = recordList.get(j);
            String strId = String.valueOf(record.getStudentId());

            if (grade == 2 && month >= 4 && month <= 10) {
                // 中２の４〜10月の場合
                if (strId.equals(studentId) &&
                        record.getGrade().equals(terms.get(1).get(0)) &&
                        (record.getTermName().equals(terms.get(1).get(1)))) {
                    //recordsに代入
                    appendRecords(records, record, term);
                }
            } else {
                if (strId.equals(studentId) &&
                        record.getGrade().equals(terms.get(1).get(0)) &&
                        (record.getTermName().equals(terms.get(1).get(1)) ||
                                record.getTermName().equals(terms.get(1).get(2)))) {
                    //recordsに代入
                    appendRecords(records, record, term);
                }
            }
        }
        // 増えていない場合（対象の成績データが無かった場合）
        if (count == records.length()) {
            records.append(",,,,,,,,,,");
        }

        count = records.length();

        // ３つ目の成績処理
        for (int k = 0; k < recordList.size(); k++) {

            SchoolRecord record = recordList.get(k);
            String strId = String.valueOf(record.getStudentId());

            if (strId.equals(studentId) &&
                    record.getGrade().equals(terms.get(2).get(0)) &&
                    (record.getTermName().equals(terms.get(2).get(1)) ||
                            record.getTermName().equals(terms.get(2).get(2)))) {
                //recordsに代入
                appendRecords(records, record, term);
            }
        }
        // 増えていない場合（対象の成績データが無かった場合）
        if (count == records.length()) {
            records.append(",,,,,,,,,,");
        }
    }
}
