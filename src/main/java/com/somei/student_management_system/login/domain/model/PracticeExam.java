package com.somei.student_management_system.login.domain.model;

import lombok.Data;

@Data
public class PracticeExam {

    private String studentId;
    private int grade;
    private int examYear;
    private String monthName;
    private String englishScore;
    private String mathScore;
    private String japaneseScore;
    private String scienceScore;
    private String socialScore;
    private String sumThree;
    private String sumAll;
    private String devThree;
    private String devFive;
    private String englishDeviation;
    private String mathDeviation;
    private String japaneseDeviation;
    private String scienceDeviation;
    private String socialDeviation;
}
