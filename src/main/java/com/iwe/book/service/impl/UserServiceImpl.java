package com.iwe.book.service.impl;

import com.iwe.book.bean.User;
import com.iwe.book.dao.UserDao;
import com.iwe.book.dao.impl.UserDaoImpl;
import com.iwe.book.service.UserService;
import com.iwe.book.util.MD5Util;
import com.iwe.book.util.UUIDUtil;

public class UserServiceImpl implements UserService {

    private UserDao userDao = new UserDaoImpl();

    @Override
    public User login(User user) {
        user.setPassword(MD5Util.getMD5(user.getPassword()));
        return userDao.login(user);
    }

    @Override
    public boolean verifyUsername(String username) {
        return userDao.verifyUsername(username);
    }

    @Override
    public boolean addUser(User user) {
        user.setUid(UUIDUtil.uuid());
        user.setPassword(MD5Util.getMD5(user.getPassword()));
        return userDao.addUser(user);
    }
}
