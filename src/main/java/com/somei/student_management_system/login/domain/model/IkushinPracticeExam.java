package com.somei.student_management_system.login.domain.model;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvBindByPosition;
import lombok.Data;

@Data
public class IkushinPracticeExam extends ImportPracticeExam {

    private String studentId;

    @CsvBindByPosition(position = 7, required = true)
    private String studentName;

    @CsvBindByPosition(position = 3, required = true)
    private String grade;

    @CsvBindByPosition(position = 0, required = true)
    private String examYear;

    @CsvBindByPosition(position = 0, required = true)
    private String monthName;

    @CsvBindByPosition(position = 10, required = true)
    private String englishScore;

    @CsvBindByPosition(position = 9, required = true)
    private String mathScore;

    @CsvBindByPosition(position = 8, required = true)
    private String japaneseScore;

    @CsvBindByPosition(position = 11, required = true)
    private String scienceScore;

    @CsvBindByPosition(position = 12, required = true)
    private String socialScore;

    @CsvBindByPosition(position = 13, required = true)
    private String sumThree;

    @CsvBindByPosition(position = 15, required = true)
    private String sumAll;

    @CsvBindByPosition(position = 21, required = true)
    private String devThree;

    @CsvBindByPosition(position = 23, required = true)
    private String devFive;

    @CsvBindByPosition(position = 18, required = true)
    private String englishDeviation;

    @CsvBindByPosition(position = 17, required = true)
    private String mathDeviation;

    @CsvBindByPosition(position = 16, required = true)
    private String japaneseDeviation;

    @CsvBindByPosition(position = 19, required = true)
    private String scienceDeviation;

    @CsvBindByPosition(position = 20, required = true)
    private String socialDeviation;
}
