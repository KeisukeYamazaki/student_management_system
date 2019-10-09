package com.somei.student_management_system.login.bean;

import com.somei.student_management_system.login.domain.model.FuturePathWithData;
import com.somei.student_management_system.login.domain.model.PracticeExam;
import com.somei.student_management_system.login.domain.model.RegularExam;
import com.somei.student_management_system.login.domain.model.SchoolRecord;
import com.somei.student_management_system.login.domain.model.Student;
import com.somei.student_management_system.login.domain.service.NumericDataService;
import com.somei.student_management_system.login.domain.service.StudentService;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class MeetingSheetWriter {

    @Autowired
    StudentService studentService;

    @Autowired
    NumericDataService numericDataService;

    @Autowired
    MeetingSheetSchoolRecordWriter recordWriter;

    @Autowired
    MeetingSheetPracticeExamWriter practiceExamWriter;

    @Autowired
    MeetingSheetRegularExamWriter regularExamWriter;

    @Autowired
    PoiMethods poiMethods;

    @Autowired
    MeetingSheetFuturePathWriter meetingSheetFuturePathWriter;

    public XSSFWorkbook writeMeetingSheet(Student student, XSSFWorkbook wb, String termName) {

        // 成績一覧の生成（学年別に作成）
        List<SchoolRecord> schoolRecordList = numericDataService.selectRecordOne(student.getStudentId());
        List<SchoolRecord> schoolRecordList_1st = new ArrayList<>();
        List<SchoolRecord> schoolRecordList_2nd = new ArrayList<>();
        List<SchoolRecord> schoolRecordList_3rd = new ArrayList<>();
        for (SchoolRecord record : schoolRecordList) {
            if (record.getGrade().equals("中３")) {
                schoolRecordList_3rd.add(record);
            } else if (record.getGrade().equals("中２")) {
                schoolRecordList_2nd.add(record);
            } else {
                schoolRecordList_1st.add(record);
            }
        }
        // まとめたリストを作成
        List<List<SchoolRecord>> allRecordList = new ArrayList<>();
        allRecordList.add(schoolRecordList_1st);
        allRecordList.add(schoolRecordList_2nd);
        allRecordList.add(schoolRecordList_3rd);

        // 定期試験一覧の生成（学年別に作成）
        List<RegularExam> regularExamList = numericDataService.selectRegularOne(student.getStudentId());
        List<RegularExam> regularExamList_1st = new ArrayList<>();
        List<RegularExam> regularExamList_2nd = new ArrayList<>();
        List<RegularExam> regularExamList_3rd = new ArrayList<>();
        for (RegularExam rexam : regularExamList) {
            if (rexam.getGrade() == 3) {
                regularExamList_3rd.add(rexam);
            } else if (rexam.getGrade() == 2) {
                regularExamList_2nd.add(rexam);
            } else {
                regularExamList_1st.add(rexam);
            }
        }

        // 模試一覧の生成（学年別に作成）
        List<PracticeExam> practiceExamList = numericDataService.selectPracticeOne(student.getStudentId());
        List<PracticeExam> practiceExamList_1st = new ArrayList<>();
        List<PracticeExam> practiceExamList_2nd = new ArrayList<>();
        List<PracticeExam> practiceExamList_3rd = new ArrayList<>();
        for (PracticeExam pexam : practiceExamList) {
            if (pexam.getGrade() == 3) {
                practiceExamList_3rd.add(pexam);
            } else if (pexam.getGrade() == 2) {
                practiceExamList_2nd.add(pexam);
            } else {
                practiceExamList_1st.add(pexam);
            }
        }

        // 進路データの生成
        FuturePathWithData futurePathData = studentService.selectPathDataOne(student.getStudentId());

        //ブックの再計算を許可
        wb.setForceFormulaRecalculation(true);

        // シートの取得
        XSSFSheet sheet = wb.getSheetAt(0);

        //シートの再計算を許可
        sheet.setForceFormulaRecalculation(true);

        // 学年によって処理を分岐
        if (student.getGrade().equals("中３")) {

            // 学期制によって処理を分岐
            if (termName.equals("３学期制")) {

                // ３学期制の場合
                // 名前の入力
                poiMethods.getCell(sheet, 1, 22).setCellValue(student.getStudentName());

                // 成績の入力
                // 中１の成績の入力
                recordWriter.write1stSchoolRecord3terms(wb, sheet, schoolRecordList_1st, 3);

                // 中２の成績の入力
                recordWriter.write2ndSchoolRecord3terms(wb, sheet, schoolRecordList_2nd, 3);

                // 中３の成績の入力
                recordWriter.write3rdSchoolRecord(wb, sheet, schoolRecordList_3rd, 3);

                // 定期試験結果の入力
                // 中１の定期試験の入力
                regularExamWriter.writeRegularExam3terms(wb, sheet, regularExamList_1st, 3, 1);

                // 中２の定期試験の入力
                regularExamWriter.writeRegularExam3terms(wb, sheet, regularExamList_2nd, 3, 2);

                // 中３の定期試験の入力
                regularExamWriter.writeRegularExam3terms(wb, sheet, regularExamList_3rd, 3, 3);

                // 模試結果の入力
                practiceExamWriter.write2nd3rdPracticeExam(wb, sheet, practiceExamList_2nd, practiceExamList_3rd, 3);

                // 進路情報の入力
                meetingSheetFuturePathWriter.writeFuturePath3rd(wb, sheet, futurePathData, allRecordList);

            } else {

                // ２学期制の場合
                // 名前の入力
                poiMethods.getCell(sheet, 1, 22).setCellValue(student.getStudentName());

                // 成績の入力
                // 中１の成績の入力
                recordWriter.write1stSchoolRecord2terms(wb, sheet, schoolRecordList_1st, 3);

                // 中２の成績の入力
                recordWriter.write2ndSchoolRecord2terms(wb, sheet, schoolRecordList_2nd, 3);

                // 中３の成績の入力
                recordWriter.write3rdSchoolRecord(wb, sheet, schoolRecordList_3rd, 2);

                // 定期試験結果の入力
                // 中１の定期試験の入力
                regularExamWriter.writeRegularExam2terms(wb, sheet, regularExamList_1st, 3, 1);

                // 中２の定期試験の入力
                regularExamWriter.writeRegularExam2terms(wb, sheet, regularExamList_2nd, 3, 2);

                // 中３の定期試験の入力
                regularExamWriter.writeRegularExam2terms(wb, sheet, regularExamList_3rd, 3, 3);

                // 模試結果の入力
                practiceExamWriter.write2nd3rdPracticeExam(wb, sheet, practiceExamList_2nd, practiceExamList_3rd, 2);

                // 進路情報の入力
                meetingSheetFuturePathWriter.writeFuturePath3rd(wb, sheet, futurePathData, allRecordList);

            }

        } else if (student.getGrade().equals("中２")) {

            // 学期制によって処理を分岐
            if (termName.equals("３学期制")) {

                // ３学期制の場合
                // 名前の入力
                poiMethods.getCell(sheet, 1, 22).setCellValue(student.getStudentName());
                // 成績の入力
                // 中１の成績の入力
                recordWriter.write1stSchoolRecord3terms(wb, sheet, schoolRecordList_1st, 2);

                // 中２の成績の入力
                recordWriter.write2ndSchoolRecord3terms(wb, sheet, schoolRecordList_2nd, 2);

                // 定期試験結果の入力
                // 中１の定期試験の入力
                regularExamWriter.writeRegularExam3terms(wb, sheet, regularExamList_1st, 2, 1);

                // 中２の定期試験の入力
                regularExamWriter.writeRegularExam3terms(wb, sheet, regularExamList_2nd, 2, 2);


                // 模試結果の入力
                practiceExamWriter.write2nd3rdPracticeExam(wb, sheet, practiceExamList_1st, practiceExamList_2nd, 3);

            } else {

                // ２学期制の場合
                // 名前の入力
                poiMethods.getCell(sheet, 1, 22).setCellValue(student.getStudentName());

                // 成績の入力
                // 中１の成績の入力
                recordWriter.write1stSchoolRecord2terms(wb, sheet, schoolRecordList_1st, 2);

                // 中２の成績の入力
                recordWriter.write2ndSchoolRecord2terms(wb, sheet, schoolRecordList_2nd, 2);

                // 定期試験結果の入力
                // 中１の定期試験の入力
                regularExamWriter.writeRegularExam2terms(wb, sheet, regularExamList_1st, 2, 1);

                // 中２の定期試験の入力
                regularExamWriter.writeRegularExam2terms(wb, sheet, regularExamList_2nd, 2, 2);

                // 模試結果の入力
                practiceExamWriter.write2nd3rdPracticeExam(wb, sheet, practiceExamList_1st, practiceExamList_2nd, 2);
            }


        } else {

            // 中１の場合
            // 学期制によって処理を分岐
            // 学期制によって処理を分岐
            if (termName.equals("３学期制")) {

                // ３学期制の場合
                // 名前の入力
                poiMethods.getCell(sheet, 1, 22).setCellValue(student.getStudentName());

                // 成績の入力
                recordWriter.write1stSchoolRecord3terms(wb, sheet, schoolRecordList_1st, 1);

                // 定期試験結果の入力
                regularExamWriter.writeRegularExam3terms(wb, sheet, regularExamList_1st, 1, 1);

                // 模試結果の入力
                practiceExamWriter.write1stPracticeExam(wb, sheet, practiceExamList_1st, 3);

            } else {

                // ２学期制の場合
                // 名前の入力
                poiMethods.getCell(sheet, 1, 22).setCellValue(student.getStudentName());

                // 成績の入力
                recordWriter.write1stSchoolRecord2terms(wb, sheet, schoolRecordList_1st, 1);

                // 定期試験結果の入力
                regularExamWriter.writeRegularExam2terms(wb, sheet, regularExamList_1st, 1, 1);

                // 模試結果の入力
                practiceExamWriter.write1stPracticeExam(wb, sheet, practiceExamList_1st, 2);
            }

        }

        return wb;
    }

}
