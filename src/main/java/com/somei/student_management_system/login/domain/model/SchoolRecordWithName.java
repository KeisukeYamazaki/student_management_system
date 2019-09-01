package com.somei.student_management_system.login.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// schoolRecordクラスとの違いは
// ・生徒名を含んでいる
// ・年度を含んでいる
// ・エクセルファイル読み込み用のクラス

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SchoolRecordWithName {

    private String studentName;
    private String studentId;
    private String grade;
    private String recordYear;
    private String termName;
    private String english;
    private String math;
    private String japanese;
    private String science;
    private String socialStudies;
    private String music;
    private String art;
    private String pe;
    private String techHome;
    private String sumFive;
    private String sumAll;
}
