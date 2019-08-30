package com.somei.student_management_system.login.domain.repository.jdbc;

import com.somei.student_management_system.login.domain.model.PracticeExam;
import com.somei.student_management_system.login.domain.model.RegularExam;
import com.somei.student_management_system.login.domain.model.SchoolRecord;
import com.somei.student_management_system.login.domain.model.Student;
import com.somei.student_management_system.login.domain.repository.NumericDataDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("NumericDataDaoJdbcImpl")
public class NumericDataDaoJdbcImpl implements NumericDataDao {

    @Autowired
    private JdbcTemplate jdbc;


    @Override
    public int insertSome(Student student) throws DataAccessException {
        return 0;
    }

    @Override
    public List<SchoolRecord> selectRecordOne(String studentId) throws DataAccessException {
        // school_recordテーブルのデータ１人分取得するSQL
        String sql = "SELECT student_id,"
                + " grade,"
                + " term_name,"
                + " english,"
                + " math,"
                + " japanese,"
                + " science,"
                + " social_studies,"
                + " music,"
                + " art,"
                + " pe,"
                + " tech_home,"
                + " sum_five,"
                + " sum_all"
                + " FROM school_record"
                + " JOIN record_group"
                + " ON school_record.record_id = record_group.id"
                + " WHERE student_id = ?"
                + " ORDER BY grade ASC, term_name ASC";

        // RowMapperの生成
        RowMapper<SchoolRecord> rowMapper = new BeanPropertyRowMapper<>(SchoolRecord.class);

        // SQL実行
        return jdbc.query(sql, rowMapper, studentId);
    }

    @Override
    public List<SchoolRecord> selectRecordMany(String school, String grade) throws DataAccessException {

        String startNum = grade.equals("中３") ? "2201" : grade.equals("中２") ? "2301" : "2401";
        String endNum = grade.equals("中３") ? "2299" : grade.equals("中２") ? "2399" : "2499";

        // school_recordテーブルのデータ複数人分取得するSQL
        String sql = "SELECT school_record.student_id,"
                + " school_record.grade,"
                + " term_name,"
                + " english,"
                + " math,"
                + " japanese,"
                + " science,"
                + " social_studies,"
                + " music,"
                + " art,"
                + " pe,"
                + " tech_home"
                + " FROM school_record"
                + " JOIN record_group"
                + " ON school_record.record_id = record_group.id"
                + " JOIN student"
                + " ON school_record.student_id = student.student_id"
                + " WHERE school = ? and school_record.student_id between ? and ?"
                + " ORDER BY term_name COLLATE \"C\"";

        // RowMapperの生成
        RowMapper<SchoolRecord> rowMapper = new BeanPropertyRowMapper<>(SchoolRecord.class);

        // SQL実行
        return jdbc.query(sql, rowMapper, school, startNum, endNum);
    }

    @Override
    public int updateSome(Student student) throws DataAccessException {
        return 0;
    }

    @Override
    public int deleteSome(String studentId) throws DataAccessException {
        return 0;
    }

    @Override
    public List<PracticeExam> selectPracticeOne(String studentId) throws DataAccessException {
        // practice_examテーブルのデータ１人分取得するSQL
        String sql = "SELECT student_id,"
                + " grade,"
                + " exam_year,"
                + " month_name,"
                + " english_score,"
                + " math_score,"
                + " japanese_score,"
                + " science_score,"
                + " social_score,"
                + " sum_three,"
                + " sum_all,"
                + " dev_three,"
                + " dev_five,"
                + " english_deviation,"
                + " math_deviation,"
                + " japanese_deviation,"
                + " science_deviation,"
                + " social_deviation"
                + " FROM practice_exam"
                + " JOIN practice_month"
                + " ON practice_exam.practice_month = practice_month.month"
                + " WHERE student_id = ?"
                + " ORDER BY grade ASC, practice_month ASC";

        // RowMapperの生成
        RowMapper<PracticeExam> rowMapper = new BeanPropertyRowMapper<>(PracticeExam.class);

        // SQL実行
        return jdbc.query(sql, rowMapper, studentId);
    }


    @Override
    public List<RegularExam> selectRegularOne(String studentId) throws DataAccessException {
        // regular_examテーブルのデータ１人分取得するSQL
        String sql = "SELECT student_id,"
                + " grade,"
                + " exam_year,"
                + " exam_name,"
                + " english,"
                + " math,"
                + " japanese,"
                + " science,"
                + " social_studies,"
                + " sum_five,"
                + " music,"
                + " art,"
                + " pe,"
                + " tech,"
                + " home"
                + " FROM regular_exam"
                + " JOIN regular_group"
                + " ON regular_exam.regular_id = regular_group.id"
                + " WHERE student_id = ?"
                + " ORDER BY grade ASC, regular_id ASC";

        // RowMapperの生成
        RowMapper<RegularExam> rowMapper = new BeanPropertyRowMapper<>(RegularExam.class);

        // SQL実行
        return jdbc.query(sql, rowMapper, studentId);
    }
}
