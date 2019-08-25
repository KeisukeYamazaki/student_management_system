package com.somei.student_management_system.login.domain.repository;

import com.somei.student_management_system.login.domain.model.Zenken;
import org.springframework.dao.DataAccessException;

import java.util.List;

public interface ZenkenDao {

    // Zenkenテーブルの件数を取得.
    public int count() throws DataAccessException;

    // Zenkenテーブルにデータを1件insert.
    public int insertOne(Zenken zenken) throws DataAccessException;

    // Zenkenテーブルの全データを取得.
    public List<Zenken> selectMany(String school, String grade) throws DataAccessException;

    // Zenkenテーブルを複数件更新.
    public List<Integer> updateMany(List<Zenken> list) throws DataAccessException;

    // Zenkenテーブルを１件削除.
    public int deleteOne(String id) throws DataAccessException;


}
