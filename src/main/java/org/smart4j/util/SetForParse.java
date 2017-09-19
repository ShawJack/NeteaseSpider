package org.smart4j.util;

import java.net.URL;
import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by ithink on 17-9-12.
 */
public class SetForParse {
    private Set<String> urls = new HashSet<String>();
    private Lock lock = new ReentrantLock();

    public boolean isEmpty(){
        return urls.isEmpty();
    }

    public void add(String url){

        if(url == null)return;

        try{
            lock.lock();
            urls.add(url);
        } finally {
            lock.unlock();
        }
    }

    public String get(){
        String url = null;
        try{
            lock.lock();

            Iterator<String> it = urls.iterator();
            if(it.hasNext()){
                url = it.next();
                it.remove();
            }
        } finally {
            lock.unlock();
        }

        return url;

    }
}
