package org.instalkbackend.service;

import org.instalkbackend.model.dto.UserDTO;
import org.instalkbackend.model.po.User;
import org.instalkbackend.model.vo.Result;

public interface UserService {
    Result<User> getInfo(Long id);

    Result update(UserDTO userDTO);
}
