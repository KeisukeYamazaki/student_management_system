package com.somei.student_management_system.login.domain.service;

import com.somei.student_management_system.login.domain.model.PracticeExam;
import com.somei.student_management_system.login.domain.model.RegularExam;
import com.somei.student_management_system.login.domain.model.SchoolRecord;
import com.somei.student_management_system.login.domain.repository.NumericDataDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NumericDataService {

    @Autowired
    NumericDataDao dao;

    /**
     * insert用メソッド.
     */


    /**
     * 1人分成績取得用メソッド.
     */
    public List<SchoolRecord> selectRecordOne(String studentId) {
        //全件取得
        return dao.selectRecordOne(studentId);
    }

    /**
     * １件以上更新用メソッド.
     */

    /**
     * １件削除用メソッド.
     */

    /**
     * 1人分模試取得用メソッド.
     */
    public List<PracticeExam> selectPracticeOne(String studentId) {
        //全件取得
        return dao.selectPracticeOne(studentId);
    }

    /**
     * 1人分定期試験取得用メソッド.
     */
    public List<RegularExam> selectRegularOne(String studentId) {
        //全件取得
        return dao.selectRegularOne(studentId);
    }
}
