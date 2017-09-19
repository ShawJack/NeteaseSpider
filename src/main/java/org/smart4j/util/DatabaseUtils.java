package org.smart4j.util;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * Created by ithink on 17-9-5.
 */
public class DatabaseUtils {

    private static final ThreadLocal<Connection> CONNECTION_HOLDER ;
    private static final QueryRunner QUERY_RUNNER;
    private static final BasicDataSource DATA_SOURCE;

    static{
        CONNECTION_HOLDER = new ThreadLocal<Connection>();
        QUERY_RUNNER = new QueryRunner();
        DATA_SOURCE = new BasicDataSource();

        Properties properties = PropsUtils.loadProps("db.properties");
        String driverClassName = PropsUtils.getString(properties, "driverClassName", "com.mysql.jc.jdbc.Driver");
        String url = PropsUtils.getString(properties, "url", "jdbc:mysql://127.0.0.2:3306/netease_music_db");
        String username = PropsUtils.getString(properties, "username", "root");
        String password = PropsUtils.getString(properties, "password", "123456");

        DATA_SOURCE.setDriverClassName(driverClassName);
        DATA_SOURCE.setUrl(url);
        DATA_SOURCE.setUsername(username);
        DATA_SOURCE.setPassword(password);
    }

    /**
     * 获取数据库连接
     */
    public static Connection getConnection(){
        Connection connection = CONNECTION_HOLDER.get();

        if(connection == null){
            try{
                connection = DATA_SOURCE.getConnection();
            } catch (SQLException e){
                throw  new RuntimeException(e);
            } finally {
                CONNECTION_HOLDER.set(connection);
            }
        }

        return connection;
    }

    /**
     * 关闭连接
     */
    public static void closeConnection(){
        Connection connection = CONNECTION_HOLDER.get();

        if(connection != null){
            try {
                connection.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }finally {
                CONNECTION_HOLDER.remove();
            }
        }
    }

    /**
     * 执行更新操作
     */
    public static int executeUpdate(String sql, Object...params){
        int rows = 0;
        try {
            Connection connection = getConnection();
            rows = QUERY_RUNNER.update(connection, sql, params);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            closeConnection();
        }

        return rows;
    }

    /**
     * 执行查询
     */
    public static <T> List<T> queryBeanList(Class<T> entityClass, String sql, Object...params){
        List<T> entityList;

        try{
            Connection connection = getConnection();
            entityList = QUERY_RUNNER.query(connection, sql, new BeanListHandler<T>(entityClass), params);
        } catch (SQLException e){
            throw new RuntimeException(e);
        }

        return entityList;
    }

    /**
     * 查询实体
     */
    public static <T> T queryEntity(Class<T> entityClass, String sql, Object...param){
        T entity;
        try{
            entity = QUERY_RUNNER.query(getConnection(), sql, new BeanHandler<T>(entityClass), param);
        } catch (SQLException e){
            throw new RuntimeException(e);
        }

        return entity;
    }

    /**
     * 执行SQL文件
     */
    public static void executeSqlFile(String filepath){
        Path path = Paths.get(filepath);

        try {
            List<String> lines = Files.readAllLines(path);
            for(String line : lines){
                executeUpdate(line);
            }
        } catch (IOException e){
            throw new RuntimeException(e);
        }
    }

    /**
     * 插入实体
     */
    public static <T> boolean insertEntity(Class<T> entityClass, Map<String, Object> fieldMap){
        if(MapUtils.isEmpty(fieldMap)){
            return false;
        }

        String sql = "REPLACE INTO " + getTableName(entityClass).toLowerCase();
        StringBuilder columns = new StringBuilder("(");
        StringBuilder values = new StringBuilder("(");
        for(String fieldName : fieldMap.keySet()){
            columns.append(fieldName).append(", ");
            values.append("?, ");
        }
        columns.replace(columns.lastIndexOf(", "), columns.length(), ")");
        values.replace(values.lastIndexOf(", "), values.length(), ")");

        sql += columns + " VALUES " + values;
        Object[] params = fieldMap.values().toArray();

        return executeUpdate(sql, params) == 1;
    }

    /**
     * 更新实体
     */
    public static boolean updateEntity(Class<?> entityClass, String id, Map<String, Object> fieldMap){
        if(MapUtils.isEmpty(fieldMap)){
            return false;
        }

        StringBuilder sb = new StringBuilder();
        for(String fieldName : fieldMap.keySet()){
            sb.append(fieldName + "=?, ");
        }
        sb.replace(sb.lastIndexOf(", "), sb.length(), " where id=" + id + ";");

        String sql = "update " + entityClass.getSimpleName() + " set " + sb;

        Object[] params = fieldMap.values().toArray();

        return executeUpdate(sql, params) == 1;
    }

    /**
     * 表名称与类实体名称相同
     */
    public static String getTableName(Class<?> entityClass){
        return entityClass.getSimpleName();
    }

}
