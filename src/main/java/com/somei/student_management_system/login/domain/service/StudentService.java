package com.somei.student_management_system.login.domain.service;

import com.somei.student_management_system.login.domain.model.Student;
import com.somei.student_management_system.login.domain.repository.StudentDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class StudentService {

    @Autowired
    StudentDao dao;

    /**
     * insert用メソッド.
     */
    public boolean insert(Student student) {

        // insert実行
        int rowNumber = dao.insertOne(student);

        // 判定用変数
        boolean result = false;

        if (rowNumber > 0) {
            // insert成功
            result = true;
        }

        return result;
    }

    /**
     * カウント用メソッド.
     */
    public ArrayList<Integer> count() {
        return dao.count();
    }

    /**
     * 全件取得用メソッド.
     */
    public List<Student> selectMany() {
        //全件取得
        return dao.selectMany();
    }

    /**
     * １件取得用メソッド.
     */
    public Student selectOne(String studentId) {
        // selectOne実行
        return dao.selectOne(studentId);
    }

    /**
     * １件更新用メソッド.
     */
    public boolean updateOne(Student student) {

        //判定用変数
        boolean result = false;

        // １件更新
        int rowNumber = dao.updateOne(student);

        if (rowNumber > 0) {
            //update成功
            result = true;
        }

        return result;
    }

    /**
     * １件削除用メソッド.
     */
    public boolean deleteOne(String studentId) {

        //１件削除
        int rowNumber = dao.deleteOne(studentId);

        //判定用変数
        boolean result = false;

        if (rowNumber > 0) {
            //delete成功
            result = true;
        }
        return result;
    }

}
