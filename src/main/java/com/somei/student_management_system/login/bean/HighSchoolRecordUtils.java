package com.somei.student_management_system.login.bean;

import com.somei.student_management_system.login.domain.model.PublicHighSchool;
import com.somei.student_management_system.login.domain.model.SchoolRecord;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class HighSchoolRecordUtils {

    /**
     * 成績のリストを引数にして、入試に必要な成績の数値のリストを返すメソッド
     *
     * @param schoolRecordList 当該の生徒の成績のリスト
     * @return 必要な成績の数値を格納したリスト
     */
    public static List<Integer> Calculation(List<SchoolRecord> schoolRecordList) {

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
            if (schoolRecord.getGrade().equals("中２") && (termName.equals("３学期") || termName.equals("後期"))) {
                lastOfSecond = schoolRecord;
            }
        }

        // 成績データがある場合は計算処理、ない場合は処理しない
        if (schoolRecordList.size() != 0) {

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
        numList.add(fifteen);
        numList.add(thirty);

        return numList;
    }

    /**
     * リスト内の数値の平均を算出するメソッド
     *
     * @param list Double型の数値リスト
     * @return リスト内の平均数値（double型）
     */
    public static double getAverage(List<Double> list) {
        if (list.size() != 0) {
            double sum = 0;
            // -1(不明)の数を数える変数
            int minusCount = 0;
            for (double num : list) {
                if (num == -1) {
                    // 数値が-1の場合、minusCountを１増やす
                    minusCount++;
                    continue;
                } else {
                    sum += num;
                }
            }
            return sum / (list.size() - minusCount);
        }
        // リストが空だった場合は 0 を返す
        return 0.0;
    }

    /**
     * 全成績のリストから２年の３学期の成績の９科合計を返すメソッド
     *
     * @param allList 全成績のリスト
     * @return ２年の３学期の成績の９科合計（なければ 0 を返す）
     */
    public static int get2ndGradeRecordSum(List<List<SchoolRecord>> allList) {
        // 返却用の変数を初期化
        int secondGradeRecordSum = 0;
        for (List<SchoolRecord> recordList : allList) {
            for (SchoolRecord record : recordList) {
                // リストの中に「学年が中２かつ学期が３学期または後期の成績があれば、９科目合計を取得する
                if (record.getGrade().equals("中２") && (record.getTermName().equals("３学期") || record.getTermName().equals("後期"))) {
                    secondGradeRecordSum = record.getSumAll();
                }
            }
        }
        // ２年の３学期の成績がなければ 0 を返す
        return secondGradeRecordSum;
    }

    /**
     * A値を算出するメソッド
     *
     * @param allList 全成績のリスト
     * @return
     */
    public static double getAScore(List<List<SchoolRecord>> allList) {
        // 返却用の変数を初期化
        double aScore = 0.0;
        if (get2ndGradeRecordSum(allList) != 0) {
            // 中２の３学期の成績がある場合
            double newestRecord = getNewestRecordSum(allList);
            double secondRecord = get2ndGradeRecordSum(allList);
            aScore = ((newestRecord * 2 + secondRecord) / 135) * 100;
        } else {
            // 中２の３学期の成績がない場合
            aScore = (((double) getNewestRecordSum(allList) * 3) / 135) * 100;
        }
        return aScore;
    }

    /**
     * 全成績のリストから最新の成績の９科合計を返すメソッド
     *
     * @param allList 全成績のリスト
     * @return 最新の成績の９科合計（なければ 0 を返す）
     */
    public static int getNewestRecordSum(List<List<SchoolRecord>> allList) {
        // 返却用の変数を初期化
        int newestRecordSum = 0;
        if (allList.get(2).size() != 0) {
            // ３年の成績が空でなかった場合
            List<SchoolRecord> list3 = allList.get(2);
            newestRecordSum = list3.get(list3.size() - 1).getSumAll();
        } else if (allList.get(1).size() != 0) {
            // ２年の成績が空でなかった場合
            List<SchoolRecord> list2 = allList.get(1);
            newestRecordSum = list2.get(list2.size() - 1).getSumAll();
        } else if (allList.get(0).size() != 0) {
            List<SchoolRecord> list1 = allList.get(0);
            newestRecordSum = list1.get(list1.size() - 1).getSumAll();
        }
        return newestRecordSum;
    }

    /**
     * 公立高校の平均内申を double で取得する
     *
     * @param publicHighSchool 当該の公立高校
     * @return 当該の高校の平均内申(3年間の平均)
     */
    public static double getHighSchoolAverageRecord(PublicHighSchool publicHighSchool) {
        // 平均内申を double に変換してリストに格納する
        List<Double> schoolReports = new ArrayList<>();
        try {
            schoolReports.add(Double.parseDouble(publicHighSchool.getSchoolReportThisyear()));
        } catch (RuntimeException e) {
            // 何もせずに飛ばす
        }
        try {
            schoolReports.add(Double.parseDouble(publicHighSchool.getSchoolReportLastyear()));
        } catch (RuntimeException e) {
            // 何もせずに飛ばす
        }
        try {
            schoolReports.add(Double.parseDouble(publicHighSchool.getSchoolReportThreeyearsago()));
        } catch (RuntimeException e) {
            // 何もせずに飛ばす
        }
        return getAverage(schoolReports);
    }

    /**
     * 公立高校の中位合格の点数(=af+bg(S値))を double で取得する
     *
     * @param publicHighSchool 当該の公立高校
     * @return 当該の高校の中位合格の点数(3年間の平均)
     */
    public static double getHighSchoolAverageScore(PublicHighSchool publicHighSchool) {
        List<Double> sScoreList = new ArrayList<>();
        try {
            sScoreList.add(Double.parseDouble(publicHighSchool.getSScoreThisyear()));
        } catch (RuntimeException e) {
            // 何もせずに飛ばす
        }
        try {
            sScoreList.add(Double.parseDouble(publicHighSchool.getSScoreLastyear()));
        } catch (RuntimeException e) {
            // 何もせずに飛ばす
        }
        try {
            sScoreList.add(Double.parseDouble(publicHighSchool.getSScoreThreeyearsago()));
        } catch (RuntimeException e) {
            // 何もせずに飛ばす
        }
        return getAverage(sScoreList);
    }

    /**
     * 公立高校のボーダーの点数(=af+bg(S値))を double で取得する
     *
     * @param publicHighSchool 当該の公立高校
     * @return 当該の高校のボーダーの点数(3年間の平均)
     */
    public static double getHighSchoolAverageBorder(PublicHighSchool publicHighSchool) {
        List<Double> borderScoreList = new ArrayList<>();
        try {
            borderScoreList.add(Double.parseDouble(publicHighSchool.getBorderThisyear()));
        } catch (RuntimeException e) {
            // 何もせずに飛ばす
        }
        try {
            borderScoreList.add(Double.parseDouble(publicHighSchool.getBoderLastyear()));
        } catch (RuntimeException e) {
            // 何もせずに飛ばす
        }
        try {
            borderScoreList.add(Double.parseDouble(publicHighSchool.getSScoreThreeyearsago()));
        } catch (RuntimeException e) {
            // 何もせずに飛ばす
        }
        return getAverage(borderScoreList);
    }
}
