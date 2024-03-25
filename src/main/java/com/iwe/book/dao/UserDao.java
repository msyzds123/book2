package com.iwe.book.dao;

import com.iwe.book.bean.User;

public interface UserDao {
    User login(User user);

    boolean verifyUsername(String username);

    boolean addUser(User user);
}
