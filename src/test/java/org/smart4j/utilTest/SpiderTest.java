package org.smart4j.utilTest;

import org.junit.Test;
import org.smart4j.ConfigConstant;
import org.smart4j.spider.Spider;

import java.io.IOException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by ithink on 17-9-12.
 */
public class SpiderTest {

    @Test
    public void parseTest() throws IOException{
        ConfigConstant.URL_SET.add(ConfigConstant.ALL_PLAY_LIST_PATH);

        while(true){

            String url = ConfigConstant.URL_SET.get();
            if(url == null)continue;

            Spider.parseUrl(url);
        }
    }

}
