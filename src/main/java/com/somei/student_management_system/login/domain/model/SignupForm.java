package com.somei.student_management_system.login.domain.model;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.util.Date;

@Data
public class SignupForm {

    @NotBlank
    private String studentId;

    @NotBlank
    private String studentName;

    @NotBlank
    @Pattern(regexp = "^[ァ-ヶー 　]*$")
    private String nameRuby;

    @NotBlank
    private String grade;

    @NotBlank
    private String school;

    private String homeRoom;

    private String localSchool;

    private String entryTime;

    @DateTimeFormat(pattern = "YYYY/M/d")
    private Date birthday;

    private Integer birthdayYear;

    private Integer birthdayMonth;

    private Integer birthdayDay;

    private String club;

    private String parents;

    private String siblings;  // 兄弟姉妹

    private String address;

    private String info;  // 共有情報
}
