package com.somei.student_management_system.login.domain.service;

import com.somei.student_management_system.login.domain.model.PrivateHighSchool;
import com.somei.student_management_system.login.domain.model.PublicHighSchool;
import com.somei.student_management_system.login.domain.repository.HighSchoolDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HighSchoolService {

    @Autowired
    HighSchoolDao highSchoolDao;

    /**
     * 公立高校データを１件取得するメソッド
     *
     * @param highschoolId 高校のID
     * @return １件の公立高校のデータ
     */
    public PublicHighSchool getPublicHighSchoolOne(String highschoolId) {

        return highSchoolDao.getPublicHighSchoolOne(highschoolId);

    }

    /**
     * 私立高校データを１件取得するメソッド
     *
     * @param highschoolId 高校のID
     * @return １件の公立高校のデータ
     */
    public PrivateHighSchool getPrivateHighSchoolOne(String highschoolId) {

        return highSchoolDao.getPrivateHighSchoolOne(highschoolId);

    }
}
