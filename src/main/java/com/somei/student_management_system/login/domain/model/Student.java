package com.somei.student_management_system.login.domain.model;

import lombok.Data;

import java.util.Date;

@Data
public class Student {

    private String studentId;
    private String studentName;
    private String nameRuby;
    private String grade;
    private String school;
    private String homeRoom;
    private String localSchool;
    private String entryTime;
    private Date birthday;
    private String club;
    private String parents;
    private String siblings;  // 兄弟姉妹
    private String address;
    private String info;  // 共有情報
}
