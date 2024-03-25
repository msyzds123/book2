package com.iwe.book.test;

import com.iwe.book.util.MD5Util;
import org.junit.Test;

import java.util.UUID;

public class TestBook {

    @Test
    public void test01(){
        String s = UUID.randomUUID().toString()
                .replace("-","");

        //ip+系统时间...:36 4个-
        System.out.println(s);
    }

    @Test
    public void test02(){
        String admin = MD5Util.getMD5("admin");

        System.out.println(admin);
    }

}
