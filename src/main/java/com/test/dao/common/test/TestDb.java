package com.test.dao.common.test;

import com.test.dao.common.CommonDao;

import java.util.List;

public class TestDb {
    public static void main(String[] args) {
        for(int i = 0; i < 500; i ++){
            Runnable fun = new Runnable(){
                @Override
                public void run() {
                    CommonDao dao = CommonDao.getInstance();
                    List<Test> list = dao.findList("select * from test", Test.class);
                    for (Test test : list) {
                        System.out.println(test.getId());
                        System.out.println(test.getUsername());
                        System.out.println(test.getPassword());
                    }
                    Test test = new Test();
                    test.setId("4");
                    test.setUsername("66");
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
            };
            Thread thread = new Thread(fun);
            thread.start();
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }

    }


}
