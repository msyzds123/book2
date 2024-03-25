package com.iwe.book.bean;

import lombok.Data;

import java.util.List;

@Data
public class TreeMenu {

    private String id;//节点编号
    private String text;//节点文本
    private boolean expanded;//是否折叠

    private List<TreeMenu> children;//当前节点的子节点
}
