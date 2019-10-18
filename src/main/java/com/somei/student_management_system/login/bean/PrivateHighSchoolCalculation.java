package com.somei.student_management_system.login.bean;

import com.somei.student_management_system.login.domain.model.PrivateHighSchool;
import com.somei.student_management_system.login.domain.model.SchoolRecord;
import com.somei.student_management_system.login.domain.repository.NumericDataDao;
import com.somei.student_management_system.login.domain.service.HighSchoolService;
import com.somei.student_management_system.login.domain.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class PrivateHighSchoolCalculation {

    @Autowired
    HighSchoolService highSchoolService;

    @Autowired
    MeetingSheetFuturePathWriter meetingSheetFuturePathWriter;

    @Autowired
    EntranceExamCalculation entranceExamCalculation;

    @Autowired
    NumericDataDao numericDataDao;

    @Autowired
    StudentService studentService;

    /**
     * 私立高校の合否判定・必要な数値の計算メソッド
     *
     * @param studentId             生徒のID
     * @param privateHighSchoolList 志望する私立高校のID
     * @return
     */
    public List<String> privateHighSchoolCalculation(String studentId, List<String> privateHighSchoolList) {

        // 返却用のリストを作成
        List<String> resultList = new ArrayList<>();

        // 対象の生徒のそれぞれの内申の数値を取得(/45, /135, /25, /50, /75, /15, /30 の順でリストを取得）
        List<Integer> numList = entranceExamCalculation.Calculation(numericDataDao.selectRecordOne(studentId));

        // 基準値を抽出する
        List<String[]> standardList = extractStandards(privateHighSchoolList);

        // 判定する
        String result = "";  // 特定の高校判定の際の結果代入用の変数
        for (int i = 0; i < standardList.size(); i++) {

            // リストに特定の高校が含まれている場合は、特定のメソッドで判定する
            switch (standardList.get(i)[0]) {
                case "7420":
                    result = yamateHighSchoolJudgement(numList);
                    resultList.add(result);
                    break;
                case "7141":
                    String grade = studentService.selectOne(studentId).getGrade();
                    result = nichifujiHighSchoolJudgement(grade, numList);
                    resultList.add(result);
                    break;
                default:
                    // 検定があるものは判定しない
                    if (Arrays.asList(standardList.get(i)).contains("英") || Arrays.asList(standardList.get(i)).contains("英検")
                            || Arrays.asList(standardList.get(i)).contains("数検") || Arrays.asList(standardList.get(i)).contains("漢検")) {
                        //検定があった場合は、空文字を返却用のリストに追加する（判定しない）
                        resultList.add("");
                        break;
                    }
                    // 「かつ」が２以上あるかどうか
                    if (countAnd(standardList.get(i)) >= 2) {
                        // 「かつ」が２つ以上ある場合は、空文字を返却用のリストに追加する（判定しない）
                        resultList.add("");
                        break;
                    }
                    if (Arrays.asList(standardList.get(i)).contains("かつ")) {
                        // 「かつ」がある場合
                        String[] judgementList = removeAnd(standardList.get(i));
                        // 判定を行い、その結果を返却用のリストに格納する
                        resultList.add(andJudegment(numList, judgementList));
                    } else {
                        //「または」がある場合
                        String[] judgementList = removeOr(standardList.get(i));
                        // 判定を行い、その結果を返却用のリストに格納する
                        resultList.add(OrJudegment(numList, judgementList));
                    }
            }
        }

        // 結果を返却する（リストの順番は、志望順に並んでいる）
        return resultList;
    }

    /**
     * 基準値抽出メソッド
     *
     * @param privateHighSchoolList シート
     * @return
     */
    private List<String[]> extractStandards(List<String> privateHighSchoolList) {

        // 基準値を抽出したものを格納するリストの作成
        List<String[]> standardList = new ArrayList<>();


        // 高校IDのリストをループで回して、基準値の配列を作り格納する
        String[] school = new String[1]; // 特定の高校のIDを格納する配列を作成
        for (int i = 0; i < privateHighSchoolList.size(); i++) {

            // 特定の高校の場合、高校IDをそのまま格納（あとで別処理をする）
            switch(privateHighSchoolList.get(i)) {
                case "7410":
                    // 7410を含む配列をつくり、格納する（山手学院）
                    school[0] = "7410";
                    standardList.add(school);
                    break;
                case "7141":
                    // 7141を含む配列をつくり、格納する（日本大学藤沢）
                    school[0] = "7141";
                    standardList.add(school);
                    break;
                default:
                    // 高校を取得
                    PrivateHighSchool highSchool = highSchoolService.getPrivateHighSchoolOne(privateHighSchoolList.get(i));
                    // 分割して配列を作成し、リストに格納
                    standardList.add(highSchool.getStandard().split(" "));
            }
        }

        return standardList;
    }

    /**
     * 「かつ」の数を数えるメソッド
     *
     * @param list 基準値の配列
     * @return 「かつ」の数
     */
    private int countAnd(String[] list) {

        // カウントする変数
        int count = 0;
        // ループを回して「かつ」の数を数える
        for (int i = 0; i < list.length; i++) {

            if (list[i].equals("かつ")) {
                count++;
            }
        }
        // カウントを返す
        return count;
    }

    /**
     * 配列から「かつ」を除くメソッド
     *
     * @param list 判定する高校の基準値が入った配列
     * @return 基準値の文字列
     */
    private String[] removeAnd(String[] list) {

        // 配列をリストに変換
        List<String> conversedList = new ArrayList<>(Arrays.asList(list));

        // リストから「かつ」を削除
        conversedList.remove("かつ");

        // リストを配列に変換して戻す
        return conversedList.toArray(new String[conversedList.size()]);
    }

    /**
     * 配列から「または」を除くメソッド
     *
     * @param list 判定する高校の基準値が入った配列
     * @return 基準値の文字列
     */
    private String[] removeOr(String[] list) {

        // 配列をリストに変換
        List<String> conversedList = new ArrayList<>(Arrays.asList(list));

        // 配列のなかの「または」をすべて削除
        conversedList.removeIf(element -> element.equals("または"));

        // リストを配列に変換して戻す
        return conversedList.toArray(new String[conversedList.size()]);
    }


    /**
     * 「かつ」を含む基準の判定メソッド
     * <p>
     * このメソッドで判定できるのは「A かつ B」の場合のみ
     * </p>
     *
     * @param numList  現状の成績の数値一覧
     * @param standard 判定する高校の基準値が入った配列
     * @return 判定結果の文字列
     */
    private String andJudegment(List<Integer> numList, String[] standard) {

        // １つ目の基準値の判定
        String judgement1 = judgmentResult(numList, standard, 1);

        // ２つ目の基準値の判定
        String judgement2 = judgmentResult(numList, standard, 3);

        if (judgement1.equals("◯") && judgement2.equals("◯")) {
            // ２つとも基準値をクリアしていれば「○」を返す
            return "◯";
        } else if (judgement1.equals("◯") || judgement2.equals("◯")) {
            if (judgement1.equals("◯")) {
                // いずれかがクリアしている場合は、クリアしていないほうの基準値を返す
                return judgement2;
            } else {
                return judgement1;
            }
        } else {
            // どちらもクリアしていない場合は、両方の基準値を返す
            return judgement1 + "かつ" + judgement2;
        }
    }

    /**
     * 「または」を含む基準の判定メソッド
     *
     * @param numList  現状の成績の数値一覧
     * @param standard 判定する高校の基準値が入った配列
     * @return 判定結果の文字列
     */
    private String OrJudegment(List<Integer> numList, String[] standard) {

        // 結果格納用のリストを作成
        List<String> resultList = new ArrayList<>();

        // 基準値の数の分だけループする。 i は基準値の分母を取り出す要素番号。
        // 基準値の分母は要素の１つおきにあるので、ループのスタートを１、増加分を２とする
        for (int i = 1; i < standard.length; i += 2) {

            if (judgmentResult(numList, standard, i).equals("○")) {
                // 基準値の判定結果が「○」なら、その時点で○を返し、処理を終了する
                return "○";
            } else {
                // 基準値に満たない場合は、必要な数値の文字列をリストに格納する
                resultList.add(judgmentResult(numList, standard, i));
            }
        }
        // 結果格納用リストから１つずつ取り出して、返却用の文字列に加える
        String result = resultList.get(0);
        for (int i = 1; i < resultList.size(); i++) {
            result = result + "または " + resultList.get(i);
        }
        // 結果を戻す
        return result;
    }

    /**
     * 成績と基準値を照らし合わせて判定するメソッド
     *
     * @param numList    現状の成績の数値一覧
     * @param standard   判定する高校の基準値が入った配列
     * @param elementNum 判定する基準値の分母（この要素から-1した数が分子(=基準値))
     * @return 判定結果の文字列
     */
    private String judgmentResult(List<Integer> numList, String[] standard, int elementNum) {

        //(/45, /135, /25, /50, /75, /15, /30 の順でリストを取得）

        // 結果返却用の文字列を初期化
        String result = "";

        switch (standard[elementNum]) {
            case "/135":
                if (Integer.parseInt(standard[elementNum - 1]) <= numList.get(1)) {
                    result = "○";
                } else {
                    int num = (int) (Math.ceil((Integer.parseInt(standard[elementNum - 1]) - numList.get(1)) / 2));
                    result = "9科目であと " + num;
                }
                break;
            case "/45":
                if (Integer.parseInt(standard[elementNum - 1]) <= numList.get(0)) {
                    result = "○";
                } else {
                    int num = Integer.parseInt(standard[elementNum - 1]) - numList.get(0);
                    result = "9科目であと " + num;
                }
                break;
            case "/75":
                if (Integer.parseInt(standard[elementNum - 1]) <= numList.get(4)) {
                    result = "○";
                } else {
                    int num = (int) (Math.ceil((Integer.parseInt(standard[elementNum - 1]) - numList.get(4)) / 2));
                    result = "5科目であと " + num;
                }
                break;
            case "/50":
                if (Integer.parseInt(standard[elementNum - 1]) <= numList.get(3)) {
                    result = "○";
                } else {
                    int num = Integer.parseInt(standard[elementNum - 1]) - numList.get(3);
                    result = "5科目であと " + num;
                }
                break;
            case "/25":
                if (Integer.parseInt(standard[elementNum - 1]) <= numList.get(2)) {
                    result = "○";
                } else {
                    int num = Integer.parseInt(standard[elementNum - 1]) - numList.get(2);
                    result = "5科目であと " + num;
                }
                break;
            case "/30":
                if (Integer.parseInt(standard[elementNum - 1]) <= numList.get(6)) {
                    result = "○";
                } else {
                    int num = Integer.parseInt(standard[elementNum - 1]) - numList.get(6);
                    result = "3科目であと " + num;
                }
                break;
            case "/15":
                if (Integer.parseInt(standard[elementNum - 1]) <= numList.get(5)) {
                    result = "○";
                } else {
                    int num = Integer.parseInt(standard[elementNum - 1]) - numList.get(5);
                    result = "3科目であと " + num;
                }
                break;
        }
        // 結果を戻す
        return result;
    }

    /**
     * 全成績のリストから２年の３学期の成績の５科合計を返すメソッド
     *
     * @param allList 全成績のリスト
     * @return ２年の３学期の成績の５科合計（なければ 0 を返す）
     */
    private int get2ndGradeRecordSumFive(List<SchoolRecord> allList) {
        // 返却用の変数を初期化
        int secondGradeRecordSumFive = 0;
        for (SchoolRecord record : allList) {
            // リストの中に「学年が中２かつ学期が３学期または後期の成績があれば、９科目合計を取得する
            if (record.getGrade().equals("中２") && (record.getTermName().equals("３学期") || record.getTermName().equals("後期"))) {
                secondGradeRecordSumFive = record.getSumFive();
            }
        }
        // ２年の３学期の成績がなければ 0 を返す
        return secondGradeRecordSumFive;
    }

    /**
     * 山手学院の処理
     *
     * @param numList 生徒の成績リスト
     * @return 判定の結果
     */
    private String yamateHighSchoolJudgement(List<Integer> numList) {

        // 結果を返す変数を初期化する
        String result = "";

        // numListの中身：(/45, /135, /25, /50, /75, /15, /30 の順）
        // 合格しているかどうかの判定
        if(numList.get(1) >= 128) {
            // 128/135をクリアした場合
            result = "◯";
        } else if (numList.get(3) >= 49) {
            // 49/50をクリアした場合
            result = "◯";
        } else if (numList.get(0) >= 42 && numList.get(2) >= 24) {
            result = "◯";
        }

        if(result.equals("◯")) {
            // 結果が ◯ の場合、そのまま返却
            return result;
        } else {
            // 結果が ○ でない場合、それぞれの基準値の差を計算する
            int gap1 = (int)Math.ceil((128 - numList.get(1))/2);  // ９科目合計で必要な内申
            int gap2 = 49 - numList.get(3);   // ５科目合計で必要な内申
            int gap3 = 42 - numList.get(0);
            int gap4 = 24 - numList.get(2);
            int gap5 = gap3 + gap4;

            // ３つの最小値を求める
            int[] gaps = {gap1, gap2, gap5};
            int min = gaps[0];   // 最小値の初期化
            int num = 0;   // 最小値の配列の添字を代入する変数
            for(int i = 1; i < gaps.length; i++) {
                if(min > gaps[i]) {
                    min = gaps[i];
                    num = i;
                }
            }
            // resultに代入する文字列を確定する
            switch (num) {
                case 0:
                    result = "9科目であと " + num;
                    break;
                case 1:
                    result = "5科目であと " + num;
                    break;
                case 2:
                    result = "9科目であと " + gap3 + " かつ " + "5科目であと " + gap4;
            }
            return result;
        }
    }

    /**
     * 日本大学藤沢の処理
     *
     * @param grade 生徒の学年
     * @param numList 生徒の成績リスト
     * @return 判定の結果
     */
    private String nichifujiHighSchoolJudgement(String grade, List<Integer> numList) {

        // 結果を返す変数を初期化する
        String result = "";

        // numListの中身：(/45, /135, /25, /50, /75, /15, /30 の順）
        // 学年によって処理が分かれる
        if(grade.equals("中３")) {
            // 中３の基準で計算する
            int gap1 = 40 - numList.get(0);
            int gap2 = 23 - numList.get(2);
            if(gap1 <= 0 && gap2 <= 0) {
                // 基準を満たしている場合
                result = "◯";
            } else {
                // 基準を満たしていない場合
                if(gap1 > 0 && gap2 > 0) {
                    // 両方満たしていない場合
                    result = "9科目であと " + gap1 + " かつ " + "5科目であと " + gap2;
                } else if (gap1 > 0) {
                    // ９科目の基準が満たしていない場合
                    result = "9科目であと " + gap1;
                } else if (gap2 > 0) {
                    // ５科目の基準がみたしていない場合
                    result = "5科目であと " + gap2;
                }
            }
        } else {
            // 中１・２の場合、中２の基準で計算する
            int gap1 = 37 - numList.get(0);
            int gap2 = 21 - numList.get(2);
            if(gap1 <= 0 && gap2 <= 0) {
                // 基準を満たしている場合
                result = "◯";
            } else {
                // 基準を満たしていない場合
                if(gap1 > 0 && gap2 > 0) {
                    // 両方満たしていない場合
                    result = "9科目であと " + gap1 + " かつ " + "5科目であと " + gap2;
                } else if (gap1 > 0) {
                    // ９科目の基準が満たしていない場合
                    result = "9科目であと " + gap1;
                } else if (gap2 > 0) {
                    // ５科目の基準がみたしていない場合
                    result = "5科目であと " + gap2;
                }
            }
        }
        return result;
    }
}
