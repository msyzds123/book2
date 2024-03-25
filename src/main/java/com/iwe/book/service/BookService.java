package com.iwe.book.service;

import com.iwe.book.bean.Book;
import com.iwe.book.bean.BookType;

import java.util.List;

public interface BookService {
    List<Book> bookList(int pageIndex,int pageSize,String bookname,String booktype);

    int count();

    List<BookType> queryAllBookTypes();

    boolean repeatBook(String author,String bookname);

    boolean addBook(Book book);

    Book queryById(String bookid);

    boolean updateBook(Book book);

    boolean deleteBatch(String ids);

    boolean updateBookNum(String bookid);

    boolean isBorrow(String bookid);

    void reduceBookNum(String bookid);
}
