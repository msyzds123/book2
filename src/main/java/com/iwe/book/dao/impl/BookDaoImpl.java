package com.iwe.book.dao.impl;

import cn.hutool.core.util.StrUtil;
import com.iwe.book.bean.Book;
import com.iwe.book.bean.BookType;
import com.iwe.book.dao.BookDao;
import com.iwe.book.util.DruidUtil;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BookDaoImpl implements BookDao {

    private QueryRunner qr =
            new QueryRunner(DruidUtil.getDataSource());

    @Override
    public List<Book> bookList(int pageIndex,int pageSize,String bookname,String booktype) {
        List<Book> bookList = null;
        int a = pageIndex * pageSize;
        String sql = "SELECT t.bookid,t.bookname,t.publisher,t.author,t.remain,tb.tname " +
                "as booktype FROM `tb_books` t " +
                "left join tb_booktype tb on t.booktype=tb.tid where 1=1";
        try {
            Map<String,String> params = new HashMap<>();
            if(StrUtil.isNotEmpty(bookname)){
                params.put("bookname","%" + bookname + "%");
                sql = sql + " and bookname like ?";
            }
            if(StrUtil.isNotEmpty(booktype)){
                params.put("booktype",booktype);
                sql = sql + " and booktype=?";
            }
            sql = sql + " limit " + a + "," + pageSize;
            if(params.size() > 0){
                Object[] objects = params.values().toArray();
                bookList = qr.query(sql,new BeanListHandler<>(Book.class),objects);
            }else{
                bookList =
                        qr.query(sql, new BeanListHandler<>(Book.class));
            }
            return bookList;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public int count() {
        String sql = "select count(*) from tb_books";
        try {
            Number number = (Number) qr.query(sql,new ScalarHandler<>());
            return number.intValue();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public List<BookType> queryAllBookTypes() {
        String sql = "select * from tb_booktype";
        try {
            List<BookType> bookTypes =
                    qr.query(sql, new BeanListHandler<>(BookType.class));
            return bookTypes;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean repeatBook(String author,String bookname) {
       //查询当前作者的所有图书
        String sql = "select  * from tb_books where author=?";
        try {
            List<Book> books =
                    qr.query(sql, new BeanListHandler<>(Book.class), author);
            for (Book book : books) {
                String bookname1 = book.getBookname();
                if(bookname1.equals(bookname)){
                    return true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean addBook(Book book) {
        String sql = "insert into tb_books values(?,?,?,?,?,?)";
        try {
            int count = qr.update(sql,book.getBookid(),book.getBookname(),book.getPublisher()
            ,book.getAuthor(),book.getBooktype(),book.getRemain());
            return count > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public Book queryById(String bookid) {
        String sql = "select * from tb_books where bookid=?";
        try {
            Book book =
                qr.query(sql, new BeanHandler<>(Book.class), bookid);
            return book;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean updateBook(Book book) {
        String sql = "update tb_books set bookname=?,publisher=?,author=?" +
                ",booktype=?,remain=? where bookid=?";
        try {
            int count = qr.update(sql,book.getBookname(),book.getPublisher(),book.getAuthor()
            ,book.getBooktype(),book.getRemain(),book.getBookid());
            return count > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean deleteBatch(String ids) {
        String[] split = ids.split(",");

        boolean deleteOk = true;
        for (String s : split) {
            String sql = "delete from tb_books where bookid=?";
            try {
                int count = qr.update(sql,s);
                if(count <= 0){
                    deleteOk = false;
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return deleteOk;
    }

    @Override
    public boolean updateBookNum(String bookid) {
        String sql = "update tb_books set remain=remain+1 where bookid=?";
        try {
            int count= qr.update(sql,bookid);
            return count > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean isBorrow(String bookid) {
        String sql = "select remain from tb_books where bookid=?";
        try {
            Number number = (Number) qr.query(sql,new ScalarHandler<>(),bookid);
            return number.intValue() > 1;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public void reduceBookNum(String bookid) {
        String sql = "update tb_books set remain=remain-1 where bookid=?";
        try {
            int update = qr.update(sql, bookid);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
