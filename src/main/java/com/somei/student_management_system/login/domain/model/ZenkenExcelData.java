package com.somei.student_management_system.login.domain.model;

import lombok.Data;

@Data
public class ZenkenExcelData extends ImportPracticeExam {

    private String firstChoice;
    private String firstSituation;
    private String secondChoice;
    private String secondSituation;
    private String thirdChoice;
    private String thirdSituation;
    private String publicSchool1;
    private String publicSchool2;
    private String publicSchool3;
    private String privateSchool1;
    private String privateSchool2;
    private String privateSchool3;
}
