package org.instalkbackend.service.impl;

import org.instalkbackend.mapper.UserMapper;
import org.instalkbackend.model.dto.LoginDTO;
import org.instalkbackend.model.po.User;
import org.instalkbackend.model.vo.LoginVO;
import org.instalkbackend.model.vo.Result;
import org.instalkbackend.service.AuthService;
import org.instalkbackend.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public Result<LoginVO> login(LoginDTO loginDTO) {
        User user = loginDTO.getUsername() != null ? userMapper.selectByUsername(loginDTO.getUsername()) : userMapper.selectByEmail(loginDTO.getEmail());
        if (user == null) return Result.error("用户不存在");
        else if (!user.getPassword().equals(loginDTO.getPassword())) return Result.error("密码错误");
        String jti = UUID.randomUUID().toString();
        Map<String, Object> claims = Map.of("id",user.getId(),"username",user.getUsername(),"jti",jti,"role",user.getRole());
        String accessToken = JwtUtil.genAccessToken(claims);
        String refreshToken = JwtUtil.genRefreshToken(claims);
        return Result.success(new LoginVO(user,accessToken,refreshToken));
    }
}
