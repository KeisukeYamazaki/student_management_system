package com.somei.student_management_system.login.bean;

import com.somei.student_management_system.login.domain.model.FuturePath;
import com.somei.student_management_system.login.domain.model.FuturePathWithData;
import com.somei.student_management_system.login.domain.model.SchoolRecord;
import com.somei.student_management_system.login.domain.repository.NumericDataDao;
import com.somei.student_management_system.login.domain.repository.StudentDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class EntranceExamCalculation {

    @Autowired
    NumericDataDao numericDataDao;

    @Autowired
    StudentDao studentDao;

    // 生徒IDを引数にして、計算した数値を含めたFuturePathWithDateを返すメソッド
    public FuturePathWithData getFuturePathDate(String studentId) {

        // インスタンスの生成
        FuturePath futurePath = new FuturePath();

        // データがある場合は、進路データの取得
        if(studentDao.selectPathOne(studentId) != null) {
            futurePath = studentDao.selectPathOne(studentId);
        }

        // 計算済みの数値のリストを取得
        List<Integer> numList = Calculation(numericDataDao.selectRecordOne(studentId));

        // FuturePathWithDateに代入して返す
        FuturePathWithData data = new FuturePathWithData();

        data.setFortyFive(numList.get(0));
        data.setHundredThirtyFive(numList.get(1));
        data.setTwentyFive(numList.get(2));
        data.setFifty(numList.get(3));
        data.setSeventyFive(numList.get(4));
        data.setFifteen(numList.get(5));
        data.setThirty(numList.get(6));
        data.setStudentId(futurePath.getStudentId());
        data.setFirstChoice(futurePath.getFirstChoice());
        data.setFirstSituation(futurePath.getFirstSituation());
        data.setSecondChoice(futurePath.getSecondChoice());
        data.setSecondSituation(futurePath.getSecondSituation());
        data.setThirdChoice(futurePath.getThirdChoice());
        data.setThirdSituation(futurePath.getThirdSituation());
        data.setPublicSchool1(futurePath.getPublicSchool1());
        data.setPublicSchool2(futurePath.getPublicSchool2());
        data.setPublicSchool3(futurePath.getPublicSchool3());
        data.setPrivateSchool1(futurePath.getPrivateSchool1());
        data.setPrivateSchool2(futurePath.getPrivateSchool2());
        data.setPrivateSchool3(futurePath.getPrivateSchool3());
        data.setInformation(futurePath.getInformation());
        data.setRecordDate(futurePath.getRecordDate());

        return data;
    }

    // 成績のリストを引数にして、入試に必要な数値のリストを返すメソッド
    public List<Integer> Calculation(List<SchoolRecord> schoolRecordList) {

        // 値を格納して送るリストを作成
        List<Integer> numList = new ArrayList<>();

        // 計算する値の変数
        Integer fortyFive = null;  // 最新の/45
        Integer hundredThirtyFive = null;  // 最新の/45 * 2 + 中2学年末の/45(なければ 最新の/45 * 3)
        Integer twentyFive = null;  // 最新の/25
        Integer fifty = null;  // 最新の/25 + 中２学年末の/25(なければ 最新の/25 * 2)
        Integer seventyFive = null;  // 最新の/25 + 中２学年末の/25 * 2(なければ 最新の/25 * 3)
        Integer fifteen = null;  // 最新の /15
        Integer thirty = null;  // 最新の/15 + 中２学年末の/15(なければ 最新の/15 * 2)

        // 2年の学年末の成績を格納する変数
        SchoolRecord lastOfSecond = null;

        // リストの中に中２の学年末があるかを探す
        for (SchoolRecord schoolRecord : schoolRecordList) {
            String termName = schoolRecord.getTermName();

            // 中２かつ３学期か後期があれば、変数に格納
            if (schoolRecord.getGrade() == 2 && (termName.equals("３学期") || termName.equals("後期"))) {
                lastOfSecond = schoolRecord;
            }
        }

        // 成績データがある場合は計算処理、ない場合は処理しない
        if(schoolRecordList.size() != 0) {

            // 最新の番号を取得
            int count = schoolRecordList.size() - 1;

            // 最新の成績を取得
            SchoolRecord newestRecord = schoolRecordList.get(count);

            // 最新のものを代入するものから代入
            fortyFive = newestRecord.getSumAll();
            twentyFive = newestRecord.getSumFive();
            fifteen = newestRecord.getEnglish() + newestRecord.getMath() + newestRecord.getJapanese();

            // 中２学年末の有無で条件分岐
            if (lastOfSecond != null) {
                hundredThirtyFive = newestRecord.getSumAll() * 2 + lastOfSecond.getSumAll();
                fifty = newestRecord.getSumFive() + lastOfSecond.getSumFive();
                seventyFive = newestRecord.getSumFive() * 2 + lastOfSecond.getSumFive();
                thirty = fifteen + lastOfSecond.getEnglish() + lastOfSecond.getMath() + lastOfSecond.getJapanese();
            } else {
                hundredThirtyFive = fortyFive * 3;
                fifty = twentyFive * 2;
                seventyFive = twentyFive * 3;
                thirty = fifteen * 2;
            }
        }

        // リストに格納する
        numList.add(fortyFive);
        numList.add(hundredThirtyFive);
        numList.add(twentyFive);
        numList.add(fifty);
        numList.add(seventyFive);
        numList.add(thirty);
        numList.add(fifteen);

        return numList;
    }
}
