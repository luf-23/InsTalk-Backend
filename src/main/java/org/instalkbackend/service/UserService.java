package org.instalkbackend.service;

import org.instalkbackend.model.dto.UserDTO;
import org.instalkbackend.model.po.User;
import org.instalkbackend.model.vo.Result;
import org.instalkbackend.model.vo.UserInfoVO;

public interface UserService {
    Result<UserInfoVO> getInfo(User user);

    Result update(UserDTO userDTO);
}
