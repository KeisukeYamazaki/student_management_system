package com.somei.student_management_system.login.bean;

import com.somei.student_management_system.login.domain.model.SchoolRecordWithName;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class RecordRegistry {

    /**
     * 各種登録画面のGETメソッド.
     *
     * @param srwn SchoolRecordWithName
     * @return List<SchoolRecordWithName>
     */
    public List<SchoolRecordWithName> recordRegistration(SchoolRecordWithName srwn) {

        // 送られてきた値をフィールド別にリストに格納
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
        List<String> sumFiveList = Arrays.asList(srwn.getSumFive().split(",", -1));
        List<String> sumAllList = Arrays.asList(srwn.getSumAll().split(",", -1));

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
            if(englishList.get(i).equals("")) {
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
            inList.setSumFive(sumFiveList.get(i));
            inList.setSumAll(sumAllList.get(i));

            SchoolRecordWithNameList.add(inList);
        }

        return SchoolRecordWithNameList;
    }
}
