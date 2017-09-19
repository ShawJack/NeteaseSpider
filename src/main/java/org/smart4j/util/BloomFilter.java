package org.smart4j.util;

import java.util.BitSet;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by ithink on 17-9-12.
 */
public class BloomFilter {

    private final static int DEFAULT_SIZE = 2 << 24;
    private final static int[] seeds = new int[]{7, 11, 13, 31, 37, 61};
    private BitSet bitSet = new BitSet(DEFAULT_SIZE);
    private Hash[] hashes = new Hash[seeds.length];
    private Lock lock = new ReentrantLock();

    public BloomFilter(){
        for(int i=0; i<seeds.length; i++){
            hashes[i] = new Hash(DEFAULT_SIZE, seeds[i]);
        }
    }

    public void add(String value){
        if(value == null)return ;

        try {
            lock.lock();
            for (int i = 0; i < hashes.length; i++) {
                bitSet.set(hashes[i].hash(value), true);
            }
        }finally {
            lock.unlock();
        }

    }

    public boolean contains(String value){
        if(value == null)return false;

        try {
            lock.lock();
            for (Hash hash : hashes) {
                if (!bitSet.get(hash.hash(value))) return false;
            }
        }finally {
            lock.unlock();
        }

        return true;
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

}
