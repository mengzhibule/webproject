package com.test.dao.common.test;

import com.test.dao.common.CommonDao;

import java.util.List;

public class TestDb {
    public static void main(String[] args) {
        CommonDao dao = CommonDao.getInstance();
        List<Test> list = dao.findList("select * from test", Test.class);
        for (Test test : list) {
            System.out.println(test.getId());
            System.out.println(test.getUsername());
            System.out.println(test.getPassword());
        }
        Test test = new Test();
        test.setId("3");
        test.setUsername("4444");
        test.setPassword("3");
        int insert = dao.delete(test);
        System.out.println(insert);

        list = dao.findList("select * from test", Test.class);
        for (Test rec : list) {
            System.out.println(rec.getId());
            System.out.println(rec.getUsername());
            System.out.println(rec.getPassword());
        }
    }


}
