package com.somei.student_management_system.login.domain.model;

import com.opencsv.bean.CsvBindByName;
import lombok.Data;

/**
 * 模擬試験のCSVデータを受け取るクラス
 */
@Data
public class ImportPracticeExam {

    @CsvBindByName(column = "生徒ID", required = true)
    private String studentId;

    @CsvBindByName(column = "学年", required = true)
    private int grade;

    @CsvBindByName(column = "実施年", required = true)
    private int examYear;

    @CsvBindByName(column = "実施月", required = true)
    private String monthName;

    @CsvBindByName(column = "英語_点数", required = true)
    private String englishScore;

    @CsvBindByName(column = "数学_点数", required = true)
    private String mathScore;

    @CsvBindByName(column = "国語_点数", required = true)
    private String japaneseScore;

    @CsvBindByName(column = "理科_点数", required = true)
    private String scienceScore;

    @CsvBindByName(column = "社会_点数", required = true)
    private String socialScore;

    @CsvBindByName(column = "3科目合計", required = true)
    private String sumThree;

    @CsvBindByName(column = "5科目合計", required = true)
    private String sumAll;

    @CsvBindByName(column = "3科目偏差値", required = true)
    private String devThree;

    @CsvBindByName(column = "5科目偏差値", required = true)
    private String devFive;

    @CsvBindByName(column = "英語_偏差値", required = true)
    private String englishDeviation;

    @CsvBindByName(column = "数学_偏差値", required = true)
    private String mathDeviation;

    @CsvBindByName(column = "国語_偏差値", required = true)
    private String japaneseDeviation;

    @CsvBindByName(column = "理科_偏差値", required = true)
    private String scienceDeviation;

    @CsvBindByName(column = "社会_偏差値", required = true)
    private String socialDeviation;

}
