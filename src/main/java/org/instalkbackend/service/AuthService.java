package org.instalkbackend.service;

import jakarta.servlet.http.HttpServletResponse;
import org.instalkbackend.model.dto.LoginDTO;
import org.instalkbackend.model.dto.RegisterDTO;
import org.instalkbackend.model.vo.LoginVO;
import org.instalkbackend.model.vo.RefreshVO;
import org.instalkbackend.model.vo.Result;

public interface AuthService {
    Result<LoginVO> login(LoginDTO loginDTO);

    Result logout();

    Result<RefreshVO> refresh(String refreshToken, HttpServletResponse response);

    Result register(RegisterDTO registerDTO);

    Result sendCaptcha(String email);
}
