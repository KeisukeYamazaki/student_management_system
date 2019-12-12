package com.somei.student_management_system.login.bean;

import com.opencsv.CSVReader;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvException;
import com.somei.student_management_system.login.domain.model.IkushinPracticeExam;
import com.somei.student_management_system.login.domain.model.ImportPracticeExam;
import org.springframework.stereotype.Component;

import java.io.Reader;
import java.io.Writer;
import java.util.List;

@Component
public class IOCsv {

    /**
     * CSVファイルの書き込み
     *
     * @param writer
     * @param beans
     */
    public void write(Writer writer, List<ImportPracticeExam> beans) throws CsvException {
        StatefulBeanToCsv<ImportPracticeExam> beanToCsv = new StatefulBeanToCsvBuilder<ImportPracticeExam>(writer).build();
        beanToCsv.write(beans);
    }

    /**
     * CSVファイルの読み取り
     *
     * @param reader
     */
    public List<ImportPracticeExam> read(Reader reader) throws CsvException {
        CsvToBean<ImportPracticeExam> csvToBean = new CsvToBeanBuilder<ImportPracticeExam>(reader)
                .withType(ImportPracticeExam.class).build();
        return csvToBean.parse();
    }

    /**
     * 育伸社CSVファイルの読み取り
     *
     * @param reader
     */
    public List<IkushinPracticeExam> ikushinRead(CSVReader reader) throws CsvException {
        CsvToBean<IkushinPracticeExam> csvToBean = new CsvToBeanBuilder<IkushinPracticeExam>(reader)
                .withType(IkushinPracticeExam.class).build();
        return csvToBean.parse();
    }
}
