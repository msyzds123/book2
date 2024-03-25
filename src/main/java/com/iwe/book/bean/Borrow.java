package com.iwe.book.bean;

import lombok.Data;

@Data
public class Borrow {

    private String borrowid;
    private String bookid;
    private String borrower;
    private String phone;
    private String borrowtime;
    private String returntime;
    private String isreturn;
}
