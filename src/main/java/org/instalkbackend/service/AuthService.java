package org.instalkbackend.service;

import org.instalkbackend.model.dto.LoginDTO;
import org.instalkbackend.model.vo.LoginVO;
import org.instalkbackend.model.vo.Result;

public interface AuthService {
    Result<LoginVO> login(LoginDTO loginDTO);
}
