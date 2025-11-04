package org.instalkbackend.controller;

import org.instalkbackend.mapper.UserMapper;
import org.instalkbackend.model.dto.UserDTO;
import org.instalkbackend.model.po.User;
import org.instalkbackend.model.vo.Result;
import org.instalkbackend.model.vo.UserInfoVO;
import org.instalkbackend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private UserMapper userMapper;

    @GetMapping("/info")
    public Result<UserInfoVO> getInfo(@RequestParam Long id) {
        if (id==null) return Result.error("id不能为空");
        User user = userMapper.selectById(id);
        if (user==null) return Result.error("用户不存在");
        return userService.getInfo(user);
    }

    @PostMapping("/update")
    public Result update(@RequestBody UserDTO userDTO){
        return userService.update(userDTO);
    }

}
