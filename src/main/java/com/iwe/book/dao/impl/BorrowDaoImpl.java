package com.iwe.book.dao.impl;

import com.iwe.book.bean.Book;
import com.iwe.book.bean.BookType;
import com.iwe.book.bean.Borrow;
import com.iwe.book.bean.TreeMenu;
import com.iwe.book.dao.BorrowDao;
import com.iwe.book.service.BorrowService;
import com.iwe.book.util.DateUtil;
import com.iwe.book.util.DruidUtil;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class BorrowDaoImpl implements BorrowDao {

    private QueryRunner qr =
            new QueryRunner(DruidUtil.getDataSource());

    @Override
    public List<Borrow> borrowList() {
        String sql = "select bw.borrowid,b.bookname as " +
                "bookid,bw.borrower,bw.phone,bw.borrowtime,bw.returntime," +
                "bw.isreturn from " +
                "tb_borrowinfo bw left join tb_books " +
                "b on bw.bookid=b.bookid order by borrowtime desc";
        try {
            List<Borrow> borrowList =
                    qr.query(sql, new BeanListHandler<>(Borrow.class));
            return borrowList;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public int count() {
        String sql = "select count(*) from tb_borrowinfo";
        try {
            Number number = (Number) qr.query(sql,new ScalarHandler<>());
            return number.intValue();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public void returnBook(String borrowid) {
        String returntime = DateUtil.dateToString(new Date());
        String sql = "update tb_borrowinfo set isreturn=1,returntime=? where borrowid=? ";
        try {
            int update = qr.update(sql, returntime,borrowid);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Override
    public Borrow queryById(String borrowid) {
        String sql = "select * from tb_borrowinfo where borrowid=?";
        try {
            Borrow borrow = qr.query(sql, new BeanHandler<>(Borrow.class), borrowid);
            return borrow;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean deleteBorrow(String borrowid) {
        String sql = "delete from tb_borrowinfo where borrowid=?";
        try {
            int count = qr.update(sql,borrowid);
            return  count > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean queryIsReturn(String ids) {
        String[] split = ids.split(",");


        //true:已归还
        boolean isReturn = true;
        for (String s : split) {
            String sql = "select * from tb_borrowinfo where bookid=?";
            try {
                List<Borrow> borrowList =
                        qr.query(sql, new BeanListHandler<>(Borrow.class), s);
                for (Borrow borrow : borrowList) {
                    if(borrow.getIsreturn().equals("0")){
                        isReturn = false;
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return isReturn;
    }

    @Override
    public List<TreeMenu> queryTreeMenus() {
        //查询出所有图书种类
        String sql = "select * from tb_booktype";
        List<TreeMenu> treeMenus = new ArrayList<>();
        try {
            List<BookType> bookTypes =
                    qr.query(sql, new BeanListHandler<>(BookType.class));
            for (BookType bookType : bookTypes) {
                TreeMenu treeMenu = new TreeMenu();
                treeMenu.setId(bookType.getTid());
                treeMenu.setText(bookType.getTname());
                treeMenu.setExpanded(false);

                //根据图书种类编号查询出当前种类下的所有图书
                String tid = bookType.getTid();
                String sql1 = "select * from tb_books where booktype=?";
                List<Book> books =
                        qr.query(sql1, new BeanListHandler<>(Book.class), tid);

                List<TreeMenu> children = new ArrayList<>();
                for (Book book : books) {
                    TreeMenu son = new TreeMenu();
                    son.setId(book.getBookid());
                    son.setText(book.getBookname());
                    son.setExpanded(false);
                    children.add(son);
                }
                treeMenu.setChildren(children);
                treeMenus.add(treeMenu);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return treeMenus;
    }

    @Override
    public boolean addBorrow(Borrow borrow) {
        String sql = "insert into tb_borrowinfo(borrowid,bookid,borrower," +
                "phone,borrowtime,isreturn) values(?,?,?,?,?,?)";
        try {
            int count = qr.update(sql,borrow.getBorrowid(),borrow.getBookid(),borrow.getBorrower()
            ,borrow.getPhone(),borrow.getBorrowtime(),borrow.getIsreturn());
            return  count > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
