package com.test.dao.common;

import java.sql.*;
import java.util.*;

public class CommonDao {
    private Connection conn;
    private static CommonDao instance;

    private CommonDao() {
        String url = "jdbc:mysql://192.168.129.128:3306/blog";
        String username = "root";
        String password = "Gepoint";
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(url, username, password);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static  CommonDao getInstance(){
        if (instance == null)
            instance = new CommonDao();
        return instance;
    }

    public <T> List<T> findList(String sql,Class<T> cla){
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<T> list = new ArrayList<>();
        try {
            if(null != conn){
                ps = conn.prepareStatement(sql);
            }
            rs = ps.executeQuery();
            while (rs.next()){
                T obj = cla.newInstance();
                HashMap<String,Object> rec = (HashMap<String,Object>)obj;
                ResultSetMetaData metaData = rs.getMetaData();
                for (int i = 0; i < metaData.getColumnCount(); i++) {
                    rec.put(metaData.getColumnName(i + 1).toLowerCase(),rs.getObject(metaData.getColumnName(i + 1)));
                }
                list.add(obj);
            }
        } catch (SQLException | InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        } finally {
            close(rs,ps);
        }
        return list;
    }

    public <T extends  HashMap<String,Object>> int insert(T cls) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        int rowCount = 0;
        try {
            HashMap<String,Object> rec = (HashMap<String,Object>)cls;
            StringBuffer buffer = new StringBuffer();
            buffer.append("insert into ");
            String packageName = cls.getClass().getPackage().getName();
            String className = cls.getClass().getName();
            String name = className.substring(packageName.length() + 1,className.length()).toLowerCase();
            buffer.append(name);
            buffer.append(" ( ");
            Set<String> keys = rec.keySet();
            int i = 0;
            for (String key : keys) {
                buffer.append(key);
                i ++;
                if(i != keys.size()){
                    buffer.append(",");
                }
            }
            buffer.append(" ) ");
            buffer.append(" values ( ");
            for (int j = 0; j < keys.size(); j++) {
                buffer.append("?");
                if(j != keys.size() - 1){
                    buffer.append(",");
                }
            }
            buffer.append(" ) ");
            ps = conn.prepareStatement(buffer.toString());
            int j = 0;
            for (String key : keys) {
                ps.setObject(++ j, rec.get(key));
            }
            System.out.println(buffer.toString());
            conn.setAutoCommit(false);
            rowCount = ps.executeUpdate();
            conn.commit();
        } catch (SQLException  e) {
            e.printStackTrace();
        }  finally {
            close(rs,ps);
        }
        return rowCount;
    }

    public <T extends HashMap<String,Object>> int update(T obj){
        PreparedStatement ps = null;
        ResultSet rs = null;
        int rowCount = 0;
        try {
            HashMap<String,Object> rec = (HashMap<String,Object>)obj;
            StringBuffer buffer = new StringBuffer();
            buffer.append("update ");
            String packageName = obj.getClass().getPackage().getName();
            String className = obj.getClass().getName();
            String name = className.substring(packageName.length() + 1,className.length()).toLowerCase();
            buffer.append(name);
            buffer.append(" set ");
            Set<String> keys = rec.keySet();
            int i = 0;
            for (String key : keys) {
                buffer.append(key);
                buffer.append(" = ");
                buffer.append(" ? ");
                i ++;
                if(i != keys.size()){
                    buffer.append(",");
                }
            }
            // FIXME 读取注解，获取主键，暂时写死，后期修改
            buffer.append(" where id = ? ");
            ps = conn.prepareStatement(buffer.toString());
            int j = 0;
            for (String key : keys) {
                ps.setObject(++ j, rec.get(key));
            }
            ps.setObject(++ j,rec.get("id"));
            System.out.println(buffer.toString());
            conn.setAutoCommit(false);
            rowCount = ps.executeUpdate();
            conn.commit();
        } catch (SQLException  e) {
            e.printStackTrace();
        }  finally {
            close(rs,ps);
        }
        return rowCount;
    }

    public <T extends HashMap<String,Object>> int delete(T obj){
        PreparedStatement ps = null;
        ResultSet rs = null;
        int rowCount = 0;
        try {
            HashMap<String,Object> rec = (HashMap<String,Object>)obj;
            StringBuffer buffer = new StringBuffer();
            buffer.append("delete from ");
            String packageName = obj.getClass().getPackage().getName();
            String className = obj.getClass().getName();
            String name = className.substring(packageName.length() + 1,className.length()).toLowerCase();
            buffer.append(name);
            buffer.append(" where ");
            Set<String> keys = rec.keySet();
            int i = 0;
            for (String key : keys) {
                buffer.append(key);
                buffer.append(" = ");
                buffer.append(" ? ");
                i ++;
                if(i != keys.size()){
                    buffer.append(" and ");
                }
            }
            ps = conn.prepareStatement(buffer.toString());
            int j = 0;
            for (String key : keys) {
                ps.setObject(++ j, rec.get(key));
            }
            System.out.println(buffer.toString());
            conn.setAutoCommit(false);
            rowCount = ps.executeUpdate();
            conn.commit();
        } catch (SQLException  e) {
            e.printStackTrace();
        }  finally {
            close(rs,ps);
        }
        return rowCount;
    }

    public void close(ResultSet rs,PreparedStatement ps){
        try {
            if(null != rs){
                rs.close();
            }
            if(null != ps){
                ps.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
