package org.instalkbackend.service.impl;

import jakarta.servlet.http.HttpServletResponse;
import org.instalkbackend.mapper.FriendshipMapper;
import org.instalkbackend.mapper.UserAiConfigMapper;
import org.instalkbackend.mapper.UserMapper;
import org.instalkbackend.model.dto.LoginDTO;
import org.instalkbackend.model.dto.RegisterDTO;
import org.instalkbackend.model.po.User;
import org.instalkbackend.model.vo.LoginVO;
import org.instalkbackend.model.vo.RefreshVO;
import org.instalkbackend.model.vo.Result;
import org.instalkbackend.service.AuthService;
import org.instalkbackend.util.JwtUtil;
import org.instalkbackend.util.SMTPUtil;
import org.instalkbackend.util.ThreadLocalUtil;
import org.instalkbackend.util.TokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private UserAiConfigMapper userAiConfigMapper;
    @Autowired
    private FriendshipMapper friendshipMapper;
    @Autowired
    private TokenUtil tokenUtil;
    @Autowired
    private SMTPUtil smtpUtil;

    @Override
    public Result<LoginVO> login(LoginDTO loginDTO) {
        User user = loginDTO.getUsername() != null ? userMapper.selectByUsername(loginDTO.getUsername()) : userMapper.selectByEmail(loginDTO.getEmail());
        if (user == null) return Result.error("用户不存在");
        else if (!user.getPassword().equals(loginDTO.getPassword())) return Result.error("密码错误");
        String jti = UUID.randomUUID().toString().replace("-", "");
        Map<String, Object> claims = Map.of("id",user.getId(),"username",user.getUsername(),"jti",jti,"role",user.getRole());
        String accessToken = JwtUtil.genAccessToken(claims);
        String refreshToken = JwtUtil.genRefreshToken(claims);
        return Result.success(new LoginVO(user,accessToken,refreshToken));
    }

    @Override
    public Result logout() {
        Map<String,Object> claims = ThreadLocalUtil.get();
        String jti = (String) claims.get("jti");
        tokenUtil.add(jti);
        return Result.success();
    }

    @Override
    public Result<RefreshVO> refresh(String refreshToken, HttpServletResponse response) {
        Map<String,Object> claims;
        try{
            claims = JwtUtil.parseToken(refreshToken);
        }catch(Exception e){
            response.setStatus(401);
            return Result.error("token刷新失败");
        }
        String jti = (String) claims.get("jti");
        tokenUtil.add(jti);
        String newJti = UUID.randomUUID().toString().replace("-", "");
        claims.put("jti",newJti);
        String accessToken = JwtUtil.genAccessToken(claims);
        String newRefreshToken = JwtUtil.genRefreshToken(claims);
        return Result.success(new RefreshVO(accessToken,newRefreshToken));
    }

    @Override
    public Result register(RegisterDTO registerDTO) {
        if (registerDTO.getUsername() == null) return Result.error("用户名不能为空");
        if (registerDTO.getEmail() == null) return Result.error("邮箱不能为空");
        if (registerDTO.getPassword() == null) return Result.error("密码不能为空");
        if (userMapper.selectByUsername(registerDTO.getUsername()) != null) return Result.error("用户名已存在");
        if (userMapper.selectByEmail(registerDTO.getEmail()) != null) return Result.error("邮箱已存在");
        if (!smtpUtil.verifyCaptcha(registerDTO.getEmail(),registerDTO.getCaptcha())) return Result.error("验证码错误");
        User user = new User(registerDTO.getUsername(),registerDTO.getEmail(),registerDTO.getPassword());
        userMapper.add(user);
        // 添加机器人
        User robot = new User();
        robot.setSignature(String.format("我是%s的ai助手",user.getUsername()));
        robot.setUsername(String.format("%s的ai助手",user.getUsername()));
        robot.setEmail(String.format("%s@robot.com",user.getEmail()));
        robot.setAvatar("https://luf-23.oss-cn-wuhan-lr.aliyuncs.com/ins_talk/ai.png");
        robot.setRole("ROBOT");
        robot.setPassword(user.getPassword());
        userMapper.addRobot(robot);
        userAiConfigMapper.add(user.getId(),robot.getId());
        Long minId = Long.min(user.getId(),robot.getId());
        Long maxId = Long.max(user.getId(),robot.getId());
        friendshipMapper.makeFriendsWithRobot(minId,maxId);

        return Result.success();
    }

    @Override
    public Result sendCaptcha(String email) {
        if (email == null) return Result.error("邮箱不能为空");
        if (userMapper.selectByEmail(email) != null) return Result.error("邮箱已存在");
        smtpUtil.sendCaptcha(email);
        return Result.success();
    }
}
