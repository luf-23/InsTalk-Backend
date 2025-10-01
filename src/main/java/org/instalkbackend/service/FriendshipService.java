package org.instalkbackend.service;

import org.instalkbackend.model.vo.Result;

public interface FriendshipService {
    Result sendFriendshipRequest(String username);

    Result acceptFriendshipRequest(String username);

    Result rejectFriendshipRequest(String username);

    Result deleteFriendshipRequest(String username);
}
