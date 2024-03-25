package com.iwe.book.controller;

import cn.hutool.json.JSONUtil;
import com.iwe.book.bean.Book;
import com.iwe.book.bean.BookType;
import com.iwe.book.bean.PageResult;
import com.iwe.book.bean.ResultVo;
import com.iwe.book.service.BookService;
import com.iwe.book.service.BorrowService;
import com.iwe.book.service.impl.BookServiceImpl;
import com.iwe.book.service.impl.BorrowServiceImpl;
import com.iwe.book.util.FormBeanUtil;
import org.w3c.dom.stylesheets.LinkStyle;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@WebServlet("/books")
public class BookServlet extends BaseServlet{

    private BookService bookService =
            new BookServiceImpl();

    private BorrowService borrowService =
            new BorrowServiceImpl();

    public void bookList(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String bookname = req.getParameter("bookname");
        String booktype = req.getParameter("booktype");

        //获取当前页
        int pageIndex = Integer.parseInt(req.getParameter("pageIndex"));

        //获取每页记录数
        int pageSize = Integer.parseInt(req.getParameter("pageSize"));

        List<Book> bookList = bookService.bookList(pageIndex,pageSize,bookname,booktype);
        //查询总记录数
        int total = bookService.count();

        PageResult<Book> pageResult = new PageResult<>();
        pageResult.setTotal(total);
        pageResult.setData(bookList);

        resp.getWriter().write(JSONUtil.toJsonStr(pageResult));
    }

    //查询所有图书种类
    public void queryAllBookTypes(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<BookType> bookTypes = bookService.queryAllBookTypes();
        resp.getWriter().write(JSONUtil.toJsonStr(bookTypes));
    }

    //异步添加图书
    public void addBook(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String data = req.getParameter("data");
        Book book = JSONUtil.toBean(data, Book.class);
        boolean repeat = bookService.repeatBook(book.getAuthor(),book.getBookname());
        ResultVo resultVo = new ResultVo();
        if(repeat){
            resultVo.setMess("作者书名重复");
        }else{
            boolean addOk = bookService.addBook(book);
            if(addOk){
                resultVo.setOk(true);
                resultVo.setMess("添加图书成功");
            }
        }
        resp.getWriter().write(JSONUtil.toJsonStr(resultVo));
    }

    //异步修改图书
    public void updateBook(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String data = req.getParameter("data");
        Book book = JSONUtil.toBean(data, Book.class);
        ResultVo resultVo = new ResultVo();
        boolean updateOk = bookService.updateBook(book);
        if(updateOk){
            resultVo.setOk(true);
            resultVo.setMess("修改图书成功");
        }
        resp.getWriter().write(JSONUtil.toJsonStr(resultVo));
    }

    //根据主键查询数据
    public void queryById(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String bookid = req.getParameter("bookid");
        Book book = bookService.queryById(bookid);
        resp.getWriter().write(JSONUtil.toJsonStr(book));
    }

    //批量删除
    public void deleteBatch(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String ids = req.getParameter("ids");

        /*判断被删除的图书有没有未归还的
        * */
        //1、查询图书对应的借阅信息的isreturn的状态
        ResultVo resultVo = new ResultVo();
        boolean isReturn = borrowService.queryIsReturn(ids);
        if(!isReturn){
            resultVo.setMess("有未归还的图书");
        }else{
            //图书全部归还完毕
            boolean deleteOk = bookService.deleteBatch(ids);
            if(deleteOk){
                resultVo.setOk(true);
                resultVo.setMess("删除成功");
            }
        }

        resp.getWriter().write(JSONUtil.toJsonStr(resultVo));
    }
}
