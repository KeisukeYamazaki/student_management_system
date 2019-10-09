package com.somei.student_management_system.login.domain.repository.jdbc;

import com.somei.student_management_system.login.domain.model.Highschools;
import com.somei.student_management_system.login.domain.model.PrivateHighSchool;
import com.somei.student_management_system.login.domain.model.PublicHighSchool;
import com.somei.student_management_system.login.domain.repository.HighSchoolDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class HighSchoolDaoJdbcImpl implements HighSchoolDao {

    @Autowired
    private JdbcTemplate jdbc;

    /**
     * 公立高校データを１件取得する
     *
     * @param highschoolId 高校のID
     * @return １件の公立高校データ
     */
    @Override
    public PublicHighSchool getPublicHighSchoolOne(String highschoolId) throws DataAccessException {

        try {

            String sql = "SELECT * FROM public_highschool WHERE highschool_id = ?";

            //RowMapperの生成
            RowMapper<PublicHighSchool> rowMapper = new BeanPropertyRowMapper<>(PublicHighSchool.class);

            //SQL実行
            return jdbc.queryForObject(sql, rowMapper, highschoolId);

        } catch (DataAccessException e) {

            // //データが無かった場合は、nullを返す
            return null;
        }
    }

    /**
     * 私立高校データを１件取得する
     *
     * @param highschoolId 高校のID
     * @return １件の私立高校データ
     */
    @Override
    public PrivateHighSchool getPrivateHighSchoolOne(String highschoolId) throws DataAccessException {

        try {

            String sql = "SELECT * FROM private_highschool WHERE highschool_id = ?";

            //RowMapperの生成
            RowMapper<PrivateHighSchool> rowMapper = new BeanPropertyRowMapper<>(PrivateHighSchool.class);

            //SQL実行
            return jdbc.queryForObject(sql, rowMapper, highschoolId);

        } catch (DataAccessException e) {

            // //データが無かった場合は、nullを返す
            return null;
        }
    }

    @Override
    public List<PublicHighSchool> getPublicHighSchoolAll() throws DataAccessException {
        return null;
    }

    @Override
    public List<PrivateHighSchool> getPrivateHighSchoolAll() throws DataAccessException {
        return null;
    }

    @Override
    public List<Highschools> getHighschoolList() throws DataAccessException {

        try {

            String sql = "SELECT highschool_id, highschool_name FROM public_highschool"
                    + " UNION "
                    + " SELECT highschool_id, highschool_name FROM private_highschool"
                    + " ORDER BY highschool_id";

            //RowMapperの生成
            RowMapper<Highschools> rowMapper = new BeanPropertyRowMapper<>(Highschools.class);

            //SQL実行
            return jdbc.query(sql, rowMapper);

        } catch (DataAccessException e) {
            e.printStackTrace();
        }

        return null;
    }
}
