package com.test.dao.common;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class CommonDao {
    private Connection conn;
    private static CommonDao instance;

    // 保存后，在同个线程中(同一次请求中)拿到的数据库连接都是同一个。
    private static ThreadLocal<List<CommonDao>> daosLocal = new ThreadLocal<>();

    private CommonDao() {
        String url = "jdbc:mysql://192.168.129.15:3306/blog";
        String username = "root";
        String password = "11111";
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(url, username, password);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static CommonDao getInstance() {
        List<CommonDao> daos = daosLocal.get();
        if (null == daos || daos.isEmpty()) {
            instance = new CommonDao();
            daos = new ArrayList<>();
            daos.add(instance);
            daosLocal.set(daos);
            return instance;
        } else {
            return daos.get(0);
        }
    }

    public <T> List<T> findList(String sql, Class<T> cla) {
        List<T> list = new ArrayList<>();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            if (null != conn) {
                ps = conn.prepareStatement(sql);
            }
            rs = ps.executeQuery();
            while (rs.next()) {
                T obj = cla.newInstance();
                HashMap<String, Object> rec = (HashMap<String, Object>) obj;
                ResultSetMetaData metaData = rs.getMetaData();
                for (int i = 0; i < metaData.getColumnCount(); i++) {
                    rec.put(metaData.getColumnName(i + 1).toLowerCase(), rs.getObject(metaData.getColumnName(i + 1)));
                }
                list.add(obj);
            }
        } catch (SQLException | InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        } finally {
            close(ps, rs);
        }
        return list;
    }

    public <T extends HashMap<String, Object>> int insert(T obj) {
        int rowCount = 0;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            HashMap<String, Object> rec = (HashMap<String, Object>) obj;
            StringBuffer buffer = new StringBuffer();
            buffer.append("insert into ");
            buffer.append(getTableName(obj));
            buffer.append(" ( ");
            Set<String> keys = rec.keySet();
            int i = 0;
            for (String key : keys) {
                buffer.append(key);
                i++;
                if (i != keys.size()) {
                    buffer.append(",");
                }
            }
            buffer.append(" ) ");
            buffer.append(" values ( ");
            for (int j = 0; j < keys.size(); j++) {
                buffer.append("?");
                if (j != keys.size() - 1) {
                    buffer.append(",");
                }
            }
            buffer.append(" ) ");
            rowCount = transcation(buffer.toString(), rec, false);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(ps, rs);
        }
        return rowCount;
    }

    public <T extends HashMap<String, Object>> int update(T obj) {
        int rowCount = 0;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            HashMap<String, Object> rec = (HashMap<String, Object>) obj;
            StringBuffer buffer = new StringBuffer();
            buffer.append("update ");
            buffer.append(getTableName(obj));
            buffer.append(" set ");
            Set<String> keys = rec.keySet();
            int i = 0;
            for (String key : keys) {
                buffer.append(key);
                buffer.append(" = ");
                buffer.append(" ? ");
                i++;
                if (i != keys.size()) {
                    buffer.append(",");
                }
            }
            // FIXME 读取注解，获取主键，暂时写死，后期修改
            buffer.append(" where id = ? ");
            rowCount = transcation(buffer.toString(), rec, true);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(ps, rs);
        }
        return rowCount;
    }

    public <T extends HashMap<String, Object>> int delete(T obj) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        int rowCount = 0;
        try {
            HashMap<String, Object> rec = (HashMap<String, Object>) obj;
            StringBuffer buffer = new StringBuffer();
            buffer.append("delete from ");
            buffer.append(getTableName(obj));
            buffer.append(" where ");
            Set<String> keys = rec.keySet();
            int i = 0;
            for (String key : keys) {
                buffer.append(key);
                buffer.append(" = ");
                buffer.append(" ? ");
                i++;
                if (i != keys.size()) {
                    buffer.append(" and ");
                }
            }
            rowCount = transcation(buffer.toString(), rec, false);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(ps, rs);
        }
        return rowCount;
    }

    private int transcation(String sql, Map<String, Object> rec, boolean flag) throws SQLException {
        PreparedStatement ps = null;
        //ResultSet rs = null;
        ps = conn.prepareStatement(sql);
        int rowCount = 0;
        int j = 0;
        Set<String> keys = rec.keySet();
        for (String key : keys) {
            ps.setObject(++j, rec.get(key));
        }
        if (flag) {
            ps.setObject(++j, rec.get("id"));
        }
        conn.setAutoCommit(false);
        rowCount = ps.executeUpdate();
        conn.commit();
        return rowCount;
    }

    private <T extends HashMap<String, Object>> String getTableName(T obj) {
        String packageName = obj.getClass().getPackage().getName();
        String className = obj.getClass().getName();
        String name = className.substring(packageName.length() + 1, className.length()).toLowerCase();
        return name;
    }

    public void close(PreparedStatement ps, ResultSet rs) {
        try {
            if (null != rs) {
                rs.close();
            }
            if (null != ps) {
                ps.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void close() {
        try {
            if (null != conn) {
                conn.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            removeConnetion();
        }
    }

    public void closeAllConns() {
        List<CommonDao> daos = daosLocal.get();
        if (null == daos) {
            return;
        }
        for (int i = 0; i < daos.size(); i++) {
            daos.get(i).close();
        }
        daos.clear();
    }

    private void removeConnetion() {
        List<CommonDao> daos = daosLocal.get();
        if (null != daos) {
            for (int i = 0; i < daos.size(); i++) {
                CommonDao dao = daos.get(i);
                if (dao.conn == conn) {
                    daos.remove(i);
                }
                i--;
            }
        }
    }
}
