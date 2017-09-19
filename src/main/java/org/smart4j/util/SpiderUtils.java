package org.smart4j.util;

import com.google.gson.*;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.smart4j.ConfigConstant;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by ithink on 17-9-5.
 */
public class SpiderUtils {

    /**
     * 读取网页源代码
     */
    public static String httpRequest(String requestUrl){

        StringBuilder content = new StringBuilder();

        try (BufferedReader bufferedReader = new BufferedReader(
                new InputStreamReader(
                        new URL(requestUrl).openStream()))) {

            String line;
            while((line=bufferedReader.readLine()) != null){
                content.append(line);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return content.toString();
    }

    /**
     * 使用HttpClient
     */
    public static String httpRequestByHttpClient(String url) throws IOException{
        HttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(url);
        HttpResponse response = httpClient.execute(httpGet);
        InputStream inputStream = response.getEntity().getContent();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder content = new StringBuilder();
        String line;
        while ((line=bufferedReader.readLine()) != null){
            content.append(line + "\n");
        }
        return content.toString();
    }

    /**
     * 用JSoup获取JSON数据
     */
    public static String httpRequestByJSoupTest(String url) throws IOException{
        Connection connection = Jsoup.connect(url);
        connection.method(Connection.Method.POST);
        connection.data("params", "U4iqX5ZR2uC3unXwtYiz/ACyvU8QJRAsRYXXmMECm3TcuIm6jdGxCMLnuEGvjdKfA9ea7Yf6yafklSPjSRGI74MefhQPrEz8wnlHoXYK8OSbyrlYm5LRpQ10cgoI4mr6qhfMmRZ/WJdreyZDyXpdphyYHIbyUOIuGtwNL38qKUilARe4EUrv/8nwT6MYVw6G");
        connection.data("encSecKey", "b6899b08bc8fb9b1e07b98bdcc5eeb9cbe34ff96b98e56d2a6b97909b0d646a58d494ccad89c6c3306caa290e4f1d0cddd241235aadfc5af72e1bed7e7e7f2192a80f1398e95ed053a3514e28349c5c356f78ea24c5701930e1163a2b3f3cae9a799c44f68d8ad09f439b464ca7fe3592f54e7180aba550ca010ad2e8c6267f8");
        connection.userAgent("Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/59.0.3071.115 Safari/537.36");
        connection.referrer("http://music.163.com/song?id=212233");
        connection.header("Host", "music.163.com");
        connection.header("Orgin", "http://music.163.com");
        connection.header("Content-Type", "application/x-www-form-urlencoded");
        connection.header("Accept-Encoding","gzip, deflate");
        connection.header("Accept-Language", "zh-CN,zh;q=0.8");
        Connection.Response response = connection.timeout(10000).ignoreContentType(true).execute();
        String body = response.body();
        Gson gson = new Gson();
        Map<String, Object> map = gson.fromJson(body, HashMap.class);
        return ((Double)map.get("total")).longValue()+"";//document.outerHtml();
    }

    /**
     * 使用JSoup获取JSON数据并解析
     */
    public static Map<String, Object> getJsonByJSoup(String url) throws IOException{
        Connection connection = Jsoup.connect(url);
        connection.method(Connection.Method.POST);
        connection.data(ConfigConstant.PARAMS, ConfigConstant.PARAMS_VALUE);
        connection.data(ConfigConstant.ENC_SEC_KEY, ConfigConstant.ENC_SEC_KEY_VALUE);
        Connection.Response response = connection.timeout(10000).ignoreContentType(true).execute();
        String body = response.body();
        Gson gson = new Gson();
        Map<String, Object> jsonMap = gson.fromJson(body, HashMap.class);

        return jsonMap;
    }

    /**
     * 使用JSoup获取到网页
     */
    public static Document httpRequestByJSoup(String url) throws IOException{
        Connection connection = Jsoup.connect(url);
        Connection.Response response = connection.timeout(10000).ignoreContentType(true).execute();
        //String body = response.body();

        return response.parse();
    }

}
