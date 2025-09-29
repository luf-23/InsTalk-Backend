package org.instalkbackend.controller;

import org.instalkbackend.model.dto.LoginDTO;
import org.instalkbackend.model.vo.LoginVO;
import org.instalkbackend.model.vo.Result;
import org.instalkbackend.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public Result<LoginVO> login(@RequestBody LoginDTO loginDTO){
        if (loginDTO == null) return Result.error("参数错误");
        if (loginDTO.getEmail()== null&& loginDTO.getUsername()== null) return Result.error("参数错误");
        if (loginDTO.getPassword()== null) return Result.error("参数错误");
        return authService.login(loginDTO);
    }

}
