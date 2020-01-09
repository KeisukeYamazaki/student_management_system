package com.somei.student_management_system.login.domain.repository;

import com.somei.student_management_system.login.domain.model.ImportPracticeExam;
import com.somei.student_management_system.login.domain.model.PracticeExam;
import com.somei.student_management_system.login.domain.model.RegularExam;
import com.somei.student_management_system.login.domain.model.SchoolRecord;
import com.somei.student_management_system.login.domain.model.SchoolRecordWithName;
import com.somei.student_management_system.login.domain.model.Student;
import org.springframework.dao.DataAccessException;

import java.util.List;

public interface NumericDataDao {

    // テーブルにデータを1件以上insert.
    public int insertSome(Student student) throws DataAccessException;

    // school_recordテーブルの1人分の成績データを取得.
    public List<SchoolRecord> selectRecordOne(String studentId) throws DataAccessException;

    // school_recordテーブルの複数人の成績データを取得.
    public List<SchoolRecord> selectRecordMany(String school, String grade) throws DataAccessException;

    // school_recordテーブルの複数人の成績データを取得.
    public List<SchoolRecordWithName> selectRecordManyForRegistry(String grade, String termName) throws DataAccessException;

    // school_recordテーブルの複数人の成績データを挿入.
    public List<Integer> insertRecordMany(List<SchoolRecordWithName> list) throws DataAccessException;

    // テーブルを１件以上更新.
    public int updateSome(Student student) throws DataAccessException;

    // テーブルを１件以上削除.
    public int deleteSome(String studentId) throws DataAccessException;

    // practice_examテーブルの1人分の模試データを取得.
    public List<PracticeExam> selectPracticeOne(String studentId) throws DataAccessException;

    // practice_examテーブルに複数人の模試データを挿入.
    public List<Integer> insertPracticeMany(List<? extends ImportPracticeExam> list) throws DataAccessException;

    // regular_examテーブルに複数人の模試データを挿入.
    public List<Integer> insertRegularMany(List<RegularExam> list) throws DataAccessException;

    // regular_examテーブルの1人分の定期試験データを取得.
    public List<RegularExam> selectRegularOne(String studentId) throws DataAccessException;



}

