package com.iwe.book.dao.impl;

import com.iwe.book.bean.User;
import com.iwe.book.dao.UserDao;
import com.iwe.book.util.DruidUtil;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.sql.SQLException;


public class UserDaoImpl implements UserDao {

    private QueryRunner qr =
            new QueryRunner(DruidUtil.getDataSource());

    @Override
    public User login(User user) {
        String sql = "select * from tb_user where username=? and password=?";
        try {
            user = qr.query(sql,new BeanHandler<>(User.class),
                    user.getUsername(),user.getPassword());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }

    @Override
    public boolean verifyUsername(String username) {
        String sql = "select count(*) from tb_user where username=?";
        try {
            Number number = (Number) qr.query(sql,new ScalarHandler<>(),username);
            return number.intValue() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean addUser(User user) {
        String sql = "insert into tb_user(uid,username,password) values(?,?,?)";
        try {
            int update = qr.update(sql, user.getUid(), user.getUsername(), user.getPassword());
            return update > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
