package com.iwe.book.service.impl;

import com.iwe.book.bean.Borrow;
import com.iwe.book.bean.TreeMenu;
import com.iwe.book.dao.BorrowDao;
import com.iwe.book.dao.impl.BorrowDaoImpl;
import com.iwe.book.service.BorrowService;
import com.iwe.book.util.UUIDUtil;

import java.util.List;

public class BorrowServiceImpl implements BorrowService {

    private BorrowDao borrowDao =
            new BorrowDaoImpl();

    @Override
    public List<Borrow> borrowList() {
        return borrowDao.borrowList();
    }

    @Override
    public int count() {
        return borrowDao.count();
    }

    @Override
    public void returnBook(String borrowid) {
        borrowDao.returnBook(borrowid);
    }

    @Override
    public Borrow queryById(String borrowid) {
        return borrowDao.queryById(borrowid);
    }

    @Override
    public boolean deleteBorrow(String borrowid) {
        return borrowDao.deleteBorrow(borrowid);
    }

    @Override
    public boolean queryIsReturn(String ids) {
        return borrowDao.queryIsReturn(ids);
    }

    @Override
    public List<TreeMenu> queryTreeMenus() {
        return borrowDao.queryTreeMenus();
    }

    @Override
    public boolean addBorrow(Borrow borrow) {
        borrow.setBorrowid(UUIDUtil.uuid());
        borrow.setIsreturn("0");
        return borrowDao.addBorrow(borrow);
    }
}
