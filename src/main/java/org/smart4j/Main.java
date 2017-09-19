package org.smart4j;

import org.smart4j.spider.Spider;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by ithink on 17-9-12.
 */
public class Main {

    public static void main(String[] args){
        ConfigConstant.URL_SET.add(ConfigConstant.ALL_PLAY_LIST_PATH);
        ExecutorService executor = Executors.newFixedThreadPool(2);

        while(true){
            String url = ConfigConstant.URL_SET.get();
            if(url == null)continue;

            System.out.println(url);
            executor.submit(() -> {
                try {
                    Spider.parseUrl(url);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }
    }

}
