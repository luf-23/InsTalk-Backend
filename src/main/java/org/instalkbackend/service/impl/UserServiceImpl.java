package org.instalkbackend.service.impl;

import org.instalkbackend.mapper.UserMapper;
import org.instalkbackend.model.po.User;
import org.instalkbackend.model.vo.Result;
import org.instalkbackend.service.UserService;
import org.instalkbackend.util.ThreadLocalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public Result<User> getInfo() {
        Long id = ThreadLocalUtil.getId();
        User user = userMapper.selectById(id);
        user.setPassword(null);
        return Result.success(user);
    }
}
