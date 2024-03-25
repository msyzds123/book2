package com.iwe.book.util;

import java.util.UUID;

public class UUIDUtil {

    public static String uuid(){
        return UUID.randomUUID().toString().replace("-","");
    }
}
