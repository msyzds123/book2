package com.iwe.book.service;

import com.iwe.book.bean.User;

public interface UserService {
    User login(User user);

    boolean verifyUsername(String username);

    boolean addUser(User user);
}
