package com.somei.student_management_system.login.domain.repository;

import com.somei.student_management_system.login.domain.model.FuturePath;
import com.somei.student_management_system.login.domain.model.Student;
import org.springframework.dao.DataAccessException;

import java.util.ArrayList;
import java.util.List;

public interface StudentDao {

    // studentテーブルの件数を取得.
    public ArrayList<Integer> count() throws DataAccessException;

    // studentテーブルにデータを1件insert.
    public int insertOne(Student student) throws DataAccessException;

    // studentテーブルのデータを１件取得
    public Student selectOne(String studentId) throws DataAccessException;

    // studentテーブルの全データを取得.
    public List<Student> selectMany() throws DataAccessException;

    // future_pathテーブルのデータを１件取得
    public FuturePath selectPathOne(String studentId) throws DataAccessException;

    // studentテーブルを１件更新.
    public int updateOne(Student student) throws DataAccessException;

    // future_pathテーブルのデータを１件更新
    public int updatePathOne(FuturePath futurePath) throws DataAccessException;

    // studentテーブルを１件削除.
    public int deleteOne(String studentId) throws DataAccessException;
}
