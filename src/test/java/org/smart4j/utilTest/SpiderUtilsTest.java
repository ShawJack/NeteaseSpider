package org.smart4j.utilTest;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;
import org.smart4j.ConfigConstant;
import org.smart4j.util.SpiderUtils;

import java.io.IOException;

/**
 * Created by ithink on 17-9-6.
 */
public class SpiderUtilsTest {

    @Test
    public void httpReuqestTest(){
        System.out.println(SpiderUtils.httpRequest(ConfigConstant.ALL_PLAY_LIST_PATH));
    }

    @Test
    public void httpRequestByHttpClientTest() throws IOException{
        //http://music.163.com/weapi/v1/resource/comments/R_SO_4_280220?csrf_token=
        System.out.println(SpiderUtils.httpRequestByHttpClient("http://music.163.com/weapi/v1/resource/comments/R_SO_4_212233?csrf_token="));
    }

    @Test
    public void httpRequestByJSoupTest() throws IOException{
        //System.out.println(SpiderUtils.httpRequestByJSoup("http://music.163.com/weapi/v1/resource/comments/R_SO_4_212233?csrf_token="));
        //String htmlText = SpiderUtils.httpRequestByJSoup(ConfigConstant.MUSIC_BASE_PATH + "28875230");
        Document doc = SpiderUtils.httpRequestByJSoup(ConfigConstant.MUSIC_BASE_PATH + "212233");

        Element title = doc.select("em.f-ff2").first();
        Element subTitle = doc.select("div[class=subtit f-fs1 f-ff2]").first();
        Element songer = doc.select("p[class=des s-fc4]").select("span").select("a[class=s-fc7]").first();
        String album = doc.select("p[class=des s-fc4]").select("a[class=s-fc7]").last().attr("href");

        String jsonUrl = "http://music.163.com/weapi/v1/resource/comments/R_SO_4_" + 212233 + "?csrf_token=";
        String comments = ((Double) SpiderUtils.getJsonByJSoup(jsonUrl).get("total")).longValue() + "";
        System.out.println("标题：" + title.text());
        if(subTitle != null)System.out.println("子标题：" + subTitle.text());
        System.out.println("歌手：" + songer.text());
        System.out.println("所属专辑ID：" + album.substring(album.lastIndexOf("=")+1));
        System.out.println("评论数量：" + comments);

        Elements otherSongs = doc.select("a[href^=/song?id=]");
        for(Element e : otherSongs){
            System.out.println(e.attr("href"));
        }

    }

    /**
     *
     */
    @Test
    public void getPalyListTest() throws IOException{
        //886796444
        Document doc = SpiderUtils.httpRequestByJSoup(ConfigConstant.PLAY_LIST_BASE_PATH + "886796444");
        Elements otherSongs = doc.select("a[href~=^/([^/]+)(.*)]");
        for(Element e : otherSongs){
            System.out.println(e.attr("href"));
        }
    }

}
