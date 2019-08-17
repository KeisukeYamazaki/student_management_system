package com.somei.student_management_system.login.domain.repository.jdbc;

import com.somei.student_management_system.login.domain.model.FuturePath;
import com.somei.student_management_system.login.domain.model.Student;
import com.somei.student_management_system.login.domain.repository.StudentDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class StudentDaoJdbcImpl implements StudentDao {

    @Autowired
    private JdbcTemplate jdbc;

    @Override
    // Studentテーブルの件数を取得
    public ArrayList<Integer> count() throws DataAccessException {
        // 全件・各学年の結果を格納するリストを作成
        ArrayList<Integer> counts = new ArrayList<>();

        // 全件取得してカウント
        int count = jdbc.queryForObject("SELECT COUNT(*) FROM student", Integer.class);

        // 全件取得の結果を加える
        counts.add(count);

        // 各学年をカウントしてリストに入れる
        counts.add(jdbc.queryForObject("SELECT COUNT(*) FROM student WHERE grade = '中３'", Integer.class));
        counts.add(jdbc.queryForObject("SELECT COUNT(*) FROM student WHERE grade = '中２'", Integer.class));
        counts.add(jdbc.queryForObject("SELECT COUNT(*) FROM student WHERE grade = '中１'", Integer.class));
        counts.add(jdbc.queryForObject("SELECT COUNT(*) FROM student WHERE grade = '小６'", Integer.class));
        counts.add(jdbc.queryForObject("SELECT COUNT(*) FROM student WHERE grade = '小５'", Integer.class));
        counts.add(jdbc.queryForObject("SELECT COUNT(*) FROM student WHERE grade = '小４'", Integer.class));
        counts.add(jdbc.queryForObject("SELECT COUNT(*) FROM student WHERE grade = '小３'", Integer.class));

        return counts;
    }

    @Override
    // Studentテーブルにデータを1件insert.
    public int insertOne(Student student) throws DataAccessException {
        //１件登録
        int rowNumber = jdbc.update("INSERT INTO student(student_id,"
                        + " student_name,"
                        + " name_ruby,"
                        + " grade,"
                        + " school,"
                        + " home_room,"
                        + " local_school,"
                        + " entry_time,"
                        + " birthday,"
                        + " club,"
                        + " parents,"
                        + " siblings,"
                        + " address,"
                        + " info)"
                        + " VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
                student.getStudentId(),
                student.getStudentName(),
                student.getNameRuby(),
                student.getGrade(),
                student.getSchool(),
                student.getHomeRoom(),
                student.getLocalSchool(),
                student.getEntryTime(),
                student.getBirthday(),
                student.getClub(),
                student.getParents(),
                student.getSiblings(),
                student.getAddress(),
                student.getInfo());

        return rowNumber;

    }

    //ユーザー１件取得
    @Override
    public Student selectOne(String studentId) {

        //１件取得用SQL
        String sql = "SELECT * FROM student WHERE student_id = ?";

        //RowMapperの生成
        RowMapper<Student> rowMapper = new BeanPropertyRowMapper<>(Student.class);

        //SQL実行
        return jdbc.queryForObject(sql, rowMapper, studentId);
    }

    //ユーザー全件取得
    @Override
    public List<Student> selectMany() {

        //studentテーブルのデータを全件取得するSQL
        String sql = "SELECT * FROM student ORDER BY home_room COLLATE \"C\", student_id";

        //RowMapperの生成
        RowMapper<Student> rowMapper = new BeanPropertyRowMapper<Student>(Student.class);

        //SQL実行
        return jdbc.query(sql, rowMapper);
    }

    @Override
    public FuturePath selectPathOne(String studentId) throws DataAccessException {

        try {

            //future_pathテーブルのデータを全件取得するSQL
            String sql = "SELECT * FROM future_path WHERE student_id = ?";

            //RowMapperの生成
            RowMapper<FuturePath> rowMapper = new BeanPropertyRowMapper<>(FuturePath.class);

            //SQL実行
            return jdbc.queryForObject(sql, rowMapper, studentId);

        } catch (DataAccessException e) {

            //データが無かった場合は、nullを返す
            return null;
        }
    }

    @Override
    public int updateOne(Student student) throws DataAccessException {
        //１件更新
        int rowNumber = jdbc.update("UPDATE student"
                        + " SET"
                        + " student_name = ?,"
                        + " name_ruby = ?,"
                        + " grade = ?,"
                        + " school = ?,"
                        + " home_room = ?,"
                        + " local_school = ?,"
                        + " entry_time = ?,"
                        + " birthday = ?,"
                        + " club = ?,"
                        + " parents = ?,"
                        + " siblings = ?,"
                        + " address = ?,"
                        + " info = ?"
                        + " WHERE student_id = ?"
                , student.getStudentName()
                , student.getNameRuby()
                , student.getGrade()
                , student.getSchool()
                , student.getHomeRoom()
                , student.getLocalSchool()
                , student.getEntryTime()
                , student.getBirthday()
                , student.getClub()
                , student.getParents()
                , student.getSiblings()
                , student.getAddress()
                , student.getInfo()
                , student.getStudentId());

        return rowNumber;
    }

    @Override
    public int updatePathOne(FuturePath futurePath) throws DataAccessException {
        //１件更新
        int rowNumber = jdbc.update("UPDATE future_path"
                        + " SET"
                        + " first_choice = ?,"
                        + " second_choice = ?,"
                        + " third_choice = ?,"
                        + " public_school1 = ?,"
                        + " public_school2 = ?,"
                        + " public_school3 = ?,"
                        + " private_school1 = ?,"
                        + " private_school2 = ?,"
                        + " private_school3 = ?,"
                        + " information = ?"
                        + " WHERE student_id = ?"
                , futurePath.getFirstChoice()
                , futurePath.getSecondChoice()
                , futurePath.getThirdChoice()
                , futurePath.getPublicSchool1()
                , futurePath.getPublicSchool2()
                , futurePath.getPublicSchool3()
                , futurePath.getPrivateSchool1()
                , futurePath.getPrivateSchool2()
                , futurePath.getPrivateSchool3()
                , futurePath.getInformation()
                , futurePath.getStudentId());

        return rowNumber;
    }

    @Override
    public int deleteOne(String studentId) throws DataAccessException {
        //１件削除
        int rowNumber = jdbc.update("DELETE FROM student WHERE student_id = ?", studentId);

        return rowNumber;
    }
}


