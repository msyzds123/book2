package com.iwe.book.util;

import org.apache.commons.beanutils.BeanUtils;

import java.util.Map;

public class FormBeanUtil {

    public static <T> T formToBean(Map<String,String[]> map,
                        Class<T> tClass){
        T t= null;
        try {
            t = tClass.newInstance();
            BeanUtils.populate(t,map);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return t;
    }
}
