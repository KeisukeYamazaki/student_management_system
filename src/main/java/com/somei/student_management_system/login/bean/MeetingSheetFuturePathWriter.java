package com.somei.student_management_system.login.bean;

import com.somei.student_management_system.login.domain.model.FuturePathWithData;
import com.somei.student_management_system.login.domain.model.PrivateHighSchool;
import com.somei.student_management_system.login.domain.model.PublicHighSchool;
import com.somei.student_management_system.login.domain.model.SchoolRecord;
import com.somei.student_management_system.login.domain.model.SessionData;
import com.somei.student_management_system.login.domain.service.HighSchoolService;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class MeetingSheetFuturePathWriter {

    @Autowired
    PoiMethods poiMethods;

    @Autowired
    HighSchoolService highSchoolService;

    @Autowired
    SessionData sessionData;

    @Autowired
    PrivateHighSchoolCalculation privateHighSchoolCalculation;

    /**
     * 中３の進路情報入力メソッド
     *
     * @param sheet          シート
     * @param futurePathData 進路の情報
     * @param allList        全成績のリスト
     * @param grade 学年
     */
    public void writeFuturePath(XSSFSheet sheet, FuturePathWithData futurePathData, List<List<SchoolRecord>> allList, int grade) {

        // セッションから追加の高校を取り出す
        String additionalHighschool = sessionData.getStr1();
        String additionalHighschoo2 = sessionData.getStr2();

        // セッションを空にする
        sessionData.setStr1(null);
        sessionData.setStr2(null);

        // 入力した公立高校の高校番号を格納するリスト
        List<String> publicHighSchoolIds = new ArrayList<>();

        // 入力した私立高校の高校番号を格納するリスト
        List<String> privateHighSchoolIds = new ArrayList<>();

        // 公立高校を入力する行番号のカウンタ。記入の開始行は 22
        int publicRowCount = 22;

        // 私立高校を入力する行番号のカウンタ。記入の開始行は 27
        int privateRowCount = 27;

        // 第一志望から第三志望の記入（私立高校が第一志望から第三志望に含まれていた場合は、私立高校に記載する）
        // 第一志望から第三志望までループを回す
        for (int i = 0; i < 3; i++) {

            // ループの番号によって処理を分岐する（0は第一志望、1は第二志望、2は第三志望）
            switch (i) {
                case 0:
                    if (futurePathData.getFirstChoice() != null) {

                        // 第一志望が空でなかった場合
                        if (Integer.parseInt(futurePathData.getFirstChoice()) < 3500) {
                            // 高校IDが 3500 よりも小さい場合（＝公立高校の場合）
                            // 高校名を取得する
                            String highschoolName = highSchoolService.getPublicHighSchoolOne(futurePathData.getFirstChoice()).getHighschoolName();
                            // 高校へのアクセスを取得する
                            String access = highSchoolService.getPublicHighSchoolOne(futurePathData.getFirstChoice()).getAccess();
                            // 公立高校１箇所目
                            writePublicHighSchool(sheet, highSchoolService.getPublicHighSchoolOne(futurePathData.getFirstChoice()), publicRowCount);
                            // ２箇所目（名前のみ）
                            poiMethods.getCell(sheet, publicRowCount + 13, 0).setCellValue(highschoolName);
                            // ３箇所目（名前、アクセス）
                            poiMethods.getCell(sheet, publicRowCount + 20, 0).setCellValue(highschoolName);
                            poiMethods.getCell(sheet, publicRowCount + 20, 2).setCellValue(access);
                            // 公立高校のIDをリストに格納
                            publicHighSchoolIds.add(futurePathData.getFirstChoice());
                            // カウンタに１を足す
                            publicRowCount++;

                        } else {
                            // 高校IDが 3500 よりも大きい場合（＝私立高校の場合）
                            // 私立高校１箇所目
                            writePrivateHighSchool1(sheet, highSchoolService.getPrivateHighSchoolOne(futurePathData.getFirstChoice()), privateRowCount);
                            // ２箇所目
                            writePrivateHighSchool2(sheet, highSchoolService.getPrivateHighSchoolOne(futurePathData.getFirstChoice()), privateRowCount + 20);
                            // 私立高校のIDをリストに格納
                            privateHighSchoolIds.add(futurePathData.getFirstChoice());
                            // カウンタに１を足す
                            privateRowCount++;
                        }
                    }
                    break;
                case 1:
                    if (futurePathData.getSecondChoice() != null) {

                        // 第二志望が空でなかった場合
                        if (Integer.parseInt(futurePathData.getSecondChoice()) < 3500) {
                            // 高校IDが 3500 よりも小さい場合（＝公立高校の場合）
                            // 高校名を取得する
                            String highschoolName = highSchoolService.getPublicHighSchoolOne(futurePathData.getSecondChoice()).getHighschoolName();
                            // 高校へのアクセスを取得する
                            String access = highSchoolService.getPublicHighSchoolOne(futurePathData.getSecondChoice()).getAccess();
                            // 公立高校１箇所目
                            writePublicHighSchool(sheet, highSchoolService.getPublicHighSchoolOne(futurePathData.getSecondChoice()), publicRowCount);
                            // ２箇所目（名前のみ）
                            poiMethods.getCell(sheet, publicRowCount + 13, 0).setCellValue(highschoolName);
                            // ３箇所目（名前、アクセスのみ）
                            poiMethods.getCell(sheet, publicRowCount + 20, 0).setCellValue(highschoolName);
                            poiMethods.getCell(sheet, publicRowCount + 20, 2).setCellValue(access);
                            // 公立高校のIDをリストに格納
                            publicHighSchoolIds.add(futurePathData.getSecondChoice());
                            // カウンタに１を足す
                            publicRowCount++;

                        } else {
                            // 高校IDが 3500 よりも大きい場合（＝私立高校の場合）
                            // 私立高校１箇所目
                            writePrivateHighSchool1(sheet, highSchoolService.getPrivateHighSchoolOne(futurePathData.getSecondChoice()), privateRowCount);
                            // ２箇所目（名前のみ）
                            writePrivateHighSchool2(sheet, highSchoolService.getPrivateHighSchoolOne(futurePathData.getSecondChoice()), privateRowCount + 20);
                            // 私立高校のIDをリストに格納
                            privateHighSchoolIds.add(futurePathData.getSecondChoice());
                            // カウンタに１を足す
                            privateRowCount++;
                        }
                    }
                    break;
                case 2:
                    if (futurePathData.getThirdChoice() != null) {

                        // 第三志望が空でなかった場合
                        if (Integer.parseInt(futurePathData.getThirdChoice()) < 3500) {
                            // 高校IDが 3500 よりも小さい場合（＝公立高校の場合）
                            // 高校名を取得する
                            String highschoolName = highSchoolService.getPublicHighSchoolOne(futurePathData.getThirdChoice()).getHighschoolName();
                            // 高校へのアクセスを取得する
                            String access = highSchoolService.getPublicHighSchoolOne(futurePathData.getThirdChoice()).getAccess();
                            // 公立高校１箇所目
                            writePublicHighSchool(sheet, highSchoolService.getPublicHighSchoolOne(futurePathData.getThirdChoice()), publicRowCount);
                            // ２箇所目（名前のみ）
                            poiMethods.getCell(sheet, publicRowCount + 13, 0).setCellValue(highschoolName);
                            // ３箇所目（名前、アクセス）
                            poiMethods.getCell(sheet, publicRowCount + 20, 0).setCellValue(highschoolName);
                            poiMethods.getCell(sheet, publicRowCount + 20, 2).setCellValue(access);
                            // 公立高校のIDをリストに格納
                            publicHighSchoolIds.add(futurePathData.getThirdChoice());
                            // カウンタに１を足す
                            publicRowCount++;

                        } else {
                            // 高校IDが 3500 よりも大きい場合（＝私立高校の場合）
                            // 私立高校１箇所目
                            writePrivateHighSchool1(sheet, highSchoolService.getPrivateHighSchoolOne(futurePathData.getThirdChoice()), privateRowCount);
                            // ２箇所目（名前のみ）
                            writePrivateHighSchool2(sheet, highSchoolService.getPrivateHighSchoolOne(futurePathData.getThirdChoice()), privateRowCount + 20);
                            // 私立高校のIDをリストに格納
                            privateHighSchoolIds.add(futurePathData.getThirdChoice());
                            // カウンタに１を足す
                            privateRowCount++;
                        }
                    }
                    break;
            }
        }

        // 公立の追加の記載
        // 公立高校リストの取得
        List<PublicHighSchool> publicHighSchoolList = getPublicHighschoolList(futurePathData);
        if (publicHighSchoolList.size() != 0) {
            // 戻されたリストが空でなかった場合、ループを回して処理する
            for (int i = 0; i < publicHighSchoolList.size(); i++) {
                // 24行目を超えたら処理を終了する
                if (publicRowCount <= 24) {
                    // 高校名の取得
                    String highschoolName = highSchoolService.getPublicHighSchoolOne(publicHighSchoolList.get(i).getHighschoolId()).getHighschoolName();
                    // 高校へのアクセスを取得する
                    String access = highSchoolService.getPublicHighSchoolOne(publicHighSchoolList.get(i).getHighschoolId()).getAccess();
                    // 高校IDの取得
                    String highschoolId = highSchoolService.getPublicHighSchoolOne(publicHighSchoolList.get(i).getHighschoolId()).getHighschoolId();
                    // 公立高校１箇所目
                    writePublicHighSchool(sheet, publicHighSchoolList.get(i), publicRowCount);
                    // 公立高校２箇所目（名前のみ）
                    poiMethods.getCell(sheet, publicRowCount + 13, 0).setCellValue(highschoolName);
                    // 公立高校３箇所目（名前のみ）
                    poiMethods.getCell(sheet, publicRowCount + 20, 0).setCellValue(highschoolName);
                    poiMethods.getCell(sheet, publicRowCount + 20, 2).setCellValue(access);
                    // 公立高校のIDをリストに格納
                    publicHighSchoolIds.add(highschoolId);
                    // カウンタに１を足す
                    publicRowCount++;
                } else {
                    break;
                }
            }
        }

        // 私立高校の記載
        // 私立高校リストの取得
        List<PrivateHighSchool> privateHighSchoolList = getPrivateHighschoolList(futurePathData);

        // 追加の私立高校1をリストに加える
        if (additionalHighschool != null) {
            // 空の文字列でなければ、高校をリストに追加する
            privateHighSchoolList.add(highSchoolService.getPrivateHighSchoolOne(additionalHighschool));
        }

        // 追加の私立高校2をリストに加える
        if (additionalHighschoo2 != null) {
            // 空の文字列でなければ、高校をリストに追加する
            privateHighSchoolList.add(highSchoolService.getPrivateHighSchoolOne(additionalHighschoo2));
        }

        if (privateHighSchoolList.size() != 0) {

            // リストが空でなかった場合、ループを回して処理する
            for (int i = 0; i < privateHighSchoolList.size(); i++) {

                // 31行目を超えたら処理を終了する
                if (privateRowCount <= 31) {
                    // 高校のデータが存在しない場合は NullPointerException が発生する
                    try {
                        // 高校IDを取得
                        String highschoolId = highSchoolService.getPrivateHighSchoolOne(privateHighSchoolList.get(i).getHighschoolId()).getHighschoolId();
                        // 私立高校１箇所目
                        writePrivateHighSchool1(sheet, privateHighSchoolList.get(i), privateRowCount);
                        // 私立高校２箇所目（名前のみ）
                        writePrivateHighSchool2(sheet, privateHighSchoolList.get(i), privateRowCount + 20);
                        // 私立高校のIDをリストに格納
                        privateHighSchoolIds.add(highschoolId);
                        // カウンタに１を足す
                        privateRowCount++;
                    } catch (NullPointerException e) {
                        // 何もせずに飛ばす
                    }
                } else {
                    break;
                }
            }
        }

        // 公立高校の平均内申までに必要な数値、中位合格数点数、ボーダー点数を入力する
        publicHighSchoolCalculation(sheet, publicHighSchoolIds, allList, grade);

        // 私立高校の合否の結果を取得
        List<String> privateHighSchoolResultList
                = privateHighSchoolCalculation.privateHighSchoolCalculation(futurePathData.getStudentId(), privateHighSchoolIds);

        // 私立高校の合否の結果を入力する
        int startCol = 27;
        for (int i = 0; i < privateHighSchoolResultList.size(); i++) {
            poiMethods.getCell(sheet, startCol + i, 13).setCellValue(privateHighSchoolResultList.get(i));
        }
    }

    /**
     * 中１の進路情報入力メソッド
     *
     * @param sheet          シート
     * @param futurePathData 進路の情報
     * @param allList        全成績のリスト
     */
    public void writeFuturePath1st(XSSFSheet sheet, FuturePathWithData futurePathData, List<List<SchoolRecord>> allList) {

        // セッションから追加の高校を取り出す
        String additionalHighschool = sessionData.getStr1();
        String additionalHighschoo2 = sessionData.getStr2();

        // セッションを空にする
        sessionData.setStr1(null);
        sessionData.setStr2(null);

        // 入力した公立高校の高校番号を格納するリスト
        List<String> publicHighSchoolIds = new ArrayList<>();

        // 入力した私立高校の高校番号を格納するリスト
        List<String> privateHighSchoolIds = new ArrayList<>();

        // 公立高校を入力する行番号のカウンタ。記入の開始行は 20
        int publicRowCount = 20;

        // 私立高校を入力する行番号のカウンタ。記入の開始行は 27
        int privateRowCount = 27;

        // 第一志望から第三志望の記入（私立高校が第一志望から第三志望に含まれていた場合は、私立高校に記載する）
        // 第一志望から第三志望までループを回す
        for (int i = 0; i < 3; i++) {

            // ループの番号によって処理を分岐する（0は第一志望、1は第二志望、2は第三志望）
            switch (i) {
                case 0:
                    if (futurePathData.getFirstChoice() != null) {

                        // 第一志望が空でなかった場合
                        if (Integer.parseInt(futurePathData.getFirstChoice()) < 3500) {
                            // 高校IDが 3500 よりも小さい場合（＝公立高校の場合）
                            // 高校名を取得する
                            String highschoolName = highSchoolService.getPublicHighSchoolOne(futurePathData.getFirstChoice()).getHighschoolName();
                            // 公立高校１箇所目
                            writePublicHighSchool1st(sheet, highSchoolService.getPublicHighSchoolOne(futurePathData.getFirstChoice()), publicRowCount);
                            // ２箇所目（名前のみ）
                            poiMethods.getCell(sheet, publicRowCount + 15, 0).setCellValue(highschoolName);
                            // ３箇所目（名前のみ）
                            poiMethods.getCell(sheet, publicRowCount + 22, 0).setCellValue(highschoolName);
                            // 公立高校のIDをリストに格納
                            publicHighSchoolIds.add(futurePathData.getFirstChoice());
                            // カウンタに１を足す
                            publicRowCount++;

                        } else {
                            // 高校IDが 3500 よりも大きい場合（＝私立高校の場合）
                            // 高校名を取得する
                            String highschoolName = highSchoolService.getPrivateHighSchoolOne(futurePathData.getFirstChoice()).getHighschoolName();
                            // 私立高校１箇所目
                            writePrivateHighSchool1(sheet, highSchoolService.getPrivateHighSchoolOne(futurePathData.getFirstChoice()), privateRowCount);
                            // ２箇所目
                            writePrivateHighSchool2(sheet, highSchoolService.getPrivateHighSchoolOne(futurePathData.getFirstChoice()), privateRowCount + 20);
                            // 私立高校のIDをリストに格納
                            privateHighSchoolIds.add(futurePathData.getFirstChoice());
                            // カウンタに１を足す
                            privateRowCount++;
                        }
                    }
                    break;
                case 1:
                    if (futurePathData.getSecondChoice() != null) {
                        // 第二志望が空でなかった場合
                        if (Integer.parseInt(futurePathData.getFirstChoice()) < 3500) {
                            // 高校IDが 3500 よりも小さい場合（＝公立高校の場合）
                            // 高校名を取得する
                            String highschoolName = highSchoolService.getPublicHighSchoolOne(futurePathData.getSecondChoice()).getHighschoolName();
                            // 公立高校１箇所目
                            writePublicHighSchool1st(sheet, highSchoolService.getPublicHighSchoolOne(futurePathData.getSecondChoice()), publicRowCount);
                            // ２箇所目（名前のみ）
                            poiMethods.getCell(sheet, publicRowCount + 15, 0).setCellValue(highschoolName);
                            // ３箇所目（名前のみ）
                            poiMethods.getCell(sheet, publicRowCount + 22, 0).setCellValue(highschoolName);
                            // 公立高校のIDをリストに格納
                            publicHighSchoolIds.add(futurePathData.getSecondChoice());
                            // カウンタに１を足す
                            publicRowCount++;

                        } else {
                            // 高校IDが 3500 よりも大きい場合（＝私立高校の場合）
                            // 高校名を取得する
                            String highschoolName = highSchoolService.getPrivateHighSchoolOne(futurePathData.getSecondChoice()).getHighschoolName();
                            // 私立高校１箇所目
                            writePrivateHighSchool1(sheet, highSchoolService.getPrivateHighSchoolOne(futurePathData.getSecondChoice()), privateRowCount);
                            // ２箇所目
                            writePrivateHighSchool2(sheet, highSchoolService.getPrivateHighSchoolOne(futurePathData.getSecondChoice()), privateRowCount + 20);
                            // 私立高校のIDをリストに格納
                            privateHighSchoolIds.add(futurePathData.getSecondChoice());
                            // カウンタに１を足す
                            privateRowCount++;
                        }


                    }
                    break;
                case 2:
                    if (futurePathData.getThirdChoice() != null) {
                        if (futurePathData.getSecondChoice() != null) {
                            // 第三志望が空でなかった場合
                            if (Integer.parseInt(futurePathData.getThirdChoice()) < 3500) {
                                // 高校IDが 3500 よりも小さい場合（＝公立高校の場合）
                                // 高校名を取得する
                                String highschoolName = highSchoolService.getPublicHighSchoolOne(futurePathData.getThirdChoice()).getHighschoolName();
                                // 公立高校１箇所目
                                writePublicHighSchool1st(sheet, highSchoolService.getPublicHighSchoolOne(futurePathData.getThirdChoice()), publicRowCount);
                                // ２箇所目（名前のみ）
                                poiMethods.getCell(sheet, publicRowCount + 15, 0).setCellValue(highschoolName);
                                // ３箇所目（名前のみ）
                                poiMethods.getCell(sheet, publicRowCount + 22, 0).setCellValue(highschoolName);
                                // 公立高校のIDをリストに格納
                                publicHighSchoolIds.add(futurePathData.getThirdChoice());
                                // カウンタに１を足す
                                publicRowCount++;

                            } else {
                                // 高校IDが 3500 よりも大きい場合（＝私立高校の場合）
                                // 高校名を取得する
                                String highschoolName = highSchoolService.getPrivateHighSchoolOne(futurePathData.getThirdChoice()).getHighschoolName();
                                // 私立高校１箇所目
                                writePrivateHighSchool1(sheet, highSchoolService.getPrivateHighSchoolOne(futurePathData.getThirdChoice()), privateRowCount);
                                // ２箇所目
                                writePrivateHighSchool2(sheet, highSchoolService.getPrivateHighSchoolOne(futurePathData.getThirdChoice()), privateRowCount + 20);
                                // 私立高校のIDをリストに格納
                                privateHighSchoolIds.add(futurePathData.getThirdChoice());
                                // カウンタに１を足す
                                privateRowCount++;
                            }
                        }
                    }
                    break;
            }
        }

        // 公立の追加の記載
        // 公立高校リストの取得
        List<PublicHighSchool> publicHighSchoolList = getPublicHighschoolList(futurePathData);
        if (publicHighSchoolList.size() != 0) {

            // 戻されたリストが空でなかった場合、ループを回して処理する
            for (int i = 0; i < publicHighSchoolList.size(); i++) {

                // 25行目を超えたら処理を終了する
                if (publicRowCount <= 24) {
                    // 高校名の取得
                    String highschoolName = highSchoolService.getPublicHighSchoolOne(publicHighSchoolList.get(i).getHighschoolId()).getHighschoolName();
                    // 高校IDの取得
                    String highschoolId = highSchoolService.getPublicHighSchoolOne(publicHighSchoolList.get(i).getHighschoolId()).getHighschoolId();
                    // 公立高校１箇所目
                    writePublicHighSchool1st(sheet, publicHighSchoolList.get(i), publicRowCount);
                    // 公立高校２箇所目（名前のみ）
                    poiMethods.getCell(sheet, publicRowCount + 15, 0).setCellValue(highschoolName);
                    // 公立高校３箇所目（名前のみ）
                    poiMethods.getCell(sheet, publicRowCount + 22, 0).setCellValue(highschoolName);
                    // 公立高校のIDをリストに格納
                    publicHighSchoolIds.add(highschoolId);
                    // カウンタに１を足す
                    publicRowCount++;
                } else {
                    break;
                }
            }
        }

        // 私立高校の記載
        // 私立高校リストの取得
        List<PrivateHighSchool> privateHighSchoolList = getPrivateHighschoolList(futurePathData);

        // 追加の私立高校1をリストに加える
        if (additionalHighschool != null) {
            // 空の文字列でなければ、高校をリストに追加する
            privateHighSchoolList.add(highSchoolService.getPrivateHighSchoolOne(additionalHighschool));
        }

        // 追加の私立高校2をリストに加える
        if (additionalHighschoo2 != null) {
            // 空の文字列でなければ、高校をリストに追加する
            privateHighSchoolList.add(highSchoolService.getPrivateHighSchoolOne(additionalHighschoo2));
        }

        if (privateHighSchoolList.size() != 0) {

            // リストが空でなかった場合、ループを回して処理する
            for (int i = 0; i < privateHighSchoolList.size(); i++) {

                // 32行目を超えたら処理を終了する
                if (privateRowCount <= 31) {
                    // 高校のデータが存在しない場合は NullPointerException が発生する
                    try {
                        // 高校名を取得
                        String highschoolName = highSchoolService.getPrivateHighSchoolOne(privateHighSchoolList.get(i).getHighschoolId()).getHighschoolName();
                        // 高校IDを取得
                        String highschoolId = highSchoolService.getPrivateHighSchoolOne(privateHighSchoolList.get(i).getHighschoolId()).getHighschoolId();
                        // 私立高校１箇所目
                        writePrivateHighSchool1(sheet, privateHighSchoolList.get(i), privateRowCount);
                        // 私立高校２箇所目（名前のみ）
                        writePrivateHighSchool2(sheet, privateHighSchoolList.get(i), privateRowCount + 25);
                        // 私立高校のIDをリストに格納
                        privateHighSchoolIds.add(highschoolId);
                        // カウンタに１を足す
                        privateRowCount++;
                    } catch (NullPointerException e) {
                        // 何もせずに飛ばす
                    }
                } else {
                    break;
                }
            }
        }
        // 公立高校の平均内申までに必要な数値、中位合格数点数、ボーダー点数を入力する
        publicHighSchoolCalculation(sheet, publicHighSchoolIds, allList, 1);
    }

    /**
     * 中２・中３公立高校データの入力のメソッド
     *
     * @param sheet            シート
     * @param publicHighSchool 入力する公立高校のデータ
     * @param row              成績を入力する行
     */
    private void writePublicHighSchool(XSSFSheet sheet, PublicHighSchool publicHighSchool, int row) {

        poiMethods.getCell(sheet, row, 0).setCellValue((publicHighSchool.getHighschoolName()));
        poiMethods.getCell(sheet, row, 2).setCellValue((publicHighSchool.getRatio()));
        // 以下、データを double に変換して入力する。変換できなければそのまま入力する
        try {
            poiMethods.getCell(sheet, row, 4).setCellValue(Double.parseDouble(publicHighSchool.getSchoolReportThisyear()));
        } catch (RuntimeException e) {
            poiMethods.getCell(sheet, row, 4).setCellValue(publicHighSchool.getSchoolReportThisyear());
        }
        try {
            poiMethods.getCell(sheet, row, 5).setCellValue(Double.parseDouble(publicHighSchool.getSchoolReportLastyear()));
        } catch (RuntimeException e) {
            poiMethods.getCell(sheet, row, 5).setCellValue(publicHighSchool.getSchoolReportLastyear());
        }
        try {
            poiMethods.getCell(sheet, row, 6).setCellValue(Double.parseDouble(publicHighSchool.getSchoolReportThreeyearsago()));
        } catch (RuntimeException e) {
            poiMethods.getCell(sheet, row, 6).setCellValue(publicHighSchool.getSchoolReportThreeyearsago());
        }
        try {
            poiMethods.getCell(sheet, row, 7).setCellValue(Double.parseDouble(publicHighSchool.getExamScoreThisyear()));
        } catch (RuntimeException e) {
            poiMethods.getCell(sheet, row, 7).setCellValue(publicHighSchool.getExamScoreThisyear());
        }
        try {
            poiMethods.getCell(sheet, row, 8).setCellValue(Double.parseDouble(publicHighSchool.getExamScoreLastyear()));
        } catch (RuntimeException e) {
            poiMethods.getCell(sheet, row, 8).setCellValue(publicHighSchool.getExamScoreLastyear());
        }
        try {
            poiMethods.getCell(sheet, row, 9).setCellValue(Double.parseDouble(publicHighSchool.getExamScoreThreeyearago()));
        } catch (RuntimeException e) {
            poiMethods.getCell(sheet, row, 9).setCellValue(publicHighSchool.getExamScoreThreeyearago());
        }
        try {
            poiMethods.getCell(sheet, row, 10).setCellValue(Double.parseDouble(publicHighSchool.getSScoreThisyear()));
        } catch (RuntimeException e) {
            poiMethods.getCell(sheet, row, 10).setCellValue(publicHighSchool.getSScoreThisyear());
        }
        try {
            poiMethods.getCell(sheet, row, 11).setCellValue(Double.parseDouble(publicHighSchool.getSScoreLastyear()));
        } catch (RuntimeException e) {
            poiMethods.getCell(sheet, row, 11).setCellValue(publicHighSchool.getSScoreLastyear());
        }
        try {
            poiMethods.getCell(sheet, row, 12).setCellValue(Double.parseDouble(publicHighSchool.getSScoreThreeyearsago()));
        } catch (RuntimeException e) {
            poiMethods.getCell(sheet, row, 12).setCellValue(publicHighSchool.getSScoreThreeyearsago());
        }
        try {
            // ボーダーが「-1（算出不能）」の場合は「-」を入力する
            if(publicHighSchool.getBorderThisyear().equals("-1")) {
                poiMethods.getCell(sheet, row, 13).setCellValue("-");
            } else {
                poiMethods.getCell(sheet, row, 13).setCellValue(Double.parseDouble(publicHighSchool.getBorderThisyear()));
            }
        } catch (RuntimeException e) {
            poiMethods.getCell(sheet, row, 13).setCellValue(publicHighSchool.getBorderThisyear());
        }
        try {
            // ボーダーが「-1（算出不能）」の場合は「-」を入力する
            if(publicHighSchool.getBoderLastyear().equals("-1")) {
                poiMethods.getCell(sheet, row, 14).setCellValue("-");
            } else {
                poiMethods.getCell(sheet, row, 14).setCellValue(Double.parseDouble(publicHighSchool.getBoderLastyear()));
            }
        } catch (RuntimeException e) {
            poiMethods.getCell(sheet, row, 14).setCellValue(publicHighSchool.getBoderLastyear());
        }
        try {
            // ボーダーが「-1（算出不能）」の場合は「-」を入力する
            if(publicHighSchool.getBorderThreeyearsago().equals("-1")) {
                poiMethods.getCell(sheet, row, 15).setCellValue("-");
            } else {
                poiMethods.getCell(sheet, row, 15).setCellValue(Double.parseDouble(publicHighSchool.getBorderThreeyearsago()));
            }
        } catch (RuntimeException e) {
            poiMethods.getCell(sheet, row, 15).setCellValue(publicHighSchool.getBorderThreeyearsago());
        }
    }

    /**
     * 中１公立高校データの入力のメソッド
     *
     * @param sheet            シート
     * @param publicHighSchool 入力する公立高校のデータ
     * @param row              成績を入力する行
     */
    private void writePublicHighSchool1st(XSSFSheet sheet, PublicHighSchool publicHighSchool, int row) {

        poiMethods.getCell(sheet, row, 0).setCellValue((publicHighSchool.getHighschoolName()));
        poiMethods.getCell(sheet, row, 2).setCellValue((publicHighSchool.getRatio()));

        // 平均内申の算出・入力
        poiMethods.getCell(sheet, row, 8).setCellValue((int)Math.ceil(getHighschoolAverage(publicHighSchool, 1)));

        // 平均得点の算出・入力
        poiMethods.getCell(sheet, row, 14).setCellValue((int)Math.ceil(getHighschoolAverage(publicHighSchool, 2)));
    }

    /**
     * 私立高校データの入力のメソッド１（名前、学科・コース、基準）
     *
     * @param sheet             シート
     * @param privateHighSchool 入力する私立高校のデータ
     * @param row               成績を入力する行
     */
    private void writePrivateHighSchool1(XSSFSheet sheet, PrivateHighSchool privateHighSchool, int row) {

        poiMethods.getCell(sheet, row, 0).setCellValue(privateHighSchool.getHighschoolName());
        poiMethods.getCell(sheet, row, 2).setCellValue(privateHighSchool.getCourse());
        poiMethods.getCell(sheet, row, 5).setCellValue(privateHighSchool.getStandard());
    }

    /**
     * 私立高校データの入力のメソッド２（名前、アクセス、イベント） ※ イベントはまだない
     *
     * @param sheet             シート
     * @param privateHighSchool 入力する私立高校のデータ
     * @param row               成績を入力する行
     */
    private void writePrivateHighSchool2(XSSFSheet sheet, PrivateHighSchool privateHighSchool, int row) {

        poiMethods.getCell(sheet, row, 0).setCellValue(privateHighSchool.getHighschoolName());
        poiMethods.getCell(sheet, row, 2).setCellValue(privateHighSchool.getAccess());
    }

    /**
     * 公立高校データのリスト化のメソッド
     *
     * @param futurePathData 進路データ
     * @return 公立高校のリスト
     * </br> 空のリストが戻される可能性がある
     */
    private List<PublicHighSchool> getPublicHighschoolList(FuturePathWithData futurePathData) {

        // 公立高校を格納するリストを作成
        List<PublicHighSchool> publicHighSchoolList = new ArrayList<>();

        // リストに公立高校1のデータを加える
        if (futurePathData.getPublicSchool1() != null) {
            publicHighSchoolList.add(highSchoolService.getPublicHighSchoolOne(futurePathData.getPublicSchool1()));
        }
        // リストに公立高校2のデータを加える
        if (futurePathData.getPublicSchool2() != null) {
            publicHighSchoolList.add(highSchoolService.getPublicHighSchoolOne(futurePathData.getPublicSchool2()));
        }

        // リストに公立高校3のデータを加える
        if (futurePathData.getPublicSchool3() != null) {
            publicHighSchoolList.add(highSchoolService.getPublicHighSchoolOne(futurePathData.getPublicSchool3()));
        }

        // リストを戻す
        return publicHighSchoolList;
    }

    /**
     * 私立高校データのリスト化のメソッド
     *
     * @param futurePathData 進路データ
     * @return 私立高校のリスト
     * </br> 空のリストが戻される可能性がある
     */
    private List<PrivateHighSchool> getPrivateHighschoolList(FuturePathWithData futurePathData) {

        // 私立高校の場合、私立高校を格納するリストを作成
        List<PrivateHighSchool> privateHighSchoolList = new ArrayList<>();

        // リストに私立高校1のデータを加える
        if (futurePathData.getPrivateSchool1() != null) {
            privateHighSchoolList.add(highSchoolService.getPrivateHighSchoolOne(futurePathData.getPrivateSchool1()));
        }

        // リストに私立高校2のデータを加える
        if (futurePathData.getPrivateSchool2() != null) {
            privateHighSchoolList.add(highSchoolService.getPrivateHighSchoolOne(futurePathData.getPrivateSchool2()));
        }

        // リストに私立高校3のデータを加える
        if (futurePathData.getPrivateSchool3() != null) {
            privateHighSchoolList.add(highSchoolService.getPrivateHighSchoolOne(futurePathData.getPrivateSchool3()));
        }

        // リストを戻す
        return privateHighSchoolList;
    }

    /**
     * 公立高校の数値算出メソッド
     *
     * @param sheet            シート
     * @param highschoolIdList 高校のIDが格納されたリスト
     * @param allList          全成績のリスト
     */
    private void publicHighSchoolCalculation(XSSFSheet sheet, List<String> highschoolIdList, List<List<SchoolRecord>> allList, int grade) {

        for (int i = 0; i < highschoolIdList.size(); i++) {

            // 平均内申まで必要な成績の変数を宣言
            int recordNeed;

            // 目標点の変数を宣言
            int targetScore;

            // ボーダー点数の変数を宣言
            int borderScore;

            // 公立高校を取得
            PublicHighSchool publicHighSchool = highSchoolService.getPublicHighSchoolOne(highschoolIdList.get(i));

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

            // 平均内申を算出
            double averageRecord = getAverage(schoolReports);
            if (get2ndGradeRecordSum(allList) != 0) {
                // ２年の最終成績がある場合
                recordNeed = (int) Math.ceil((averageRecord - get2ndGradeRecordSum(allList)) / 2 - getNewestRecordSum(allList));
            } else {
                // ２年の最終成績がない場合
                recordNeed = (int) Math.ceil((averageRecord - getNewestRecordSum(allList)) / 2 - getNewestRecordSum(allList));
            }
            if (recordNeed > 0) {
                // ０より大きい場合（足りていない場合）
                poiMethods.getCell(sheet, i + 35, 2).setCellValue("あと " + recordNeed);
            } else {
                // ０より小さい場合（足りている場合）
                poiMethods.getCell(sheet, i + 35, 2).setCellValue("○");
            }

            // 中位合格の点数を算出
            // af+bg(S値)を double に変換してリストに格納する
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
            // // af+bg(S値) の平均を算出
            double averageSscore = getAverage(sScoreList);
            // A値を取得
            double aScore = getAScore(allList);
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
                // ボーダーの平均を算出
                double averageBorder = getAverage(borderScoreList);
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
     * リスト内の数値の平均を算出するメソッド
     *
     * @param list Double型の数値リスト
     * @return リスト内の平均数値（double型）
     */
    private double getAverage(List<Double> list) {
        if (list.size() != 0) {
            double sum = 0;
            // -1(不明)の数を数える変数
            int minusCount = 0;
            for (double num : list) {
                if(num == -1) {
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
     * 高校の合格者の平均内申、平均得点、S値平均、ボーダー平均を算出するメソッド
     *
     * @param publicHighSchool 数値を算出する高校
     * @param typeNum 平均内申（1)、平均得点（2)、S値平均（3)、ボーダー平均（4) の中で求める数値の番号
     * @return いずれかの数値（double型）
     */
    private double getHighschoolAverage(PublicHighSchool publicHighSchool, int typeNum) {

        // 数値格納用のリスト
        List<Double> NumList = new ArrayList<>();
        switch (typeNum) {
            case 1:
                // 平均内申の場合
                try {
                    NumList.add(Double.parseDouble(publicHighSchool.getSchoolReportThisyear()));
                } catch (RuntimeException e) {
                    // 何もせずに飛ばす
                }
                try {
                    NumList.add(Double.parseDouble(publicHighSchool.getSchoolReportLastyear()));
                } catch (RuntimeException e) {
                    // 何もせずに飛ばす
                }
                try {
                    NumList.add(Double.parseDouble(publicHighSchool.getSchoolReportThreeyearsago()));
                } catch (RuntimeException e) {
                    // 何もせずに飛ばす
                }
                break;
            case 2:
                // 平均得点の場合
                try {
                    NumList.add(Double.parseDouble(publicHighSchool.getExamScoreThisyear()));
                } catch (RuntimeException e) {
                    // 何もせずに飛ばす
                }
                try {
                    NumList.add(Double.parseDouble(publicHighSchool.getExamScoreLastyear()));
                } catch (RuntimeException e) {
                    // 何もせずに飛ばす
                }
                try {
                    NumList.add(Double.parseDouble(publicHighSchool.getExamScoreThreeyearago()));
                } catch (RuntimeException e) {
                    // 何もせずに飛ばす
                }
                break;
            case 3:
                // S値平均の場合
                try {
                    NumList.add(Double.parseDouble(publicHighSchool.getSScoreThisyear()));
                } catch (RuntimeException e) {
                    // 何もせずに飛ばす
                }
                try {
                    NumList.add(Double.parseDouble(publicHighSchool.getSScoreLastyear()));
                } catch (RuntimeException e) {
                    // 何もせずに飛ばす
                }
                try {
                    NumList.add(Double.parseDouble(publicHighSchool.getSScoreThreeyearsago()));
                } catch (RuntimeException e) {
                    // 何もせずに飛ばす
                }
                break;
            case 4:
                // ボーダー平均の場合
                try {
                    NumList.add(Double.parseDouble(publicHighSchool.getBorderThisyear()));
                } catch (RuntimeException e) {
                    // 何もせずに飛ばす
                }
                try {
                    NumList.add(Double.parseDouble(publicHighSchool.getBoderLastyear()));
                } catch (RuntimeException e) {
                    // 何もせずに飛ばす
                }
                try {
                    NumList.add(Double.parseDouble(publicHighSchool.getBorderThreeyearsago()));
                } catch (RuntimeException e) {
                    // 何もせずに飛ばす
                }
                break;
        }
        // // af+bg(S値) の平均を算出
        return getAverage(NumList);

    }

    /**
     * 全成績のリストから２年の３学期の成績の９科合計を返すメソッド
     *
     * @param allList 全成績のリスト
     * @return ２年の３学期の成績の９科合計（なければ 0 を返す）
     */
    private int get2ndGradeRecordSum(List<List<SchoolRecord>> allList) {
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
     * 全成績のリストから最新の成績の９科合計を返すメソッド
     *
     * @param allList 全成績のリスト
     * @return 最新の成績の９科合計（なければ 0 を返す）
     */
    private int getNewestRecordSum(List<List<SchoolRecord>> allList) {
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
     * A値を算出するメソッド
     *
     * @param allList 全成績のリスト
     * @return
     */
    private double getAScore(List<List<SchoolRecord>> allList) {
        // 返却用の変数を初期化
        double aScore = 0.0;
        if (get2ndGradeRecordSum(allList) != 0) {
            // 中２の３学期の成績がある場合
            double newestRecord = getNewestRecordSum(allList);
            double secondRecord = get2ndGradeRecordSum(allList);
            aScore = ((newestRecord * 2 + secondRecord) / 135) * 100;
        } else {
            // 中２の３学期の成績がない場合
            aScore = (((double)getNewestRecordSum(allList) * 3) / 135) * 100;
        }
        return aScore;
    }
}
