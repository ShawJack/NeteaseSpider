package org.smart4j.util;

import java.util.BitSet;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by ithink on 17-9-13.
 */
public class UrlSet {
    private SetForParse setForParse = new SetForParse();
    private BloomFilter bloomFilter = new BloomFilter();

    public synchronized String get(){
        if(isEmpty())return null;

        String url = setForParse.get();

        bloomFilter.add(url);

        return url;
    }

    public synchronized void add(String url){
        if(url == null)return;

        if(bloomFilter.contains(url))return;

        setForParse.add(url);
    }

    private boolean contains(String value){
        return bloomFilter.contains(value);
    }

    private boolean isEmpty(){
        return setForParse.isEmpty();
    }

    public static class BloomFilter {

        private final static int DEFAULT_SIZE = 2 << 24;
        private final static int[] seeds = new int[]{7, 11, 13, 31, 37, 61};
        private BitSet bitSet = new BitSet(DEFAULT_SIZE);
        private org.smart4j.util.BloomFilter.Hash[] hashes = new org.smart4j.util.BloomFilter.Hash[seeds.length];
        //private Lock lock = new ReentrantLock();

        public BloomFilter(){
            for(int i=0; i<seeds.length; i++){
                hashes[i] = new org.smart4j.util.BloomFilter.Hash(DEFAULT_SIZE, seeds[i]);
            }
        }

        public void add(String value){
            if(value == null)return ;

            for (int i = 0; i < hashes.length; i++) {
                bitSet.set(hashes[i].hash(value), true);
            }
        }

        public boolean contains(String value){
            if(value == null)return false;

            for (org.smart4j.util.BloomFilter.Hash hash : hashes) {
                if (!bitSet.get(hash.hash(value))) return false;
            }

            return true;
        }

    }

    public static class Hash{
        private int cap;
        private int seed;

        public Hash(int cap, int seed){
            this.cap = cap;
            this.seed = seed;
        }

        public int hash(String value){
            int result = 0;
            for(int i=0; i<value.length(); i++){
                result = seed * result + value.charAt(i);
            }

            return (cap - 1) & result;
        }
    }

    public static class SetForParse {
        private Set<String> urls = new HashSet<String>();
        //private Lock lock = new ReentrantLock();

        public boolean isEmpty(){
            return urls.isEmpty();
        }

        public void add(String url){

            if(url == null)return;

            urls.add(url);
        }

        public String get(){
            String url = null;

            Iterator<String> it = urls.iterator();
            if(it.hasNext()){
                url = it.next();
                it.remove();
            }

            return url;

        }
    }

}
