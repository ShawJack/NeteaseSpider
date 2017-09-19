package org.smart4j.utilTest;

import org.junit.Before;
import org.junit.Test;
import org.smart4j.bean.Music;
import org.smart4j.util.DatabaseUtils;

/**
 * Created by ithink on 17-9-5.
 */
public class DatabaseUtilsTest {

    @Before
    public void init(){
        DatabaseUtils.executeSqlFile("/home/ithink/java/NeteaseMusic/src/main/resources/sql/init.sql");
    }

    @Test
    public void queryEntityTest(){
        Music music = DatabaseUtils.queryEntity(Music.class, "SELECT * FROM music;");
        System.out.println(music.getTitle());
    }

}
