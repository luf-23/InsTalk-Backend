package org.instalkbackend.controller;

import org.instalkbackend.model.po.User;
import org.instalkbackend.model.vo.Result;
import org.instalkbackend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/info")
    public Result<User> getInfo(@RequestParam Long id) {
        if (id==null) return Result.error("id不能为空");
        return userService.getInfo(id);
    }

}
