package com.somei.student_management_system.login.bean;

import com.somei.student_management_system.login.domain.model.FuturePath;
import com.somei.student_management_system.login.domain.model.FuturePathWithData;
import com.somei.student_management_system.login.domain.model.PublicHighSchool;
import com.somei.student_management_system.login.domain.model.SchoolRecord;
import com.somei.student_management_system.login.domain.repository.NumericDataDao;
import com.somei.student_management_system.login.domain.repository.StudentDao;
import com.somei.student_management_system.login.domain.service.HighSchoolService;
import org.apache.poi.xssf.usermodel.XSSFSheet;
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

    @Autowired
    PoiMethods poiMethods;

    @Autowired
    HighSchoolService highSchoolService;

    /**
     * 生徒IDを引数にして、計算した数値を含めたFuturePathWithDateを返すメソッド
     *
     * @param studentId 生徒ID
     * @return 生徒のFuturePathWithDate
     */
    public FuturePathWithData getFuturePathDate(String studentId) {

        // インスタンスの生成
        FuturePath futurePath = new FuturePath();

        // データがある場合は、進路データの取得
        if(studentDao.selectPathOne(studentId) != null) {
            futurePath = studentDao.selectPathOne(studentId);
        }

        // 計算済みの数値のリストを取得
        List<Integer> numList = HighSchoolRecordUtils.Calculation(numericDataDao.selectRecordOne(studentId));

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

    /**
     * 面談シート用の公立高校の数値算出メソッド
     *
     * @param sheet            シート
     * @param highschoolIdList 高校のIDが格納されたリスト
     * @param allList          全成績のリスト
     */
    public void publicHighSchoolCalculation(XSSFSheet sheet, List<String> highschoolIdList, List<List<SchoolRecord>> allList, int grade) {

        for (int i = 0; i < highschoolIdList.size(); i++) {

            // 平均内申まで必要な成績の変数を宣言
            int recordNeed;

            // 公立高校を取得
            PublicHighSchool publicHighSchool = highSchoolService.getPublicHighSchoolOne(highschoolIdList.get(i));

            // 公立高校の平均内申を double で取得
            double averageRecord = HighSchoolRecordUtils.getHighSchoolAverageRecord(publicHighSchool);

            // 内申の差を算出
            int secondGradeRecordSum = HighSchoolRecordUtils.get2ndGradeRecordSum(allList);
            int newestRecordSum = HighSchoolRecordUtils.getNewestRecordSum(allList);
            if (secondGradeRecordSum != 0) {
                // ２年の最終成績がある場合
                recordNeed = (int) Math.ceil((averageRecord - secondGradeRecordSum) / 2 - newestRecordSum);
            } else {
                // ２年の最終成績がない場合
                recordNeed = (int) Math.ceil((averageRecord - newestRecordSum) / 2 - newestRecordSum);
            }
            if (recordNeed > 0) {
                // ０より大きい場合（足りていない場合）
                poiMethods.getCell(sheet, i + 35, 2).setCellValue("あと " + recordNeed);
            } else {
                // ０より小さい場合（足りている場合）
                poiMethods.getCell(sheet, i + 35, 2).setCellValue("○");
            }

            // 中位合格の点数を算出
            double averageSscore = HighSchoolRecordUtils.getHighSchoolAverageScore(publicHighSchool);

            // A値を取得
            double aScore = HighSchoolRecordUtils.getAScore(allList);

            // 比率によって処理を分岐する
            if (highSchoolService.getPublicHighSchoolOne(highschoolIdList.get(i)).getRatio().startsWith("3:5:2")) {
                // 3:5:2の場合
                double middlePass = averageSscore - (aScore * 3);
                // セルに入力する
                poiMethods.getCell(sheet, i + 35, 7).setCellValue((int)Math.ceil(middlePass) + "点");
            } else if (highSchoolService.getPublicHighSchoolOne(highschoolIdList.get(i)).getRatio().startsWith("4:4:2")) {
                // 4:4:2の場合
                double middlePass = (averageSscore - (aScore * 4)) / 4 * 5;
                poiMethods.getCell(sheet, i + 35, 7).setCellValue((int)Math.ceil(middlePass) + "点");
            } else if (highSchoolService.getPublicHighSchoolOne(highschoolIdList.get(i)).getRatio().startsWith("5:3:2")) {
                // 5:3:2の場合
                double middlePass = (averageSscore - (aScore * 5)) / 3 * 5;
                poiMethods.getCell(sheet, i + 35, 7).setCellValue((int)Math.ceil(middlePass) + "点");
            }

            if(grade == 3) {
                // 中３の場合は、ボーダー合格の点数を算出
                // ボーダーの平均を算出
                double averageBorder = HighSchoolRecordUtils.getHighSchoolAverageBorder(publicHighSchool);

                // 比率によって処理を分岐する
                if (highSchoolService.getPublicHighSchoolOne(highschoolIdList.get(i)).getRatio().startsWith("3:5:2")) {
                    // 3:5:2の場合
                    double borderPass = averageBorder - (aScore * 3);
                    // セルに入力する
                    poiMethods.getCell(sheet, i + 35, 11).setCellValue((int) Math.ceil(borderPass) + "点");
                } else if (highSchoolService.getPublicHighSchoolOne(highschoolIdList.get(i)).getRatio().startsWith("4:4:2")) {
                    // 4:4:2の場合
                    double borderPass = (averageBorder - (aScore * 4)) / 4 * 5;
                    poiMethods.getCell(sheet, i + 35, 11).setCellValue((int) Math.ceil(borderPass) + "点");
                } else if (highSchoolService.getPublicHighSchoolOne(highschoolIdList.get(i)).getRatio().startsWith("5:3:2")) {
                    // 5:3:2の場合
                    double borderPass = (averageBorder - (aScore * 5)) / 3 * 5;
                    poiMethods.getCell(sheet, i + 35, 11).setCellValue((int) Math.ceil(borderPass) + "点");
                }
            }
        }
    }

    /**
     * 公立高校の数値算出メソッド
     *
     * @param highschoolIdList 高校のIDが格納されたリスト
     * @param allList          全成績のリスト
     */
    public List<List<String>> publicHighSchoolCalculation(List<String> highschoolIdList, List<List<SchoolRecord>> allList) {

        // 返却用のリストを作成
        List<List<String>> resultList = new ArrayList<>();

        for (int i = 0; i < highschoolIdList.size(); i++) {

            // 返却リストに格納するリストを作成(inList：(必要内申、中位合格の点数、ボーダー点数))
            List<String> inList = new ArrayList<>();

            // 平均内申まで必要な成績の変数を宣言
            int recordNeed;

            // 公立高校を取得
            PublicHighSchool publicHighSchool = highSchoolService.getPublicHighSchoolOne(highschoolIdList.get(i));

            // 志望校の登録がなかった場合はループを飛ばす
            if(publicHighSchool == null) {
                continue;
            }

            // 公立高校の平均内申を double で取得
            double averageRecord = HighSchoolRecordUtils.getHighSchoolAverageRecord(publicHighSchool);

            // 平均内申との差を算出
            int secondGradeRecordSum = HighSchoolRecordUtils.get2ndGradeRecordSum(allList);
            int newestRecordSum = HighSchoolRecordUtils.getNewestRecordSum(allList);
            if (secondGradeRecordSum != 0) {
                // ２年の最終成績がある場合
                recordNeed = (int) Math.ceil((averageRecord - secondGradeRecordSum) / 2 - newestRecordSum);
            } else {
                // ２年の最終成績がない場合
                recordNeed = (int) Math.ceil((averageRecord - newestRecordSum) / 2 - newestRecordSum);
            }
            if (recordNeed > 0) {
                // ０より大きい場合（足りていない場合）
                inList.add("あと " + recordNeed);
            } else {
                // ０より小さい場合（足りている場合）
                inList.add("○");
            }

            // 中位合格の点数を算出
            double averageSscore = HighSchoolRecordUtils.getHighSchoolAverageScore(publicHighSchool);

            // A値を取得
            double aScore = HighSchoolRecordUtils.getAScore(allList);

            // 中位合格点数の初期化
            double middlePass = 0.0;

            // 比率によって処理を分岐する
            if (highSchoolService.getPublicHighSchoolOne(highschoolIdList.get(i)).getRatio().startsWith("3:5:2")) {
                // 3:5:2の場合
                middlePass = averageSscore - (aScore * 3);
            } else if (highSchoolService.getPublicHighSchoolOne(highschoolIdList.get(i)).getRatio().startsWith("4:4:2")) {
                // 4:4:2の場合
                middlePass = (averageSscore - (aScore * 4)) / 4 * 5;
            } else if (highSchoolService.getPublicHighSchoolOne(highschoolIdList.get(i)).getRatio().startsWith("5:3:2")) {
                // 5:3:2の場合
                middlePass = (averageSscore - (aScore * 5)) / 3 * 5;
            }
            inList.add((int)Math.ceil(middlePass) + "点");

            // ボーダーの平均を算出
            double averageBorder = HighSchoolRecordUtils.getHighSchoolAverageBorder(publicHighSchool);

            // ボーダーの変数の初期化
            double borderPass = 0.0;

            // 比率によって処理を分岐する
            if (highSchoolService.getPublicHighSchoolOne(highschoolIdList.get(i)).getRatio().startsWith("3:5:2")) {
                // 3:5:2の場合
                borderPass = averageBorder - (aScore * 3);
                // セルに入力する
            } else if (highSchoolService.getPublicHighSchoolOne(highschoolIdList.get(i)).getRatio().startsWith("4:4:2")) {
                // 4:4:2の場合
                borderPass = (averageBorder - (aScore * 4)) / 4 * 5;
            } else if (highSchoolService.getPublicHighSchoolOne(highschoolIdList.get(i)).getRatio().startsWith("5:3:2")) {
                // 5:3:2の場合
                borderPass = (averageBorder - (aScore * 5)) / 3 * 5;
            }
            inList.add((int) Math.ceil(borderPass) + "点");

            resultList.add(inList);
        }
        return resultList;
    }
}
