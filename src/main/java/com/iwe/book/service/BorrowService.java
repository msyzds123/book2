package com.iwe.book.service;

import com.iwe.book.bean.Borrow;
import com.iwe.book.bean.TreeMenu;

import java.util.List;

public interface BorrowService {
    List<Borrow> borrowList();

    int count();

    void returnBook(String borrowid);

    Borrow queryById(String borrowid);

    boolean deleteBorrow(String borrowid);

    boolean queryIsReturn(String ids);

    List<TreeMenu> queryTreeMenus();

    boolean addBorrow(Borrow borrow);
}
