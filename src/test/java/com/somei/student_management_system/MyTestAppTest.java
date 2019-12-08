package com.somei.student_management_system;

import junit.framework.TestCase;
import org.springframework.beans.factory.annotation.Autowired;

public class MyTestAppTest extends TestCase {

    @Autowired
    MyTestApp1 myTestApp1;

    public MyTestAppTest(String name) {
        super(name);
    }

    public void testGetId() {
        String result = "2234";
        String id = myTestApp1.getId("谷 航平");
        assertEquals(result, "2234");
    }

}
