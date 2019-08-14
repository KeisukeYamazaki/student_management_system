package com.somei.student_management_system.login.domain.repository;

import com.somei.student_management_system.login.domain.model.Student;
import org.springframework.dao.DataAccessException;

import java.util.ArrayList;
import java.util.List;

public interface StudentDao {

    // Studentテーブルの件数を取得.
    public ArrayList<Integer> count() throws DataAccessException;

    // Studentテーブルにデータを1件insert.
    public int insertOne(Student student) throws DataAccessException;

    // Studentテーブルのデータを１件取得
    public Student selectOne(String studentId) throws DataAccessException;

    // Studentテーブルの全データを取得.
    public List<Student> selectMany() throws DataAccessException;

    // Studentテーブルを１件更新.
    public int updateOne(Student student) throws DataAccessException;

    // Studentテーブルを１件削除.
    public int deleteOne(String studentId) throws DataAccessException;
}
