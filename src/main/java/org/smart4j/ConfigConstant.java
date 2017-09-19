package org.smart4j;

import org.smart4j.util.BloomFilter;
import org.smart4j.util.SetForParse;
import org.smart4j.util.UrlSet;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by ithink on 17-9-5.
 */
public interface ConfigConstant {

    String BASE_PATH = "http://music.163.com";
    String ALL_PLAY_LIST_PATH = "/discover/playlist";
    String PLAY_LIST_BASE_PATH = "http://music.163.com/playlist?id=";
    String MUSIC_BASE_PATH = "http://music.163.com/song?id=";

    String PARAMS = "params";
    String PARAMS_VALUE = "U4iqX5ZR2uC3unXwtYiz/ACyvU8QJRAsRYXXmMECm3TcuIm6jdGxCMLnuEGvjdKfA9ea7Yf6yafklSPjSRGI74MefhQPrEz8wnlHoXYK8OSbyrlYm5LRpQ10cgoI4mr6qhfMmRZ/WJdreyZDyXpdphyYHIbyUOIuGtwNL38qKUilARe4EUrv/8nwT6MYVw6G";
    String ENC_SEC_KEY = "encSecKey";
    String ENC_SEC_KEY_VALUE = "b6899b08bc8fb9b1e07b98bdcc5eeb9cbe34ff96b98e56d2a6b97909b0d646a58d494ccad89c6c3306caa290e4f1d0cddd241235aadfc5af72e1bed7e7e7f2192a80f1398e95ed053a3514e28349c5c356f78ea24c5701930e1163a2b3f3cae9a799c44f68d8ad09f439b464ca7fe3592f54e7180aba550ca010ad2e8c6267f8";

//    BloomFilter BLOOM_FILTER = new BloomFilter();
//    SetForParse SET_FOR_PARSE = new SetForParse();
    UrlSet URL_SET = new UrlSet();
}
