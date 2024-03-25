package com.iwe.book.bean;

import lombok.Data;

import java.util.List;

@Data
public class PageResult<T> {

    private int total;//满足条件的记录数
    private List<T> data;//每页的数据
}
