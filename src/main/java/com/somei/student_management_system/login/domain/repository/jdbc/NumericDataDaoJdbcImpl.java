package com.somei.student_management_system.login.domain.repository.jdbc;

import com.somei.student_management_system.login.domain.model.ImportPracticeExam;
import com.somei.student_management_system.login.domain.model.PracticeExam;
import com.somei.student_management_system.login.domain.model.RegularExam;
import com.somei.student_management_system.login.domain.model.SchoolRecord;
import com.somei.student_management_system.login.domain.model.SchoolRecordWithName;
import com.somei.student_management_system.login.domain.model.Student;
import com.somei.student_management_system.login.domain.repository.NumericDataDao;
import com.somei.student_management_system.login.domain.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository("NumericDataDaoJdbcImpl")
public class NumericDataDaoJdbcImpl implements NumericDataDao {

    @Autowired
    private JdbcTemplate jdbc;

    @Autowired
    StudentService studentService;


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
                + " WHERE student_id = ? AND char_length(school_record.student_id) = 4"
                + " ORDER BY grade ASC, term_name ASC";

        // RowMapperの生成
        RowMapper<SchoolRecord> rowMapper = new BeanPropertyRowMapper<>(SchoolRecord.class);

        // SQL実行
        return jdbc.query(sql, rowMapper, studentId);
    }

    /**
     * 複数の成績を取得.
     *
     * @param school 対象の校舎
     * @param grade  対象の学年
     * @return 実行結果のリスト
     * @throws
     */
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
                + " WHERE school = ? AND char_length(school_record.student_id) = 4 AND school_record.student_id between ? and ?"
                + " ORDER BY term_name COLLATE \"C\"";

        // RowMapperの生成
        RowMapper<SchoolRecord> rowMapper = new BeanPropertyRowMapper<>(SchoolRecord.class);

        // SQL実行
        return jdbc.query(sql, rowMapper, school, startNum, endNum);
    }

    /**
     * 複数の成績を取得（成績登録用）
     *
     * @param school   対象の校舎
     * @param grade    対象の学年
     * @param termName 対象の学期
     * @return 実行結果のリスト
     * @throws
     */
    @Override
    public List<SchoolRecordWithName> selectRecordManyForRegistry(String grade, String termName) throws DataAccessException {

        String startNum = grade.equals("中３") ? "2201" : grade.equals("中２") ? "2301" : "2401";
        String endNum = grade.equals("中３") ? "2299" : grade.equals("中２") ? "2399" : "2499";

        // school_recordテーブルのデータ複数人分取得するSQL
        String sql = null;

        // termName によって条件分岐
        if (termName.equals("１学期・前期")) {

            sql ="SELECT student.student_name,"
                    + " student.student_id,"
                    + " student.grade,"
                    + " record_year,"
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
                    + " FROM student"
                    + " LEFT OUTER JOIN school_record"
                    + " ON student.student_id = school_record.student_id"
                    + " AND student.grade = school_record.grade"
                    + " AND student.grade = ?"
                    + " AND (school_record.record_id = 1 OR school_record.record_id = 2)"
                    + " LEFT OUTER JOIN record_group"
                    + " ON school_record.record_id = record_group.id"
                    + " WHERE student.student_id BETWEEN ? AND ?"
                    + " AND char_length(student.student_id) = 4"
                    + " ORDER BY student.student_id";

        } else if (termName.equals("２学期")) {

            sql ="SELECT student.student_name,"
                    + " student.student_id,"
                    + " student.grade,"
                    + " record_year,"
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
                    + " FROM student"
                    + " LEFT OUTER JOIN school_record"
                    + " ON student.student_id = school_record.student_id"
                    + " AND student.grade = school_record.grade"
                    + " AND student.grade = ?"
                    + " AND school_record.record_id = 3"
                    + " LEFT OUTER JOIN record_group"
                    + " ON school_record.record_id = record_group.id"
                    + " WHERE student.student_id BETWEEN ? AND ?"
                    + " AND char_length(student.student_id) = 4"
                    + " ORDER BY student.student_id";

        } else {

            sql ="SELECT student.student_name,"
                    + " student.student_id,"
                    + " student.grade,"
                    + " record_year,"
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
                    + " FROM student"
                    + " LEFT OUTER JOIN school_record"
                    + " ON student.student_id = school_record.student_id"
                    + " AND student.grade = school_record.grade"
                    + " AND student.grade = ?"
                    + " AND (school_record.record_id = 4 OR school_record.record_id = 5)"
                    + " LEFT OUTER JOIN record_group"
                    + " ON school_record.record_id = record_group.id"
                    + " WHERE student.student_id BETWEEN ? AND ?"
                    + " AND char_length(student.student_id) = 4"
                    + " ORDER BY student.student_id";
        }

        // RowMapperの生成
        RowMapper<SchoolRecordWithName> rowMapper = new BeanPropertyRowMapper<>(SchoolRecordWithName.class);

        // SQL実行
        return jdbc.query(sql, rowMapper, grade, startNum, endNum);
    }

    /**
     * 複数の成績を挿入.
     *
     * @param list 成績のリスト
     * @return 実行結果のリスト
     * @throws
     */
    @Override
    public List<Integer> insertRecordMany(List<SchoolRecordWithName> list) throws DataAccessException {

        // 結果返却用のリストを生成
        List<Integer> resultList = new ArrayList<>();

        // リストの数の分のinsertを行う
        for (int i = 0; i < list.size(); i++) {

            SchoolRecordWithName srwn = list.get(i);

            if(srwn.getSumFive() == null || srwn.getSumFive().equals("")) {
                int sumFive = Integer.parseInt(srwn.getEnglish())
                        + Integer.parseInt(srwn.getMath())
                        + Integer.parseInt(srwn.getJapanese())
                        + Integer.parseInt(srwn.getScience())
                        + Integer.parseInt(srwn.getSocialStudies());
                srwn.setSumFive(String.valueOf(sumFive));
            }

            if(srwn.getSumAll() == null || srwn.getSumAll().equals("")) {
                int sumAll = Integer.parseInt(srwn.getEnglish())
                        + Integer.parseInt(srwn.getMath())
                        + Integer.parseInt(srwn.getJapanese())
                        + Integer.parseInt(srwn.getScience())
                        + Integer.parseInt(srwn.getSocialStudies())
                        + Integer.parseInt(srwn.getMusic())
                        + Integer.parseInt(srwn.getArt())
                        + Integer.parseInt(srwn.getPe())
                        + Integer.parseInt(srwn.getTechHome());
                srwn.setSumAll(String.valueOf(sumAll));
            }

            // 学期名をを数値に変換
            int recordId = 0;
            switch (srwn.getTermName()) {
                case "１学期":
                    recordId = 1;
                    break;
                case "前期":
                    recordId = 2;
                    break;
                case "２学期":
                    recordId = 3;
                    break;
                case "後期":
                    recordId = 4;
                    break;
                case "３学期":
                    recordId = 5;
                    break;
            }

            // クエリーを実行
            int rowNumber = jdbc.update("INSERT INTO school_record(student_id,"
                            + " grade,"
                            + " record_year,"
                            + " record_id,"
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
                            + " sum_all)"
                            + " VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)"
                            + " ON CONFLICT ON CONSTRAINT record_const"
                            + " DO UPDATE SET grade = ?,"
                            + " english = ?,"
                            + " math = ?,"
                            + " japanese = ?,"
                            + " science = ?,"
                            + " social_studies = ?,"
                            + " music = ?,"
                            + " art = ?,"
                            + " pe = ?,"
                            + " tech_home = ?,"
                            + " sum_five = ?,"
                            + " sum_all = ?;"
                    , srwn.getStudentId()
                    , srwn.getGrade()
                    , Integer.parseInt(srwn.getRecordYear())
                    , recordId
                    , Integer.parseInt(srwn.getEnglish())
                    , Integer.parseInt(srwn.getMath())
                    , Integer.parseInt(srwn.getJapanese())
                    , Integer.parseInt(srwn.getScience())
                    , Integer.parseInt(srwn.getSocialStudies())
                    , Integer.parseInt(srwn.getMusic())
                    , Integer.parseInt(srwn.getArt())
                    , Integer.parseInt(srwn.getPe())
                    , Integer.parseInt(srwn.getTechHome())
                    , Integer.parseInt(srwn.getSumFive())
                    , Integer.parseInt(srwn.getSumAll())
                    , srwn.getGrade()
                    , Integer.parseInt(srwn.getEnglish())
                    , Integer.parseInt(srwn.getMath())
                    , Integer.parseInt(srwn.getJapanese())
                    , Integer.parseInt(srwn.getScience())
                    , Integer.parseInt(srwn.getSocialStudies())
                    , Integer.parseInt(srwn.getMusic())
                    , Integer.parseInt(srwn.getArt())
                    , Integer.parseInt(srwn.getPe())
                    , Integer.parseInt(srwn.getTechHome())
                    , Integer.parseInt(srwn.getSumFive())
                    , Integer.parseInt(srwn.getSumAll()));

            resultList.add(rowNumber);
        }

        return resultList;
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
                + " WHERE student_id = ? AND char_length(practice_exam.student_id) = 4"
                + " ORDER BY grade ASC, practice_month ASC";

        // RowMapperの生成
        RowMapper<PracticeExam> rowMapper = new BeanPropertyRowMapper<>(PracticeExam.class);

        // SQL実行
        return jdbc.query(sql, rowMapper, studentId);
    }

    @Override
    public List<Integer> insertPracticeMany(List<? extends ImportPracticeExam> list) throws DataAccessException {

        // 結果返却用のリストを生成
        List<Integer> resultList = new ArrayList<>();

        // リストの数の分のinsertを行う
        for (int i = 0; i < list.size(); i++) {

            ImportPracticeExam ipe = list.get(i);

            // クエリーを実行
            int rowNumber = jdbc.update("INSERT INTO practice_exam(student_id,"
                            + " grade,"
                            + " exam_year,"
                            + " practice_month,"
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
                            + " social_deviation)"
                            + " VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)"
                            + " ON CONFLICT ON CONSTRAINT practice_const"
                            + " DO UPDATE SET english_score = ?,"
                            + " math_score = ?,"
                            + " japanese_score = ?,"
                            + " science_score = ?,"
                            + " social_score = ?,"
                            + " sum_three = ?,"
                            + " sum_all = ?,"
                            + " dev_three = ?,"
                            + " dev_five = ?,"
                            + " english_deviation = ?,"
                            + " math_deviation = ?,"
                            + " japanese_deviation = ?,"
                            + " science_deviation = ?,"
                            + " social_deviation = ?;"
                    , ipe.getStudentId()
                    , Integer.parseInt(ipe.getGrade())
                    , Integer.parseInt(ipe.getExamYear())
                    , ipe.getMonthName()
                    , ipe.getEnglishScore()
                    , ipe.getMathScore()
                    , ipe.getJapaneseScore()
                    , ipe.getScienceScore()
                    , ipe.getSocialScore()
                    , ipe.getSumThree()
                    , ipe.getSumAll()
                    , ipe.getDevThree()
                    , ipe.getDevFive()
                    , ipe.getEnglishDeviation()
                    , ipe.getMathDeviation()
                    , ipe.getJapaneseDeviation()
                    , ipe.getScienceDeviation()
                    , ipe.getSocialDeviation()
                    , ipe.getEnglishScore()
                    , ipe.getMathScore()
                    , ipe.getJapaneseScore()
                    , ipe.getScienceScore()
                    , ipe.getSocialScore()
                    , ipe.getSumThree()
                    , ipe.getSumAll()
                    , ipe.getDevThree()
                    , ipe.getDevFive()
                    , ipe.getEnglishDeviation()
                    , ipe.getMathDeviation()
                    , ipe.getJapaneseDeviation()
                    , ipe.getScienceDeviation()
                    , ipe.getSocialDeviation());

            resultList.add(rowNumber);
        }

        return resultList;
    }

    @Override
    public List<Integer> insertRegularMany(List<RegularExam> list) throws DataAccessException {

        // 結果返却用のリストを生成
        List<Integer> resultList = new ArrayList<>();

        // リストの数の分のinsertを行う
        for (int i = 0; i < list.size(); i++) {
            RegularExam regularExam = list.get(i);

            // 生徒情報の取得
            Student student = studentService.selectOne(regularExam.getStudentId());

            // クエリーを実行
            int rowNumber = jdbc.update("INSERT INTO regular_exam(student_id,"
                    + " grade,"
                    + " exam_year,"
                    + " regular_id,"
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
                    + " home)"
                    + " VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)"
                    + " ON CONFLICT ON CONSTRAINT regular_const"
                    + " DO UPDATE SET english = ?,"
                    + " math = ?,"
                    + " japanese = ?,"
                    + " science = ?,"
                    + " social_studies = ?,"
                    + " sum_five = ?,"
                    + " music = ?,"
                    + " art = ?,"
                    + " pe = ?,"
                    + " tech = ?,"
                    + " home = ?;"
            , regularExam.getStudentId()
            , regularExam.getGrade()
            , regularExam.getExamYear()
            , regularExam.getRegular_id()
            , regularExam.getEnglish()
            , regularExam.getMath()
            , regularExam.getJapanese()
            , regularExam.getScience()
            , regularExam.getSocialStudies()
            , regularExam.getSumFive()
            , regularExam.getMusic()
            , regularExam.getArt()
            , regularExam.getPe()
            , regularExam.getTech()
            , regularExam.getHome()
            , regularExam.getEnglish()
            , regularExam.getMath()
            , regularExam.getJapanese()
            , regularExam.getScience()
            , regularExam.getSocialStudies()
            , regularExam.getSumFive()
            , regularExam.getMusic()
            , regularExam.getArt()
            , regularExam.getPe()
            , regularExam.getTech()
            , regularExam.getHome());

            resultList.add(rowNumber);
        }
        return resultList;
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
