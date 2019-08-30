package com.somei.student_management_system.login.domain.model;

import lombok.Data;

@Data
public class SchoolRecord {

    private int studentId;
    private int grade;
    private String termName;
    private int english;
    private int math;
    private int japanese;
    private int science;
    private int socialStudies;
    private int music;
    private int art;
    private int pe;
    private int techHome;
    private int sumFive;
    private int sumAll;

}
