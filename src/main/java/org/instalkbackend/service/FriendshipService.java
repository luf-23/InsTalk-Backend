package org.instalkbackend.service;

import org.instalkbackend.model.vo.FriendVO;
import org.instalkbackend.model.vo.Result;

import java.util.List;

public interface FriendshipService {
    Result sendFriendshipRequest(String username);

    Result acceptFriendshipRequest(String username);

    Result rejectFriendshipRequest(String username);

    Result deleteFriendship(String username);

    Result<List<FriendVO>> getFriendList();

    Result<List<FriendVO>> getPendingList();
}
