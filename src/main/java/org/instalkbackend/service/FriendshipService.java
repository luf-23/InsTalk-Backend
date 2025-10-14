package org.instalkbackend.service;

import org.instalkbackend.model.vo.FriendVO;
import org.instalkbackend.model.vo.Result;

import java.util.List;

public interface FriendshipService {
    Result sendFriendshipRequest(Long id);

    Result<FriendVO> acceptFriendshipRequest(Long id);

    Result rejectFriendshipRequest(Long id);

    Result deleteFriendship(Long id);

    Result<List<FriendVO>> getFriendList();

    Result<List<FriendVO>> getPendingList();

    Result<List<FriendVO>> searchByUsername(String username);
}
