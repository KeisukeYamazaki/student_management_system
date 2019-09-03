package com.somei.student_management_system.login.domain.repository.jdbc;

import com.somei.student_management_system.login.domain.model.Zenken;
import com.somei.student_management_system.login.domain.repository.ZenkenDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository("ZenkenDaoJdbcImpl")
public class ZenkenDaoJdbcImpl implements ZenkenDao {

    @Autowired
    JdbcTemplate jdbc;

    @Override
    public int count() throws DataAccessException {
        return 0;
    }

    @Override
    public int insertOne(Zenken zenken) throws DataAccessException {

        int rowNumber = jdbc.update("INSERT INTO practice_registry(id,"
                        + " enrollment,"
                        + " prefecture)"
                        + " VALUES (?,?,?)",
                zenken.getId(),
                Integer.parseInt(zenken.getEnrollment()),
                Integer.parseInt(zenken.getPrefecture()));

        return rowNumber;
    }

    @Override
    public List<Zenken> selectMany(String school, String grade) throws DataAccessException {
        //studentテーブルの指定学年のデータを全件取得するSQL
        String sql = "SELECT id,"
                + " number,"
                + " gender,"
                + " enrollment,"
                + " student_name,"
                + " name_ruby,"
                + " prefecture,"
                + " city,"
                + " record_term"
                + " FROM practice_registry"
                + " JOIN student"
                + " ON practice_registry.id = student.student_id"
                + " WHERE school = ? and grade = ? AND char_length(student.student_id) = 4"
                + " ORDER BY number";

        //RowMapperの生成
        RowMapper<Zenken> rowMapper = new BeanPropertyRowMapper<>(Zenken.class);

        //SQL実行
        return jdbc.query(sql, rowMapper, school, grade);
    }

    @Override
    public List<Integer> updateMany(List<Zenken> list) throws DataAccessException {

        // 結果返却用のリストを生成
        List<Integer> resultList = new ArrayList<>();

        // リストの数の分のupdateを行う
        for (int i = 0; i < list.size(); i++) {

            int rowNumber = jdbc.update("UPDATE practice_registry"
                            + " SET"
                            + " number = ?,"
                            + " gender = ?,"
                            + " enrollment = ?,"
                            + " prefecture = ?,"
                            + " city = ?,"
                            + " record_term = ?"
                            + " WHERE id = ?"
                    , Integer.parseInt(list.get(i).getNumber())
                    , Integer.parseInt(list.get(i).getGender())
                    , Integer.parseInt(list.get(i).getEnrollment())
                    , Integer.parseInt(list.get(i).getPrefecture())
                    , Integer.parseInt(list.get(i).getCity())
                    , Integer.parseInt(list.get(i).getRecordTerm())
                    , list.get(i).getId());

            resultList.add(rowNumber);
        }

        return resultList;
    }
}
