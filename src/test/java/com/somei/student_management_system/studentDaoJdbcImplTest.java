package com.somei.student_management_system;

import com.somei.student_management_system.login.domain.model.Student;
import com.somei.student_management_system.login.domain.repository.jdbc.StudentDaoJdbcImpl;
import junit.framework.TestCase;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.List;

public class studentDaoJdbcImplTest extends TestCase {

    @Autowired
    StudentDaoJdbcImpl studentDaoJdbc;

    public studentDaoJdbcImplTest(String name){
        super(name);
    }

    public void testSelectIdName() {
        List<Student> result = Arrays.asList(new Student());
        List<Student> list = studentDaoJdbc.selectIdName();
        assertEquals(result, list);
    }
}
