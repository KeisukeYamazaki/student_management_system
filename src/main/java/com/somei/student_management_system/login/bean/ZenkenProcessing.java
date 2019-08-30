package com.somei.student_management_system.login.bean;

import com.somei.student_management_system.login.domain.model.RecordExistence;
import com.somei.student_management_system.login.domain.model.SchoolRecord;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ZenkenProcessing {

    /**
     * 中３内申処理（CSV作成用）.
     */
    public String recordProcessing3rd(String studentId, List<SchoolRecord> recordList, String term) {

        // 返却用
        StringBuilder records = new StringBuilder();
        int count = records.length();

        for (int i = 0; i < recordList.size(); i++) {

            // リストから i 番目の成績データを取り出す
            SchoolRecord record = recordList.get(i);
            // Idを文字列に変換して変数に代入
            String strId = String.valueOf(record.getStudentId());

            // Id・学年・学期が合致した成績データを見つけたら、recordsに代入
            if (strId.equals(studentId) && record.getGrade() == 2 && (record.getTermName().equals("３学期") || record.getTermName().equals("後期"))) {

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
        }

        // 増えていない場合（対象の成績データが無かった場合）
        if (count == records.length()) {
            records.append(",,,,,,,,,,");
        }

        count = records.length();

        for (int j = 0; j < recordList.size(); j++) {

            SchoolRecord record = recordList.get(j);
            String strId = String.valueOf(record.getStudentId());

            if (strId.equals(studentId) && record.getGrade() == 3 && (record.getTermName().equals("１学期") || record.getTermName().equals("前期"))) {

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
        }

        // 増えていない場合（対象の成績データが無かった場合）
        if (count == records.length()) {
            records.append(",,,,,,,,,,");
        }

        count = records.length();

        for (int k = 0; k < recordList.size(); k++) {

            SchoolRecord record = recordList.get(k);
            String strId = String.valueOf(record.getStudentId());

            if (strId.equals(studentId) && record.getGrade() == 3 && (record.getTermName().equals("２学期") || record.getTermName().equals("後期"))) {

                records.append("," + term  + ",");
                records.append(record.getEnglish() + ",");
                records.append(record.getMath() + ",");
                records.append(record.getJapanese() + ",");
                records.append(record.getScience() + ",");
                records.append(record.getSocialStudies() + ",");
                records.append(record.getMusic() + ",");
                records.append(record.getArt() + ",");
                records.append(record.getPe() + ",");
                records.append(record.getTechHome() + ",");

            }
        }

        // 増えていない場合（対象の成績データが無かった場合）
        if (count == records.length()) {
            records.append(",,,,,,,,,,");
        }

        return new String(records);
    }

    /**
     * 中２内申処理（CSV作成用）.
     */
    public String recordProcessing2nd(String studentId, List<SchoolRecord> recordList, String term) {

        // 返却用
        StringBuilder records = new StringBuilder();
        int count = records.length();

        for (int i = 0; i < recordList.size(); i++) {

            SchoolRecord record = recordList.get(i);
            String strId = String.valueOf(record.getStudentId());

            if (strId.equals(studentId) && record.getGrade() == 1 && (record.getTermName().equals("３学期") || record.getTermName().equals("後期"))) {

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
        }

        // 増えていない場合（対象の成績データが無かった場合）
        if (count == records.length()) {
            records.append(",,,,,,,,,,");
        }

        count = records.length();

        for (int j = 0; j < recordList.size(); j++) {

            SchoolRecord record = recordList.get(j);
            String strId = String.valueOf(record.getStudentId());

            if (strId.equals(studentId) && record.getGrade() == 2 && (record.getTermName().equals("１学期"))) {

                records.append("," + term  + ",");
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
        }

        // 増えていない場合（対象の成績データが無かった場合）
        if (count == records.length()) {
            records.append(",,,,,,,,,,");
        }

        count = records.length();

        for (int k = 0; k < recordList.size(); k++) {

            SchoolRecord record = recordList.get(k);
            String strId = String.valueOf(record.getStudentId());

            if (strId.equals(studentId) && record.getGrade() == 2 && (record.getTermName().equals("２学期") || record.getTermName().equals("前期"))) {

                records.append("," + term  + ",");
                records.append(record.getEnglish() + ",");
                records.append(record.getMath() + ",");
                records.append(record.getJapanese() + ",");
                records.append(record.getScience() + ",");
                records.append(record.getSocialStudies() + ",");
                records.append(record.getMusic() + ",");
                records.append(record.getArt() + ",");
                records.append(record.getPe() + ",");
                records.append(record.getTechHome() + ",");

            }
        }

        // 増えていない場合（対象の成績データが無かった場合）
        if (count == records.length()) {
            records.append(",,,,,,,,,,");
        }

        return new String(records);
    }

    /**
     * 中１内申処理（CSV作成用）.
     */
    public String recordProcessing1st(String studentId, List<SchoolRecord> recordList, String term) {

        // 返却用
        StringBuilder records = new StringBuilder();
        int count = records.length();

        for (int i = 0; i < recordList.size(); i++) {

            SchoolRecord record = recordList.get(i);
            String strId = String.valueOf(record.getStudentId());

            if (strId.equals(studentId) && record.getGrade() == 1 && (record.getTermName().equals("１学期"))) {

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
        }

        // 増えていない場合（対象の成績データが無かった場合）
        if (count == records.length()) {
            records.append(",,,,,,,,,,");
        }

        count = records.length();

        for (int j = 0; j < recordList.size(); j++) {

            SchoolRecord record = recordList.get(j);
            String strId = String.valueOf(record.getStudentId());

            if (strId.equals(studentId) && record.getGrade() == 1 && (record.getTermName().equals("２学期") || record.getTermName().equals("前期"))) {

                records.append("," + term  + ",");
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
        }

        // 増えていない場合（対象の成績データが無かった場合）
        if (count == records.length()) {
            records.append(",,,,,,,,,,");
        }

        count = records.length();

        for (int k = 0; k < recordList.size(); k++) {

            SchoolRecord record = recordList.get(k);
            String strId = String.valueOf(record.getStudentId());

            if (strId.equals(studentId) && record.getGrade() == 1 && (record.getTermName().equals("３学期") || record.getTermName().equals("後期"))) {

                records.append("," + term  + ",");
                records.append(record.getEnglish() + ",");
                records.append(record.getMath() + ",");
                records.append(record.getJapanese() + ",");
                records.append(record.getScience() + ",");
                records.append(record.getSocialStudies() + ",");
                records.append(record.getMusic() + ",");
                records.append(record.getArt() + ",");
                records.append(record.getPe() + ",");
                records.append(record.getTechHome() + ",");

            }
        }

        // 増えていない場合（対象の成績データが無かった場合）
        if (count == records.length()) {
            records.append(",,,,,,,,,,");
        }

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
            if (strId.equals(studentId) && record.getGrade() == 2 && (record.getTermName().equals("３学期") || record.getTermName().equals("後期"))) {

                // 対象の成績があれば true をセット
                existence.setFirstRecord(true);

            }
        }

        // ２つ目の成績をセットする
        for (int j = 0; j < recordList.size(); j++) {

            SchoolRecord record = recordList.get(j);
            String strId = String.valueOf(record.getStudentId());

            if (strId.equals(studentId) && record.getGrade() == 3 && (record.getTermName().equals("１学期") || record.getTermName().equals("前期"))) {

                existence.setSecondRecord(true);

            }
        }

        // ３つめの成績をセットする
        for (int k = 0; k < recordList.size(); k++) {

            SchoolRecord record = recordList.get(k);
            String strId = String.valueOf(record.getStudentId());

            if (strId.equals(studentId) && record.getGrade() == 3 && (record.getTermName().equals("２学期") || record.getTermName().equals("後期"))) {

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
            if (strId.equals(studentId) && record.getGrade() == 1 && (record.getTermName().equals("３学期") || record.getTermName().equals("後期"))) {

                // 対象の成績があれば true をセット
                existence.setFirstRecord(true);

            }
        }

        // ２つ目の成績をセットする
        for (int j = 0; j < recordList.size(); j++) {

            SchoolRecord record = recordList.get(j);
            String strId = String.valueOf(record.getStudentId());

            if (strId.equals(studentId) && record.getGrade() == 2 && (record.getTermName().equals("１学期"))) {

                existence.setSecondRecord(true);

            }
        }

        // ３つめの成績をセットする
        for (int k = 0; k < recordList.size(); k++) {

            SchoolRecord record = recordList.get(k);
            String strId = String.valueOf(record.getStudentId());

            if (strId.equals(studentId) && record.getGrade() == 2 && (record.getTermName().equals("２学期") || record.getTermName().equals("前期"))) {

                existence.setThirdRecord(true);

            }
        }

        return existence;
    }

    /**
     * 中２内申処理（画面表示用）.
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
            if (strId.equals(studentId) && record.getGrade() == 1 && (record.getTermName().equals("１学期"))) {

                // 対象の成績があれば true をセット
                existence.setFirstRecord(true);

            }
        }

        // ２つ目の成績をセットする
        for (int j = 0; j < recordList.size(); j++) {

            SchoolRecord record = recordList.get(j);
            String strId = String.valueOf(record.getStudentId());

            if (strId.equals(studentId) && record.getGrade() == 1 && (record.getTermName().equals("２学期") || record.getTermName().equals("前期"))) {

                existence.setSecondRecord(true);

            }
        }

        // ３つめの成績をセットする
        for (int k = 0; k < recordList.size(); k++) {

            SchoolRecord record = recordList.get(k);
            String strId = String.valueOf(record.getStudentId());

            if (strId.equals(studentId) && record.getGrade() == 1 && (record.getTermName().equals("３学期") || record.getTermName().equals("後期"))) {

                existence.setThirdRecord(true);

            }
        }

        return existence;
    }
}
