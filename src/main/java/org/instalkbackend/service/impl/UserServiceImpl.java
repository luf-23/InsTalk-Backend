package org.instalkbackend.service.impl;

import org.instalkbackend.mapper.UserMapper;
import org.instalkbackend.model.dto.UserDTO;
import org.instalkbackend.model.po.User;
import org.instalkbackend.model.vo.Result;
import org.instalkbackend.model.vo.UserInfoVO;
import org.instalkbackend.service.UserService;
import org.instalkbackend.util.ThreadLocalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public Result<UserInfoVO> getInfo(User user) {
        UserInfoVO userInfoVO = new UserInfoVO(user);
        return Result.success(userInfoVO);
    }

    @Override
    public Result update(UserDTO userDTO) {
        Long id = ThreadLocalUtil.getId();
        User user = userMapper.selectById(id);
        User newUser = new User(user,userDTO);
        userMapper.update(newUser);
        return Result.success();
    }
}
