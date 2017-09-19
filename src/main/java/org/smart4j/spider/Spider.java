package org.smart4j.spider;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.smart4j.ConfigConstant;
import org.smart4j.bean.Music;
import org.smart4j.bean.PlayList;
import org.smart4j.util.DatabaseUtils;
import org.smart4j.util.SpiderUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by ithink on 17-9-12.
 */
public class Spider {

    public static void parseUrl(String url) throws IOException{
        Document document = null;
        if(url.startsWith("/song")){
            document = parseMusic(url);
        }else if(url.startsWith("/playlist")){
            document = parsePlayList(url);
        }

        getUrls(document, ConfigConstant.BASE_PATH + url);
    }

    public static Document parsePlayList(String url) throws IOException{
        Document document = SpiderUtils.httpRequestByJSoup(ConfigConstant.BASE_PATH + url);

        Element title = document.select("h2[class=f-ff2 f-brk]").first();
        Element createdBy = document.select("a[class=s-fc7]").first();
        Element createDate = document.select("span[class=time s-fc4]").first();
        String collect = document.select("a[data-res-action=fav]").first().attr("data-count");
        String share = document.select("a[data-res-action=share]").first().attr("data-count");
        Element comment = document.select("span[id=cnt_comment_count]").first();
        Elements tags = document.select("a[class=u-tag]");
        Element description = document.select("p[id=album-desc-more]").first();
        Element playTimes = document.select("strong[id=play-count]").first();
        Elements musicList = document.select("a[href^=/song?id=]");
        String id = url.substring(url.lastIndexOf("=")+1);

        /*System.out.println("歌单ID：" + id);
        System.out.println("歌单名：" + title.text());
        System.out.println("创建者：" + createdBy.text());
        System.out.println("创建日期：" + createDate.text());
        System.out.print("标签：");
        for(Element e : tags){
            System.out.print(e.text() + " ");
        }
        System.out.println();
        if(description != null)System.out.println("介绍：" + description.text());
        System.out.println("收藏数：" + collect);
        System.out.println("分享数：" + share);
        System.out.println("评论数：" + comment.text());
        System.out.println("播放数：" + playTimes.text());
        System.out.println("歌单列表：");
        for(Element e : musicList){
            String songName = e.text();
            String songUrl = e.attr("href");
            System.out.println(songName + "(" + songUrl.substring(songUrl.lastIndexOf("=")+1) + ")");
        }
        System.out.println("-------------");
        System.out.println();*/
        Map<String, Object> fieldMap = new HashMap<>();
        fieldMap.put("id", id);
        fieldMap.put("title", title.text());

        StringBuffer label = new StringBuffer();
        for(Element e : tags){
            label.append(e.text() + ",");
        }
        fieldMap.put("label", label.substring(0, label.length()-1));
        if(description != null)fieldMap.put("description", description.text());
        fieldMap.put("share", share);
        fieldMap.put("collect", collect);
        fieldMap.put("comment", comment.text());
        fieldMap.put("createdBy", createdBy.text());
        fieldMap.put("createdDate", createDate.text().substring(0, 10));

        StringBuffer musics = new StringBuffer();
        for(Element e : musicList){
            String songName = e.text();
            String songUrl = e.attr("href");
            musics.append(songName + "(" + songUrl.substring(songUrl.lastIndexOf("=")+1) + "),");
        }
        fieldMap.put("musicList", musics.substring(0, musics.length()-1));

        System.out.println(DatabaseUtils.insertEntity(PlayList.class, fieldMap));

        return document;
    }

    public static Document parseMusic(String url) throws IOException{
        Document doc = SpiderUtils.httpRequestByJSoup(ConfigConstant.BASE_PATH + url);

        Element title = doc.select("em.f-ff2").first();
        Element subTitle = doc.select("div[class=subtit f-fs1 f-ff2]").first();
        String songer = doc.select("p[class=des s-fc4]").select("span").attr("title");
        String album = doc.select("p[class=des s-fc4]").select("a[class=s-fc7]").last().attr("href");
        String songId = url.substring(url.lastIndexOf("=") + 1);

        String jsonUrl = "http://music.163.com/weapi/v1/resource/comments/R_SO_4_" + songId + "?csrf_token=";
        String comments = ((Double) SpiderUtils.getJsonByJSoup(jsonUrl).get("total")).longValue() + "";

        /*System.out.println("歌曲ID：" + songId);
        System.out.println("标题：" + title.text());
        if(subTitle != null)System.out.println("子标题：" + subTitle.text());
        System.out.println("歌手：" + songer);
        System.out.println("所属专辑ID：" + album.substring(album.lastIndexOf("=")+1));
        System.out.println("评论数量：" + comments);
        System.out.println("----------------------");
        System.out.println();*/

        Map<String, Object> fieldMap = new HashMap<>();
        fieldMap.put("id", songId);
        fieldMap.put("title", title.text());
        if(subTitle != null)fieldMap.put("subTitle", subTitle.text());
        fieldMap.put("songer", songer);
        fieldMap.put("albumId", album.substring(album.lastIndexOf("=")+1));
        fieldMap.put("comment", comments);

        System.out.println(fieldMap.toString());
        System.out.println(DatabaseUtils.insertEntity(Music.class, fieldMap));

        return doc;
    }

    public static void getUrls(Document document, String url) throws IOException{
        if(document == null)document = SpiderUtils.httpRequestByJSoup(url);

        Elements urls = document.select("a[href~=^/([^/]+)(.*)]");
        for(Element e : urls){
            String href = e.attr("href");
            //System.out.println(href);
            ConfigConstant.URL_SET.add(href);
        }
    }

}
