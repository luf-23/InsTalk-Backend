package org.instalkbackend.controller;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.Pattern;
import org.instalkbackend.model.dto.LoginDTO;
import org.instalkbackend.model.dto.RegisterDTO;
import org.instalkbackend.model.vo.LoginVO;
import org.instalkbackend.model.vo.RefreshVO;
import org.instalkbackend.model.vo.Result;
import org.instalkbackend.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

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

    @PostMapping("/logout")
    public Result logout(){
        return authService.logout();
    }

    @PostMapping("/refresh")
    public Result<RefreshVO> refresh(@RequestBody Map<String,String> requestBody, HttpServletResponse response){
        String refreshToken = requestBody.get("refreshToken");
        return authService.refresh(refreshToken,response);
    }

    @PostMapping("/register")
    public Result register(@RequestBody RegisterDTO registerDTO){
        if (registerDTO == null) return Result.error("表单为空");
        if (!registerDTO.getUsername().matches("^\\S{5,16}$")
                ||!registerDTO.getPassword().matches("^\\S{5,16}$")
                ||!registerDTO.getEmail().matches("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$"))
            return Result.error("参数错误");
        return authService.register(registerDTO);
    }

    @GetMapping("/captcha")
    public Result captcha(@RequestParam String email){
        return authService.sendCaptcha(email);
    }

}
