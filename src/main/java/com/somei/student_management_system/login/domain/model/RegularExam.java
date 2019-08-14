package com.somei.student_management_system.login.domain.model;

import lombok.Data;

@Data
public class RegularExam {

    private String studentId;
    private int grade;
    private int examYear;
    private String examName;
    private String english;
    private String math;
    private String japanese;
    private String science;
    private String socialStudies;
    private String sumFive;
    private String music;
    private String art;
    private String pe;
    private String tech;
    private String home;

}
