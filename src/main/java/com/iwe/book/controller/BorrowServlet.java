package com.iwe.book.controller;

import cn.hutool.json.JSONUtil;
import com.iwe.book.bean.*;
import com.iwe.book.service.BookService;
import com.iwe.book.service.BorrowService;
import com.iwe.book.service.impl.BookServiceImpl;
import com.iwe.book.service.impl.BorrowServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/borrow")
public class BorrowServlet extends BaseServlet{

    private BorrowService borrowService =
            new BorrowServiceImpl();

    private BookService bookService =
            new BookServiceImpl();

    //借阅信息列表
    public void borrowList(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<Borrow> data = borrowService.borrowList();
        int total = borrowService.count();
        PageResult pageResult = new PageResult();
        pageResult.setTotal(total);
        pageResult.setData(data);
        resp.getWriter().write(JSONUtil.toJsonStr(pageResult));
    }

    //还书
    public void returnBook(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String borrowid = req.getParameter("borrowid");
        //更改还书状态
        borrowService.returnBook(borrowid);
        //更改图书数量
        Borrow borrow = borrowService.queryById(borrowid);
        boolean updateOk = bookService.updateBookNum(borrow.getBookid());
        ResultVo resultVo = new ResultVo();
        if(updateOk){
            resultVo.setOk(true);
            resultVo.setMess("还书成功");
        }
        resp.getWriter().write(JSONUtil.toJsonStr(resultVo));
    }

    //删除借阅信息
    public void deleteBorrow(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String borrowid = req.getParameter("borrowid");
        //更改还书状态
        boolean deleteOk = borrowService.deleteBorrow(borrowid);
        ResultVo resultVo = new ResultVo();
        if(deleteOk){
            resultVo.setOk(true);
            resultVo.setMess("删除成功");
        }
        resp.getWriter().write(JSONUtil.toJsonStr(resultVo));
    }

    //异步查询图书种类和种类下的所有图书信息树
    public void queryTreeMenus(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<TreeMenu> treeMenus =
                borrowService.queryTreeMenus();
        resp.getWriter().write(JSONUtil.toJsonStr(treeMenus));
    }

    //保存借阅信息
    public void addBorrow(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String data = req.getParameter("data");
        Borrow borrow = JSONUtil.toBean(data, Borrow.class);
        //判断图书库存是否>1
        ResultVo resultVo = new ResultVo();
        boolean isBorrow = bookService.isBorrow(borrow.getBookid());
        if(isBorrow){
            resultVo.setOk(true);
            boolean addOk = borrowService.addBorrow(borrow);
            if(addOk){
                resultVo.setMess("添加成功");
                //更新图书库存
                bookService.reduceBookNum(borrow.getBookid());
            }
        }else{
            resultVo.setMess("库存不足");
        }
        resp.getWriter().write(JSONUtil.toJsonStr(resultVo));
    }
}
