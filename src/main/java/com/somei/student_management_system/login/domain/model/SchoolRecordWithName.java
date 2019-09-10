package com.somei.student_management_system.login.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.Pattern;

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

    @Pattern(regexp = "[0-9]{4}")
    private String recordYear;

    private String termName;

    @Pattern(regexp = "[1-5]")
    private String english;

    @Pattern(regexp = "[1-5]")
    private String math;

    @Pattern(regexp = "[1-5]")
    private String japanese;

    @Pattern(regexp = "[1-5]")
    private String science;

    @Pattern(regexp = "[1-5]")
    private String socialStudies;

    @Pattern(regexp = "[1-5]")
    private String music;

    @Pattern(regexp = "[1-5]")
    private String art;

    @Pattern(regexp = "[1-5]")
    private String pe;

    @Pattern(regexp = "[1-5]")
    private String techHome;

    @Range(min = 5, max = 25)
    private String sumFive;

    @Range(min = 9, max = 45)
    private String sumAll;
}
