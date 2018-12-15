package com.test.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;

public class IndexServlet extends HttpServlet {
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //获取请求的url
        String url = req.getRequestURI();
        //获取调用的方法名
        String cmd = req.getParameter("cmd");
        //com.ss.action.IndexAction.action
        String className = url.substring(url.lastIndexOf("/") + 1, url.lastIndexOf("."));
        //返回结果
        String result = "";
        if (null != className && !"".equals(className)) {
            try {
                Class<?> cla = Class.forName(className);
                Object obj = cla.newInstance();
                Object value = cla.getMethod(cmd).invoke(obj);
                if (null != value) {
                    result = value.toString();
                }
            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }

        PrintWriter out = resp.getWriter();
        out.write(result);
        if (out != null) {
            out.close();
        }
    }
}
