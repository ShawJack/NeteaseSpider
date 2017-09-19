package org.smart4j.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by ithink on 17-9-5.
 */
public class PropsUtils {

    /**
     * 加载配置文件
     * @param filename
     * @return
     * @throws IOException
     */
    public static Properties loadProps(String filename){
        InputStream is = null;
        Properties properties = null;

        try{
            is = Thread.currentThread().getContextClassLoader().getResourceAsStream(filename);
            properties = new Properties();

            properties.load(is);
        } catch (IOException e){
            e.printStackTrace();
        }

        return properties;
    }

    /**
     * 获取配置参数
     * @param props
     * @param key
     * @param defaultValue
     * @return
     */
    public static String getString(Properties props, String key, String defaultValue){
        String value = defaultValue;

        if(props.containsKey(key)){
            value = props.getProperty(key);
        }

        return value;
    }

}
