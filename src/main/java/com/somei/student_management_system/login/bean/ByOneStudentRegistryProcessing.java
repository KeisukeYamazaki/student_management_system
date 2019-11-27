package com.somei.student_management_system.login.bean;

import com.somei.student_management_system.login.domain.model.SchoolRecord;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ByOneStudentRegistryProcessing {

    public SchoolRecord recordClassification(List<SchoolRecord> list, String grade, String term) {

        // 新しいSchoolRecordインスタンスを作成
        SchoolRecord record = new SchoolRecord();
        // 引数のlistをループで回し、grade, term に合致すればrecordを更新する。
        for (SchoolRecord listRecord : list) {
            if (listRecord.getGrade().equals(grade) && listRecord.getTermName().equals(term)) {
                record = listRecord;
            }
        }
        // recordを返す
        return record;
    }
}
