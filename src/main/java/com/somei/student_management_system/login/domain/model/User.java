package com.somei.student_management_system.login.domain.model;

import lombok.Data;

@Data
public class User {

    private String userId;
    private String userName;
    private String password;
    private String role;
    private String reallyPass;
}
