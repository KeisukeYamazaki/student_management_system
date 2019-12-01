package com.somei.student_management_system.login.domain.model;

import lombok.Data;

@Data
public class SchoolRecord {

    private String studentId;
    private String grade;
    private Integer recordYear;
    private String termName;
    private Integer english;
    private Integer math;
    private Integer japanese;
    private Integer science;
    private Integer socialStudies;
    private Integer music;
    private Integer art;
    private Integer pe;
    private Integer techHome;
    private Integer sumFive;
    private Integer sumAll;

}
