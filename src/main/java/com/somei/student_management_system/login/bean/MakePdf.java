package com.somei.student_management_system.login.bean;

import com.somei.student_management_system.login.domain.model.NameList;
import com.somei.student_management_system.login.domain.service.StudentService;
import lombok.val;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class MakePdf {

    @Autowired
    StudentService studentService;

    /**
     * PDF作成(プロジェクト直下に保存)
     *
     * @param homeRoom
     * @return
     */
    public String makePdf(String homeRoom) {

        // ファイル名作成用に日付を取得
        LocalDate data = LocalDate.now();

        // ファイル名を決める
        String filename = DateTimeFormatter.ofPattern("yyyyMMdd").format(data) + homeRoom + ".pdf";

        // 引数で渡されたクラスのリストを取得
        List<NameList> list = studentService.selectManyByHomeRoom(homeRoom);

        // jrxmlファイルのパラメータ指定用
        Map<String, Object> map = new HashMap<>();
        map.put("classRoom", homeRoom);

        // jasperreport用のリストのデータソースを作成
        JRDataSource dataSource = new JRBeanCollectionDataSource(list);

        // 帳票レイアウト
        val report = loadReport();

        try {

            if (report != null) {

                // データソースとパラメータをコンパイルされた帳票に設定
                /** 日本語が表示されない場合は Jaspersoft Studio上で
                 * 日本語が出ないオブジェクトのフォントが
                 *「IPAex」以外になっていないかチェック   */
                JasperPrint pdf = JasperFillManager.fillReport(report, map, dataSource);

                // 出力用のパス・ファイル名を指定する

                //以下の場合はプロジェクトフォルダの直下にPDFが作成される
                File path = new File(filename);

                // 帳票の出力
                JasperExportManager.exportReportToPdfFile(pdf, path.getAbsolutePath());

            } else {

                System.out.println("jrxmlファイルが見つかりませんでした。");
            }

        } catch (JRException e) {

            e.printStackTrace();
            return null;
        }

        // ファイル名を返す
        return filename;

    }

    /**
     * 帳票レイアウトを読み込む
     *
     * @return
     */
    protected final JasperReport loadReport() {
        val resource = new ClassPathResource("jasperreport/name_list.jrxml");

        try {
            val fileName = resource.getFilename();
            if (fileName.endsWith(".jasper")) {
                try (val is = resource.getInputStream()) {
                    return (JasperReport) JRLoader.loadObject(is);
                }
            } else if (fileName.endsWith(".jrxml")) {
                try (val is = resource.getInputStream()) {
                    JasperDesign design = JRXmlLoader.load(is);
                    return JasperCompileManager.compileReport(design);
                }
            } else {
                throw new IllegalArgumentException(
                        ".jasper または .jrxml の帳票を指定してください。 [" + fileName + "] must end in either ");
            }
        } catch (IOException e) {
            throw new IllegalArgumentException("failed to load report. " + resource, e);
        } catch (JRException e) {
            throw new IllegalArgumentException("failed to parse report. " + resource, e);
        }
    }

}


