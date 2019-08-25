package com.somei.student_management_system.login.domain.model;

import lombok.Data;

@Data
public class RecordExistence {

    private String studentId;
    private boolean firstRecord;
    private boolean secondRecord;
    private boolean thirdRecord;

}
