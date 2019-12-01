package com.somei.student_management_system.login.bean;

import com.somei.student_management_system.login.domain.model.ImportPracticeExam;
import com.somei.student_management_system.login.domain.model.SchoolRecordWithName;
import com.somei.student_management_system.login.domain.model.SessionData;
import com.somei.student_management_system.login.domain.model.Student;
import com.somei.student_management_system.login.domain.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class RecordRegistry {

    @Autowired
    StudentService studentService;

    @Autowired
    SessionData sessionData;

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
}
