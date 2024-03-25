package com.iwe.book.service.impl;

import com.iwe.book.bean.Book;
import com.iwe.book.bean.BookType;
import com.iwe.book.dao.BookDao;
import com.iwe.book.dao.impl.BookDaoImpl;
import com.iwe.book.service.BookService;
import com.iwe.book.util.UUIDUtil;

import java.util.List;
import java.util.UUID;

public class BookServiceImpl implements BookService {
    private BookDao bookDao =
            new BookDaoImpl();

    @Override
    public List<Book> bookList(int pageIndex,int pageSize,String bookname,String booktype) {
        return bookDao.bookList(pageIndex,pageSize,bookname,booktype);
    }

    @Override
    public int count() {
        return bookDao.count();
    }

    @Override
    public List<BookType> queryAllBookTypes() {
        return bookDao.queryAllBookTypes();
    }

    @Override
    public boolean repeatBook(String author,String bookname) {
        return bookDao.repeatBook(author,bookname);
    }

    @Override
    public boolean addBook(Book book) {
        book.setBookid(UUIDUtil.uuid());
        return bookDao.addBook(book);
    }

    @Override
    public Book queryById(String bookid) {
        return bookDao.queryById(bookid);
    }

    @Override
    public boolean updateBook(Book book) {
        return bookDao.updateBook(book);
    }

    @Override
    public boolean deleteBatch(String ids) {
        return bookDao.deleteBatch(ids);
    }

    @Override
    public boolean updateBookNum(String bookid) {
        return bookDao.updateBookNum(bookid);
    }

    @Override
    public boolean isBorrow(String bookid) {
        return bookDao.isBorrow(bookid);
    }

    @Override
    public void reduceBookNum(String bookid) {
        bookDao.reduceBookNum(bookid);
    }
}
