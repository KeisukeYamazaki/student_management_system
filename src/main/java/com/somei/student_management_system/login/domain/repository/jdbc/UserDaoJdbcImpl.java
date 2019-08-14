package com.somei.student_management_system.login.domain.repository.jdbc;

import com.somei.student_management_system.login.domain.model.User;
import com.somei.student_management_system.login.domain.repository.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository("UserDaoJdbcImpl")
public class UserDaoJdbcImpl implements UserDao {

    @Autowired
    JdbcTemplate jdbc;

    @Autowired
    PasswordEncoder passwordEncoder;

    // Userテーブルの件数を取得.
    @Override
    public int count() throws DataAccessException {

        // 全件取得してカウント
        int count = jdbc.queryForObject("SELECT COUNT(*) FROM s_user", Integer.class);

        return count;
    }

    // Userテーブルにデータを1件insert.
    @Override
    public int insertOne(User user) throws DataAccessException {

        //パスワード暗号化
        String password = passwordEncoder.encode(user.getPassword());

        //１件登録
        int rowNumber = jdbc.update("INSERT INTO s_user(user_id,"
                        + " password,"
                        + " user_name,"
                        + " role)"
                        + " VALUES(?, ?, ?, ?)",
                user.getUserId(),
                password,
                user.getUserName(),
                user.getRole());

        return rowNumber;
    }

    // Userテーブルのデータを１件取得
    @Override
    public User selectOne(String userId) throws DataAccessException {

        // １件取得
        Map<String, Object> map = jdbc.queryForMap("SELECT * FROM s_user"
                + " WHERE user_id = ?", userId);

        // 結果返却用の変数
        User user = new User();

        // 取得したデータを結果返却用の変数にセットしていく
        user.setUserId((String) map.get("user_id")); //ユーザーID
        user.setPassword((String) map.get("password")); //パスワード
        user.setUserName((String) map.get("user_name")); //ユーザー名
        user.setRole((String) map.get("role")); //ロール
        user.setReallyPass((String) map.get("really_pass"));  // 打ち込みパスワード

        return user;
    }

    // Userテーブルの全データを取得.
    @Override
    public List<User> selectMany() throws DataAccessException {

        // M_USERテーブルのデータを全件取得
        List<Map<String, Object>> getList = jdbc.queryForList("SELECT * FROM s_user");

        // 結果返却用の変数
        List<User> userList = new ArrayList<>();

        // 取得したデータを結果返却用のListに格納していく
        for (Map<String, Object> map : getList) {

            //Userインスタンスの生成
            User user = new User();

            // Userインスタンスに取得したデータをセットする
            user.setUserId((String) map.get("user_id")); //ユーザーID
            user.setPassword((String) map.get("password")); //パスワード
            user.setUserName((String) map.get("user_name")); //ユーザー名
            user.setRole((String) map.get("role")); //ロール
            user.setReallyPass((String) map.get("really_pass"));  // 打ち込みパスワード

            //結果返却用のListに追加
            userList.add(user);
        }

        return userList;
    }

    // Userテーブルを１件更新.
    @Override
    public int updateOne(User user) throws DataAccessException {

        //パスワード暗号化
        String password = passwordEncoder.encode(user.getPassword());

        //１件更新
        int rowNumber = jdbc.update("UPDATE S_USER"
                        + " SET"
                        + " password = ?,"
                        + " user_name = ?,"
                        + " WHERE user_id = ?",
                password,
                user.getUserName(),
                user.getUserId());

        return rowNumber;
    }

    // Userテーブルを１件削除.
    @Override
    public int deleteOne(String userId) throws DataAccessException {

        //１件削除
        int rowNumber = jdbc.update("DELETE FROM s_user WHERE user_id = ?", userId);

        return rowNumber;
    }

}
