package com.somei.student_management_system.login.domain.service;

import com.somei.student_management_system.login.bean.EntranceExamCalculation;
import com.somei.student_management_system.login.domain.model.FuturePath;
import com.somei.student_management_system.login.domain.model.FuturePathWithData;
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

    @Autowired
    EntranceExamCalculation entranceExamCalculation;

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
     * 指定学年全件取得メソッド.
     */
    public List<Student> selectManyByGrade(String grade) {
        // 指定学年を全件取得
        return dao.selectManyByGrade(grade);
    }

    /**
     * 生徒データ１件取得用メソッド.
     */
    public Student selectOne(String studentId) {
        // selectOne実行
        return dao.selectOne(studentId);
    }

    /**
     * 進路データ１件取得用メソッド.
     */
    public FuturePathWithData selectPathDataOne(String studentId) {

        // 計算した数値を含めて送る
        return entranceExamCalculation.getFuturePathDate(studentId);
    }

    /**
     * 生徒データ１件更新用メソッド.
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
     * 生徒のクラス複数更新メソッド
     */
    public boolean updateHomeRoom(List<Student> list) {

        //判定用変数
        boolean result = false;

        // 複数更新
        List<Integer> rowNumbers = dao.updateHomeRoom(list);

        for(Integer rowNumber : rowNumbers) {

            if (rowNumber > 0) {

                //update成功
                result = true;

            } else {
                // もし更新できない行があったら、falseを返す
                return false;
            }

        }

        return result;
    }


    /**
     * 進路データ１件更新用メソッド.
     */
    public boolean updatePathOne(FuturePath futurePath) {

        // 判定用変数
        boolean result = false;

        // １件更新
        int rowNumber = dao.updatePathOne(futurePath);

        if (rowNumber > 0) {
            // update成功
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
