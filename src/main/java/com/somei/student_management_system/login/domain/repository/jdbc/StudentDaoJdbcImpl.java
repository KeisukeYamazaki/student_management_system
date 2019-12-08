package com.somei.student_management_system.login.domain.repository.jdbc;

import com.somei.student_management_system.login.domain.model.FuturePath;
import com.somei.student_management_system.login.domain.model.NameList;
import com.somei.student_management_system.login.domain.model.Student;
import com.somei.student_management_system.login.domain.model.Highschools;
import com.somei.student_management_system.login.domain.repository.StudentDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class StudentDaoJdbcImpl implements StudentDao {

    @Autowired
    private JdbcTemplate jdbc;

    @Override
    // Studentテーブルの件数を取得
    public ArrayList<Integer> count() throws DataAccessException {
        // 全件・各学年の結果を格納するリストを作成
        ArrayList<Integer> counts = new ArrayList<>();

        // 全件取得してカウント
        int count = jdbc.queryForObject("SELECT COUNT(*) FROM student WHERE char_length(student_id) = 4", Integer.class);

        // 全件取得の結果を加える
        counts.add(count);

        // 各学年をカウントしてリストに入れる
        counts.add(jdbc.queryForObject("SELECT COUNT(*) FROM student WHERE grade = '中３' AND char_length(student_id) = 4", Integer.class));
        counts.add(jdbc.queryForObject("SELECT COUNT(*) FROM student WHERE grade = '中２' AND char_length(student_id) = 4", Integer.class));
        counts.add(jdbc.queryForObject("SELECT COUNT(*) FROM student WHERE grade = '中１' AND char_length(student_id) = 4", Integer.class));
        counts.add(jdbc.queryForObject("SELECT COUNT(*) FROM student WHERE grade = '小６' AND char_length(student_id) = 4", Integer.class));
        counts.add(jdbc.queryForObject("SELECT COUNT(*) FROM student WHERE grade = '小５' AND char_length(student_id) = 4", Integer.class));
        counts.add(jdbc.queryForObject("SELECT COUNT(*) FROM student WHERE grade = '小４' AND char_length(student_id) = 4", Integer.class));
        counts.add(jdbc.queryForObject("SELECT COUNT(*) FROM student WHERE grade = '小３' AND char_length(student_id) = 4", Integer.class));

        return counts;
    }

    @Override
    // Studentテーブルにデータを1件insert.
    public int insertOne(Student student) throws DataAccessException {
        //１件登録
        int rowNumber = jdbc.update("INSERT INTO student(student_id,"
                        + " student_name,"
                        + " name_ruby,"
                        + " grade,"
                        + " school,"
                        + " home_room,"
                        + " local_school,"
                        + " entry_time,"
                        + " birthday,"
                        + " club,"
                        + " parents,"
                        + " siblings,"
                        + " address,"
                        + " info)"
                        + " VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
                student.getStudentId(),
                student.getStudentName(),
                student.getNameRuby(),
                student.getGrade(),
                student.getSchool(),
                student.getHomeRoom(),
                student.getLocalSchool(),
                student.getEntryTime(),
                student.getBirthday(),
                student.getClub(),
                student.getParents(),
                student.getSiblings(),
                student.getAddress(),
                student.getInfo());

        return rowNumber;

    }

    //生徒１件取得
    @Override
    public Student selectOne(String studentId) {

        //１件取得用SQL
        String sql = "SELECT * FROM student WHERE student_id = ?";

        //RowMapperの生成
        RowMapper<Student> rowMapper = new BeanPropertyRowMapper<>(Student.class);

        //SQL実行
        return jdbc.queryForObject(sql, rowMapper, studentId);
    }

    //生徒全件取得
    @Override
    public List<Student> selectMany() {

        //studentテーブルのデータを全件取得するSQL
        String sql = "SELECT * FROM student WHERE char_length(student_id) = 4 ORDER BY home_room COLLATE \"C\", student_id ";

        //RowMapperの生成
        RowMapper<Student> rowMapper = new BeanPropertyRowMapper<Student>(Student.class);

        //SQL実行
        return jdbc.query(sql, rowMapper);
    }

    // studentテーブルの指定学年の全データを取得
    @Override
    public List<Student> selectManyByGrade(String grade) throws DataAccessException {

        //studentテーブルの指定学年のデータを全件取得するSQL
        String sql = "SELECT * FROM student  WHERE grade = ? AND char_length(student_id) = 4 ORDER BY home_room COLLATE \"C\", student_id";

        //RowMapperの生成
        RowMapper<Student> rowMapper = new BeanPropertyRowMapper<Student>(Student.class);

        //SQL実行
        return jdbc.query(sql, rowMapper, grade);
    }

    @Override
    public List<NameList> selectManyByHomeRoom(String homeRoom) throws DataAccessException {

        try {

            // クラスが３Ａだった場合（３特と合併クラス）
            if (homeRoom.equals("３Ａ")) {

                // ビューを作成する
                String sqlCreateView = "CREATE VIEW nameList_3A AS"
                        + " SELECT student_id, student_name, home_room FROM student WHERE home_room = '３特'"
                        + " UNION"
                        + " SELECT student_id, student_name, home_room FROM student WHERE home_room = '３Ａ'";

                String sql = "SELECT student_id, student_name, home_room from namelist_3a"
                        + " ORDER BY home_room COLLATE \"C\", student_id";

                //RowMapperの生成
                RowMapper<NameList> rowMapper = new BeanPropertyRowMapper<>(NameList.class);

                //SQL実行
                jdbc.execute(sqlCreateView);
                return jdbc.query(sql, rowMapper);

            } else {

                //studentテーブルの指定クラスのデータを全件取得するSQL
                String sql = "SELECT student_id, student_name, home_room FROM student"
                        + " WHERE home_room = ? AND char_length(student_id) = 4 ORDER BY home_room COLLATE \"C\", student_id";

                //RowMapperの生成
                RowMapper<NameList> rowMapper = new BeanPropertyRowMapper<>(NameList.class);

                //SQL実行
                return jdbc.query(sql, rowMapper, homeRoom);
            }

        } catch (DataAccessException e) {
            e.printStackTrace();

            // もし当該クラスのデータが無かった場合は null を返す
            return null;

        } finally {

            // ビューを削除する
            String sqlDropView = "DROP VIEW IF EXISTS nameList_3A";
            jdbc.execute(sqlDropView);

        }
    }

    @Override
    public List<Student> selectManyByBirthday(String thisMonth, String nextMonth) throws DataAccessException {

        try {

            String sql = "SELECT * FROM student"
                    + " WHERE to_char(birthday, 'FMMM') = ? AND char_length(student_id) = 4"
                    + " OR to_char(birthday, 'FMMM') = ?"
                    + " ORDER BY to_char(birthday, 'MM-dd')";

            //RowMapperの生成
            RowMapper<Student> rowMapper = new BeanPropertyRowMapper<Student>(Student.class);

            //SQL実行
            return jdbc.query(sql, rowMapper, thisMonth, nextMonth);

        } catch (DataAccessException e) {
            e.printStackTrace();

            //データがない場合はnullを返す
            return null;

        }

    }

    @Override
    public FuturePath selectPathOne(String studentId) throws DataAccessException {

        try {

            //future_pathテーブルのデータを取得するSQL
            String sql = "SELECT * FROM future_path WHERE student_id = ? AND char_length(student_id) = 4";

            //RowMapperの生成
            RowMapper<FuturePath> rowMapper = new BeanPropertyRowMapper<>(FuturePath.class);

            //SQL実行
            return jdbc.queryForObject(sql, rowMapper, studentId);

        } catch (DataAccessException e) {

            //データが無かった場合は、nullを返す
            return null;
        }
    }

    // 指定学年の最終ID番号+1を取得
    @Override
    public String selectLastId(String grade) throws DataAccessException {

        String sqlGrade = null;

        switch (grade) {
            case "中３":
                sqlGrade = "22";
                break;
            case "中２":
                sqlGrade = "23";
                break;
            case "中１":
                sqlGrade = "24";
                break;
            case "小６":
                sqlGrade = "25";
                break;
            case "小５":
                sqlGrade = "26";
                break;
            case "小４":
                sqlGrade = "27";
                break;
            case "小３":
                sqlGrade = "28";
                break;
        }

        // ワイルドカード１
        sqlGrade = sqlGrade + "%";
        // ワイルドカード２
        String minusSqlGrade = "-" + sqlGrade;

        try {

            String sql = "SELECT MAX(ABS(to_number(student_id, '9999'))) FROM student WHERE student_id ~~* any(array[?, ?]);";

            //SQLを実行して、その値に１を加え変数に代入

            String id = jdbc.queryForObject(sql, String.class, sqlGrade, minusSqlGrade);

            if(id != null) {

                // idがnullでなければ、Integerに変換して１を加える
                Integer newId = Integer.parseInt(id) + 1;

                // Stringに変換して返す
                return String.valueOf(newId);

            } else {

                // null の場合（＝その学年で１人目の生徒の場合）、sqlGrade に 01 を加えて返す
                return sqlGrade.replace("%", "01");

            }

        } catch (DataAccessException e) {

            //例外が発生したらnullを返す
            return null;
        }
    }

    @Override
    public List<Student> selectIdName() throws DataAccessException {

        String sql = "SELECT student_id, student_name FROM student WHERE char_length(student_id) = 4";

        //RowMapperの生成
        RowMapper<Student> rowMapper = new BeanPropertyRowMapper<>(Student.class);

        //SQL実行
        return jdbc.query(sql, rowMapper);
    }

    // 生徒１件更新
    @Override
    public int updateOne(Student student) throws DataAccessException {
        //１件更新
        int rowNumber = jdbc.update("UPDATE student"
                        + " SET"
                        + " student_name = ?,"
                        + " name_ruby = ?,"
                        + " grade = ?,"
                        + " school = ?,"
                        + " home_room = ?,"
                        + " local_school = ?,"
                        + " entry_time = ?,"
                        + " birthday = ?,"
                        + " club = ?,"
                        + " parents = ?,"
                        + " siblings = ?,"
                        + " address = ?,"
                        + " info = ?"
                        + " WHERE student_id = ?"
                , student.getStudentName()
                , student.getNameRuby()
                , student.getGrade()
                , student.getSchool()
                , student.getHomeRoom()
                , student.getLocalSchool()
                , student.getEntryTime()
                , student.getBirthday()
                , student.getClub()
                , student.getParents()
                , student.getSiblings()
                , student.getAddress()
                , student.getInfo()
                , student.getStudentId());

        return rowNumber;
    }

    @Override
    // studentテーブルのクラスを複数件更新
    public List<Integer> updateHomeRoom(List<Student> list) throws DataAccessException {

        // 結果返却用のリストを生成
        List<Integer> resultList = new ArrayList<>();

        // リストの数の分のupdateを行う
        for (int i = 0; i < list.size(); i++) {

            int rowNumber = jdbc.update("UPDATE student"
                            + " SET"
                            + " home_room = ?"
                            + " WHERE student_id = ?"
                    , list.get(i).getHomeRoom()
                    , list.get(i).getStudentId());

            resultList.add(rowNumber);
        }

        return resultList;

    }

    // 生徒の進路データを１件更新
    @Override
    public int updatePathOne(FuturePath futurePath) throws DataAccessException {
        //１件更新
        int rowNumber = jdbc.update("UPDATE future_path"
                        + " SET"
                        + " first_choice = ?,"
                        + " second_choice = ?,"
                        + " third_choice = ?,"
                        + " public_school1 = ?,"
                        + " public_school2 = ?,"
                        + " public_school3 = ?,"
                        + " private_school1 = ?,"
                        + " private_school2 = ?,"
                        + " private_school3 = ?,"
                        + " information = ?,"
                        + " record_date = current_timestamp AT TIME ZONE 'Asia/Tokyo'"
                        + " WHERE student_id = ?"
                , nullConvert(futurePath.getFirstChoice())
                , nullConvert(futurePath.getSecondChoice())
                , nullConvert(futurePath.getThirdChoice())
                , nullConvert(futurePath.getPublicSchool1())
                , nullConvert(futurePath.getPublicSchool2())
                , nullConvert(futurePath.getPublicSchool3())
                , nullConvert(futurePath.getPrivateSchool1())
                , nullConvert(futurePath.getPrivateSchool2())
                , nullConvert(futurePath.getPrivateSchool3())
                , futurePath.getInformation()
                , futurePath.getStudentId());

        return rowNumber;
    }

    /**
     * 空の値（未選択）の場合 null に変えるメソッド
     *
     * @param str 登録された高校の番号
     * @return if " " -> null, else str
     */
    private String nullConvert(String str) {
        // もし引数が" "ならnullを返す
        // nullであると例外が発生するので、nullでない場合にチェックする
        if(str != null) {
            if (str.equals(" ")) {
                return null;
            } else {
                // そうでなければ値をそのまま返す
                return str;
            }
        }
        return null;
    }

    // future_pathテーブルのデータを１件挿入
    @Override
    public int insertPathOne(String studentId) throws DataAccessException {

        int rowNumber = jdbc.update("INSERT INTO future_path (student_id, record_date) VALUES (?, current_timestamp AT TIME ZONE 'Asia/Tokyo')", studentId);

        return rowNumber;
    }

    // 生徒データ１件削除（IDの前に-をつけて、一覧に表示させないようにする）
    @Override
    public List<Integer> deleteOne(String studentId) throws DataAccessException {

        String deleteId = "-" + studentId ;

        List<Integer> resultList = new ArrayList<>();
        //１件削除
        // student テーブルの処理
        try {
            resultList.add(jdbc.update("UPDATE student SET student_id = ? WHERE student_id = ?", deleteId, studentId));
        } catch (DataAccessException e) {
            System.out.println("studentに " + studentId + " はありませんでした");
        }

        // future_path テーブルの処理
        try {
            resultList.add(jdbc.update("UPDATE future_path SET student_id = ? WHERE student_id = ?", deleteId, studentId));
        } catch (DataAccessException e) {
            System.out.println("future_pathに " + studentId + " はありませんでした");
        }

        // practice_registry テーブルの処理
        try {
            resultList.add(jdbc.update("UPDATE practice_registry SET id = ? WHERE id = ?", deleteId, studentId));
        } catch (DataAccessException e) {
            System.out.println("practice_registryに " + studentId + " はありませんでした");
        }

        // practice_exam テーブルの処理
        try {
            resultList.add(jdbc.update("UPDATE practice_exam SET student_id = ? WHERE student_id = ?", deleteId, studentId));
        } catch (DataAccessException e) {
            System.out.println("practice_examに  " + studentId + " はありませんでした");
        }

        // regular_exam テーブルの処理
        try {
            resultList.add(jdbc.update("UPDATE regular_exam SET student_id = ? WHERE student_id = ?", deleteId, studentId));
        } catch (DataAccessException e) {
            System.out.println("regular_examに " + studentId + " はありませんでした");
        }

        // school_record テーブルの処理
        try {
            resultList.add(jdbc.update("UPDATE school_record SET student_id = ? WHERE student_id = ?", deleteId, studentId));
        } catch (DataAccessException e) {
            System.out.println("school_recordに " + studentId + " はありませんでした");
        }

        return resultList;
    }
}


