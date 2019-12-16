package com.somei.student_management_system.login.bean;

import com.somei.student_management_system.MyTestApp1;
import com.somei.student_management_system.login.domain.model.ImportPracticeExam;
import com.somei.student_management_system.login.domain.model.RegularExam;
import com.somei.student_management_system.login.domain.model.SchoolRecordWithName;
import com.somei.student_management_system.login.domain.model.SessionData;
import com.somei.student_management_system.login.domain.model.Student;
import com.somei.student_management_system.login.domain.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.Map.*;

@Component
public class RegistryProcessing {

    @Autowired
    StudentService studentService;

    @Autowired
    SessionData sessionData;

    @Autowired
    MyTestApp1 myTestApp1;

    /**
     * 成績登録メソッド.
     *
     * @param srwn SchoolRecordWithName
     * @return List<SchoolRecordWithName>
     */
    public List<SchoolRecordWithName> recordRegistration(SchoolRecordWithName srwn) {

        // 送られてきた値をフィールド別にリストに格納
        // nullの可能性がある５科目・９科目合計は先に初期化しておく
        List<String> sumFiveList = new ArrayList<>();
        List<String> sumAllList = new ArrayList<>();
        // リスト化していく
        List<String> nameList = Arrays.asList(srwn.getStudentName().split(",", -1));
        List<String> idList = Arrays.asList(srwn.getStudentId().split(",", -1));
        List<String> gradeList = Arrays.asList(srwn.getGrade().split(",", -1));
        List<String> yearList = Arrays.asList(srwn.getRecordYear().split(",", -1));
        List<String> termList = Arrays.asList(srwn.getTermName().split(",", -1));
        List<String> englishList = Arrays.asList(srwn.getEnglish().split(",", -1));
        List<String> mathList = Arrays.asList(srwn.getMath().split(",", -1));
        List<String> japaneseList = Arrays.asList(srwn.getJapanese().split(",", -1));
        List<String> scienceList = Arrays.asList(srwn.getScience().split(",", -1));
        List<String> socialList = Arrays.asList(srwn.getSocialStudies().split(",", -1));
        List<String> musicList = Arrays.asList(srwn.getMusic().split(",", -1));
        List<String> artList = Arrays.asList(srwn.getArt().split(",", -1));
        List<String> peList = Arrays.asList(srwn.getPe().split(",", -1));
        List<String> techHomeList = Arrays.asList(srwn.getTechHome().split(",", -1));
        // ５科目合計がnullなら何もしない
        if (srwn.getSumFive() != null) {
            sumFiveList = Arrays.asList(srwn.getSumFive().split(",", -1));
            sumAllList = Arrays.asList(srwn.getSumAll().split(",", -1));
        }
        // 返却用のリストを作成
        List<SchoolRecordWithName> SchoolRecordWithNameList = new ArrayList<>();

        for (int i = 0; i < nameList.size(); i++) {
            SchoolRecordWithName inList = new SchoolRecordWithName();

            // SchoolRecordWithNameクラスにフィールド別のリストを代入してリストに格納
            inList.setStudentName(nameList.get(i));
            inList.setStudentId(idList.get(i));
            inList.setGrade(gradeList.get(i));
            inList.setRecordYear(yearList.get(i));
            inList.setTermName(termList.get(i));
            // 英語が空ならループを抜ける（成績が登録されていないとみなす）
            if (englishList.get(i).equals("")) {
                continue;
            } else {
                inList.setEnglish(englishList.get(i));
            }
            inList.setMath(mathList.get(i));
            inList.setJapanese(japaneseList.get(i));
            inList.setScience(scienceList.get(i));
            inList.setSocialStudies(socialList.get(i));
            inList.setMusic(musicList.get(i));
            inList.setArt(artList.get(i));
            inList.setPe(peList.get(i));
            inList.setTechHome(techHomeList.get(i));
            // ５科目合計がnullなら何もしない
            if (srwn.getSumFive() != null) {
                inList.setSumFive(sumFiveList.get(i));
                inList.setSumAll(sumAllList.get(i));
            }
            // リストに追加する
            SchoolRecordWithNameList.add(inList);
        }

        return SchoolRecordWithNameList;
    }

    /**
     * 模試結果登録メソッド.
     *
     * @param exam 模擬試験の結果
     * @return List<PracticeExam>
     */
    public List<ImportPracticeExam> practiceRegistration(ImportPracticeExam exam) {

        // 生徒を取得
        Student student = studentService.selectOne(sessionData.getStr3());

        // リスト化していく
        List<String> gradeList = Arrays.asList(exam.getGrade().split(",", -1));
        List<String> examYearList = Arrays.asList(exam.getExamYear().split(",", -1));
        List<String> monthNameList = Arrays.asList(exam.getMonthName().split(",", -1));
        List<String> englishScoreList = Arrays.asList(exam.getEnglishScore().split(",", -1));
        List<String> mathScoreList = Arrays.asList(exam.getMathScore().split(",", -1));
        List<String> japaneseScoreList = Arrays.asList(exam.getJapaneseScore().split(",", -1));
        List<String> scienceScoreList = Arrays.asList(exam.getScienceScore().split(",", -1));
        List<String> socialScoreList = Arrays.asList(exam.getSocialScore().split(",", -1));
        List<String> devThreeList = Arrays.asList(exam.getDevThree().split(",", -1));
        List<String> devFiveList = Arrays.asList(exam.getDevFive().split(",", -1));
        List<String> englishDeviation = Arrays.asList(exam.getEnglishDeviation().split(",", -1));
        List<String> mathDeviation = Arrays.asList(exam.getMathDeviation().split(",", -1));
        List<String> japaneseDeviation = Arrays.asList(exam.getJapaneseDeviation().split(",", -1));
        List<String> scienceDeviation = Arrays.asList(exam.getScienceDeviation().split(",", -1));
        List<String> socialDeviation = Arrays.asList(exam.getSocialDeviation().split(",", -1));

        // 返却用のリストを作成
        List<ImportPracticeExam> practiceExamList = new ArrayList<>();

        for (int i = 0; i < gradeList.size(); i++) {

            ImportPracticeExam inList = new ImportPracticeExam();

            // ３科目の点数のリストを作成
            List<String> threeSubjects
                    = Arrays.asList(englishScoreList.get(i), mathScoreList.get(i), japaneseScoreList.get(i));

            // ５科目の点数のリストを作成
            List<String> fiveSubjects
                    = Arrays.asList(englishScoreList.get(i), mathScoreList.get(i), japaneseScoreList.get(i),
                    scienceScoreList.get(i), socialScoreList.get(i));

            //PracticeExamクラスにフィールド別のリストを代入してリストに格納
            inList.setStudentId(student.getStudentId());
            inList.setGrade(gradeList.get(i));
            inList.setExamYear(examYearList.get(i));
            inList.setMonthName(monthNameList.get(i));
            // 英語が空ならループを抜ける（成績が登録されていないとみなす）
            if (englishScoreList.get(i).equals("")) {
                continue;
            } else {
                inList.setEnglishScore(englishScoreList.get(i));
            }
            inList.setMathScore(mathScoreList.get(i));
            inList.setJapaneseScore(japaneseScoreList.get(i));
            inList.setScienceScore(scienceScoreList.get(i));
            inList.setSocialScore(socialScoreList.get(i));
            inList.setSumThree(String.valueOf(sumSubjects(threeSubjects)));
            inList.setSumAll(String.valueOf(sumSubjects(fiveSubjects)));
            inList.setDevThree(devThreeList.get(i));
            inList.setDevFive(devFiveList.get(i));
            inList.setEnglishDeviation(englishDeviation.get(i));
            inList.setMathDeviation(mathDeviation.get(i));
            inList.setJapaneseDeviation(japaneseDeviation.get(i));
            inList.setScienceDeviation(scienceDeviation.get(i));
            inList.setSocialDeviation(socialDeviation.get(i));

            practiceExamList.add(inList);
        }
        return practiceExamList;
    }

    /**
     * 定期試験結果ペーストデータ登録メソッド
     *
     * @param regulars 定期試験のペーストデータ
     * @return 定期試験の結果リスト
     */
    public Map<List<RegularExam>, List<String>> regularRegistration(String regulars) {
        List<String> list = Arrays.asList(regulars.split("\r\n"));
        List<List<String>> strList = list.stream().map(l -> Arrays.asList(l.split("\t"))).collect(Collectors.toList());

        // データ登録用のリストを作成
        List<RegularExam> regularExamList = new ArrayList<>();

        // 登録できなかった生徒の名前を格納するリストを作成
        List<String> notRegistryList = new ArrayList<>();

        for (int i = 1; i < strList.size(); i++) {
            if (strList.get(i).size() == 0) {
                // 空のリストは飛ばす
                continue;
            } else {
                RegularExam regularExam = new RegularExam();
                Student student = new Student();
                // 生徒情報の取得
                try {
                    student = studentService.selectOne(myTestApp1.getId(strList.get(i).get(1)));
                } catch (EmptyResultDataAccessException e) {
                    notRegistryList.add(strList.get(i).get(1));
                    break;
                }
                // 試験Idをセットする
                if (student.getLocalSchool().contains("東野")) {
                    regularExam.setRegular_id(myTestApp1.searchRegularExamId(strList.get(0).get(3)));
                } else {
                    regularExam.setRegular_id(myTestApp1.searchRegularExamId(strList.get(0).get(2)));
                }
                regularExam.setStudentId(myTestApp1.getId(strList.get(i).get(1)));
                regularExam.setGrade(strList.get(i).get(0));
                regularExam.setExamYear(strList.get(0).get(1));
                regularExam.setEnglish(strList.get(i).get(2));
                regularExam.setMath(strList.get(i).get(3));
                regularExam.setJapanese(strList.get(i).get(4));
                regularExam.setScience(strList.get(i).get(5));
                regularExam.setSocialStudies(strList.get(i).get(6));
                regularExam.setMusic(strList.get(i).get(7));
                regularExam.setArt(strList.get(i).get(8));
                regularExam.setPe(strList.get(i).get(9));
                regularExam.setTech(strList.get(i).get(10));
                regularExam.setHome(strList.get(i).get(11));
                regularExam.setSumFive(strList.get(i).get(12));

                // リストに格納する
                regularExamList.add(regularExam);
            }
        }
        // Mapに格納して返す
        Map<List<RegularExam>, List<String>> returnMap1 = Map.of(regularExamList, notRegistryList);
        /*Map<List<RegularExam>, List<String>> returnMap = new HashMap<List<RegularExam>, List<String>>() {
            {
                put(regularExamList, notRegistryList);
            }
        };*/
        return returnMap1;
    }

    /**
     * 模擬試験ペーストリスト作成メソッド.
     *
     * @param practices 模試の全県サイトからのペーストデータ
     * @param grade     学年
     * @param year      年度
     * @param month     実施月
     * @return 模試登録確認画面へ遷移
     */
    public List<ImportPracticeExam> makePracticeList(String practices,
                                                     String grade,
                                                     String year,
                                                     String month) {

        // １人ずつのリストにする
        List<String> list = Arrays.asList(practices.split("\r\n[0-9]+\t\r\n[0-9]+\r\n"));
        List<List<String>> strList = list.stream()
                .map(l -> new ArrayList<>(Arrays.asList(l.split("\t|\n|\r"))))
                .collect(Collectors.toList());

        // ""は除外する
        List<List<String>> newStrList = new ArrayList<>();
        for (List<String> inList : strList) {
            // E1は" "にする、 "[", "]"は削除する
            inList.stream().map(i -> i.replace("E1", " "));
            // ""は削除する
            inList.removeIf(i -> i.equals(""));

            // 格納する
            newStrList.add(inList);
        }

        // １つめの先頭の要素を２つ削除(1, 1)を削除
        newStrList.get(0).remove(0);
        newStrList.get(0).remove(0);

        // 返却用のリストを作成
        List<ImportPracticeExam> practiceExamList = new ArrayList<>();

        // 志望校が３つの場合の添字
        List<Integer> numList_3schools = Arrays.asList(0, 21, 22, 24, 25, 27, 28, 30, 31, 33, 34, 36, 37, 39, 40);
        // 志望校が２つの場合の添字
        List<Integer> numList_2schools = numList_3schools.stream().map(num -> num -= 1).collect(Collectors.toList());
        // 志望校が１つの場合の添字
        List<Integer> numList_1school = numList_3schools.stream().map(num -> num -= 2).collect(Collectors.toList());

        for (int i = 0; i < newStrList.size(); i++) {
            ImportPracticeExam practiceExam = new ImportPracticeExam();

            // 生徒情報を取得
            Student student = studentService.selectOne(myTestApp1.getId(newStrList.get(i).get(0)));

            // 添字のリストを代入するリスト
            List<Integer> numList = new ArrayList<>();

            if (newStrList.get(i).size() == 40) {
                // 志望校が１つの場合
                numList = numList_1school;
            } else if (newStrList.get(i).size() == 41) {
                // 志望校が２つの場合
                numList = numList_2schools;
            } else {
                // 志望校が３つの場合
                numList = numList_3schools;
            }

            // ImportPracticeExamの各項目をセットする
            practiceExam.setStudentId(student.getStudentId());
            practiceExam.setGrade(grade);
            practiceExam.setExamYear(year);
            practiceExam.setMonthName(month);
            practiceExam.setEnglishScore(newStrList.get(i).get(numList.get(1)));
            practiceExam.setEnglishDeviation(removeBrackets(newStrList.get(i).get(numList.get(2))));
            practiceExam.setMathScore(newStrList.get(i).get(numList.get(3)));
            practiceExam.setMathDeviation(removeBrackets(newStrList.get(i).get(numList.get(4))));
            practiceExam.setJapaneseScore(newStrList.get(i).get(numList.get(5)));
            practiceExam.setJapaneseDeviation(removeBrackets(newStrList.get(i).get(numList.get(6))));
            practiceExam.setScienceScore(newStrList.get(i).get(numList.get(7)));
            practiceExam.setScienceDeviation(removeBrackets(newStrList.get(i).get(numList.get(8))));
            practiceExam.setSocialScore(newStrList.get(i).get(numList.get(9)));
            practiceExam.setSocialDeviation(removeBrackets(newStrList.get(i).get(numList.get(10))));
            practiceExam.setSumThree(newStrList.get(i).get(numList.get(11)));
            practiceExam.setDevThree(removeBrackets(newStrList.get(i).get(numList.get(12))));
            practiceExam.setSumAll(newStrList.get(i).get(numList.get(13)));
            practiceExam.setDevFive(removeBrackets(newStrList.get(i).get(numList.get(14))));

            practiceExamList.add(practiceExam);
        }
        return practiceExamList;
    }

    /**
     * 定期試験結果登録メソッド
     *
     * @param exam 定期試験のデータ
     * @return 定期試験の結果リスト
     */
    public List<RegularExam> regularRegistration(RegularExam exam) {

        // 生徒を取得
        Student student = studentService.selectOne(sessionData.getStr3());

        // リスト化していく
        List<String> gradeList = Arrays.asList(exam.getGrade().split(",", -1));
        List<String> examYearList = Arrays.asList(exam.getExamYear().split(",", -1));
        List<String> regularIdList = Arrays.asList(exam.getRegular_id().split(",", -1));
        List<String> englishList = Arrays.asList(exam.getEnglish().split(",", -1));
        List<String> mathList = Arrays.asList(exam.getMath().split(",", -1));
        List<String> japaneseList = Arrays.asList(exam.getJapanese().split(",", -1));
        List<String> scienceList = Arrays.asList(exam.getScience().split(",", -1));
        List<String> socialStudiesList = Arrays.asList(exam.getSocialStudies().split(",", -1));
        List<String> musicList = Arrays.asList(exam.getMusic().split(",", -1));
        List<String> artList = Arrays.asList(exam.getArt().split(",", -1));
        List<String> peList = Arrays.asList(exam.getPe().split(",", -1));
        List<String> techList = Arrays.asList(exam.getTech().split(",", -1));
        List<String> homeList = Arrays.asList(exam.getHome().split(",", -1));

        // 返却用のリストを作成
        List<RegularExam> regularExamList = new ArrayList<>();

        for (int i = 0; i < gradeList.size(); i++) {

            RegularExam inList = new RegularExam();

            // ５科目の点数のリストを作成
            List<String> fiveSubjects
                    = Arrays.asList(englishList.get(i), mathList.get(i), japaneseList.get(i),
                    scienceList.get(i), socialStudiesList.get(i));

            //PracticeExamクラスにフィールド別のリストを代入してリストに格納
            inList.setStudentId(student.getStudentId());
            inList.setGrade(gradeList.get(i));
            inList.setExamYear(examYearList.get(i));
            inList.setRegular_id(regularIdList.get(i));
            // 英語が空ならループを抜ける（成績が登録されていないとみなす）
            if (englishList.get(i).equals("")) {
                continue;
            } else {
                inList.setEnglish(englishList.get(i));
            }
            inList.setMath(mathList.get(i));
            inList.setJapanese(japaneseList.get(i));
            inList.setScience(scienceList.get(i));
            inList.setSocialStudies(socialStudiesList.get(i));
            inList.setSumFive(String.valueOf(sumSubjects(fiveSubjects)));
            inList.setMusic(musicList.get(i));
            inList.setArt(artList.get(i));
            inList.setPe(peList.get(i));
            inList.setTech(techList.get(i));
            inList.setHome(homeList.get(i));

            regularExamList.add(inList);
        }
        return regularExamList;
    }

    /**
     * リスト内の数値の合計計算メソッド.
     *
     * @param subjects ５科目の成績のリスト
     * @return ５科目の合計
     */
    private int sumSubjects(List<String> subjects) {

        return subjects.stream()
                .mapToInt(Integer::parseInt)
                .sum();
    }

    /**
     * ９科目の合計計算メソッド.
     *
     * @param sumFive      ５科目の成績
     * @param fourSubjects 技能４科目の成績のリスト
     * @return ９科目の合計
     */
    private int sumAll(int sumFive, List<String> fourSubjects) {

        return fourSubjects.stream()
                .mapToInt(Integer::parseInt)
                .sum() + sumFive;
    }

    /**
     * [ ]除去メソッド
     *
     * @param str [ ]付きの文字列
     * @return [ ]を除去した文字列
     */
    private String removeBrackets(String str) {
        String removedStr = str.replace("[", "").trim();
        return removedStr.replace("]", "").trim();
    }
}
