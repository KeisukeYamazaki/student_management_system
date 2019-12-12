package com.somei.student_management_system.login.domain.model;

import com.opencsv.bean.CsvBindByPosition;
import lombok.Data;

@Data
public class IkushinPracticeExam extends ImportPracticeExam {

    private String studentId;

    @CsvBindByPosition(position = 7)
    private String studentName;

    @CsvBindByPosition(position = 3)
    private String grade;

    @CsvBindByPosition(position = 0)
    private String yearMonth;

    private String examYear;

    private String monthName;

    @CsvBindByPosition(position = 10)
    private String englishScore;

    @CsvBindByPosition(position = 9)
    private String mathScore;

    @CsvBindByPosition(position = 8)
    private String japaneseScore;

    @CsvBindByPosition(position = 11)
    private String scienceScore;

    @CsvBindByPosition(position = 12)
    private String socialScore;

    @CsvBindByPosition(position = 13)
    private String sumThree;

    @CsvBindByPosition(position = 15)
    private String sumAll;

    @CsvBindByPosition(position = 21)
    private String devThree;

    @CsvBindByPosition(position = 23)
    private String devFive;

    @CsvBindByPosition(position = 18)
    private String englishDeviation;

    @CsvBindByPosition(position = 17)
    private String mathDeviation;

    @CsvBindByPosition(position = 16)
    private String japaneseDeviation;

    @CsvBindByPosition(position = 19)
    private String scienceDeviation;

    @CsvBindByPosition(position = 20)
    private String socialDeviation;
}
