package com.somei.student_management_system.login.domain.repository;

import com.somei.student_management_system.login.domain.model.Highschools;
import com.somei.student_management_system.login.domain.model.PrivateHighSchool;
import com.somei.student_management_system.login.domain.model.PublicHighSchool;
import org.springframework.dao.DataAccessException;

import java.util.List;

public interface HighSchoolDao {

    // 公立高校のデータを１件取得
    public PublicHighSchool getPublicHighSchoolOne(String highschoolId) throws DataAccessException;

    // 私立高校のデータを１件取得
    public PrivateHighSchool getPrivateHighSchoolOne(String highschoolId) throws DataAccessException;

    // 公立高校のデータを全件取得
    public List<PublicHighSchool> getPublicHighSchoolAll() throws DataAccessException;

    // 私立高校のデータを全件取得
    public List<PrivateHighSchool> getPrivateHighSchoolAll() throws DataAccessException;

    // 高校idと高校名の一覧を取得
    public List<Highschools> getHighschoolList() throws DataAccessException;
}
