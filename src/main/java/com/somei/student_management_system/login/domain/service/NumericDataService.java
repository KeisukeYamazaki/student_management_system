package com.somei.student_management_system.login.domain.service;

import com.somei.student_management_system.login.domain.model.ImportPracticeExam;
import com.somei.student_management_system.login.domain.model.PracticeExam;
import com.somei.student_management_system.login.domain.model.RegularExam;
import com.somei.student_management_system.login.domain.model.SchoolRecord;
import com.somei.student_management_system.login.domain.model.SchoolRecordWithName;
import com.somei.student_management_system.login.domain.repository.NumericDataDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NumericDataService {

    @Autowired
    NumericDataDao dao;

    /**
     * insert用メソッド.
     */


    /**
     * 1人分成績取得用メソッド.
     */
    public List<SchoolRecord> selectRecordOne(String studentId) {
        //全件取得
        return dao.selectRecordOne(studentId);
    }



    /**
     * 複数の成績を取得.
     */
    public List<SchoolRecordWithName> selectRecordMany(String school, String grade, String termName) {
        //全件取得
        return dao.selectRecordMany(school, grade, termName);
    }

    /**
     * 複数の成績を挿入
     * @param list 成績のリスト
     * @return 更新の結果
     */
    public boolean updateRecordMany(List<SchoolRecordWithName> list) {

        //判定用変数
        boolean result = false;

        // 複数更新
        List<Integer> rowNumbers = dao.insertRecordMany(list);

        for (Integer rowNumber : rowNumbers) {

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
     * １件削除用メソッド.
     */


    /**
     * 1人分模試取得用メソッド.
     */
    public List<PracticeExam> selectPracticeOne(String studentId) {
        //全件取得
        return dao.selectPracticeOne(studentId);
    }

    /**
     * 複数人の模試データ挿入メソッド.
     * @param list 模試データのリスト
     */
    public boolean insertPracticeMany(List<ImportPracticeExam> list) {

        //判定用変数
        boolean result = false;

        // 複数更新
        List<Integer> rowNumbers = dao.insertPracticeMany(list);

        for (Integer rowNumber : rowNumbers) {

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
     * 1人分定期試験取得用メソッド.
     */
    public List<RegularExam> selectRegularOne(String studentId) {
        //全件取得
        return dao.selectRegularOne(studentId);
    }
}
