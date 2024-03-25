package com.iwe.book.controller;

import cn.hutool.json.JSONUtil;
import com.iwe.book.bean.ResultVo;
import com.iwe.book.bean.User;
import com.iwe.book.service.UserService;
import com.iwe.book.service.impl.UserServiceImpl;
import com.iwe.book.util.FormBeanUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/user")
public class UserServlet extends BaseServlet{

    private UserService userService =
            new UserServiceImpl();

    public void login(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ResultVo resultVo = new ResultVo();
       //获取用户输入验证码
        String code = req.getParameter("code");

        //获取正确的验证码
        HttpSession session = req.getSession();
        String correctCode = (String) session.getAttribute("code");
        if(!correctCode.equalsIgnoreCase(code)){
            resultVo.setMess("验证码错误");
        }else{
            User user = FormBeanUtil.formToBean(req.getParameterMap(), User.class);
            user = userService.login(user);

            if(user == null){
                resultVo.setMess("用户名或密码错误");
            }else{
                resultVo.setOk(true);
                session.setAttribute("user",user);
            }
        }
        resp.getWriter().write(JSONUtil.toJsonStr(resultVo));
    }

    //异步校验用户名是否被注册过
    public void verifyUsername(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        boolean isExists =
                userService.verifyUsername(req.getParameter("username"));
        ResultVo resultVo = new ResultVo();
        if(isExists){
            resultVo.setOk(true);
            resultVo.setMess("用户已被注册");
        }
        resp.getWriter().write(JSONUtil.toJsonStr(resultVo));
    }

    //异步注册
    public void regist(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User user =
                FormBeanUtil.formToBean(req.getParameterMap(), User.class);
        boolean addOK = userService.addUser(user);
        ResultVo resultVo = new ResultVo();
        if(addOK){
            resultVo.setOk(true);
            resultVo.setMess("注册成功^_^!!!");
        }
        resp.getWriter().write(JSONUtil.toJsonStr(resultVo));
    }

    //异步获取用户数据
    public void getInfo(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User user = (User) req.getSession().getAttribute("user");
        ResultVo<User> resultVo = new ResultVo();
        resultVo.setT(user);
        resp.getWriter().write(JSONUtil.toJsonStr(resultVo));
    }

    //登出
    public void logOut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getSession().removeAttribute("user");
        resp.sendRedirect("/book/login.html");
    }
}
