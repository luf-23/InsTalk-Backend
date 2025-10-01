package org.instalkbackend.service.impl;

import org.instalkbackend.mapper.FriendshipMapper;
import org.instalkbackend.mapper.UserMapper;
import org.instalkbackend.model.po.Friendship;
import org.instalkbackend.model.vo.Result;
import org.instalkbackend.service.FriendshipService;
import org.instalkbackend.util.ThreadLocalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class FriendshipServiceImpl implements FriendshipService {

    @Autowired
    private FriendshipMapper friendshipMapper;
    @Autowired
    private UserMapper userMapper;

    @Override
    public Result sendFriendshipRequest(String username) {
        Map<String,Object> claims = ThreadLocalUtil.get();
        String myUsername = (String) claims.get("username");
        if (myUsername.equals(username)) return Result.error("不能添加自己为好友");
        Long myId = userMapper.selectByUsername(myUsername).getId();
        Long id = userMapper.selectByUsername(username).getId();
        Long id1 = Math.min(myId,id);
        Long id2 = Math.max(myId,id);
        Friendship friendship = friendshipMapper.selectByUserId1AndUserId2(id1,id2);
        if (friendship != null){
            if (friendship.getStatus().equals("PENDING")) return Result.error("好友申请已存在");
            else if (friendship.getStatus().equals("BLOCKED")) return Result.error("你已被拉入黑名单或对方被你拉入了黑名单");
            else return Result.error("已经是好友");
        }
        friendshipMapper.addRequest(id1,id2);
        return Result.success();
    }

    @Override
    public Result acceptFriendshipRequest(String username) {
        Map<String,Object> claims = ThreadLocalUtil.get();
        String myUsername = (String) claims.get("username");
        if (myUsername.equals(username)) return Result.error("不能添加自己为好友");
        Long myId = userMapper.selectByUsername(myUsername).getId();
        Long id = userMapper.selectByUsername(username).getId();
        Long id1 = Math.min(myId,id);
        Long id2 = Math.max(myId,id);
        Friendship friendship = friendshipMapper.selectByUserId1AndUserId2(id1,id2);
        if (friendship == null) return Result.error("好友申请不存在");
        else{
            if (friendship.getStatus().equals("BLOCKED")) return Result.error("你已被拉入黑名单或对方被你拉入了黑名单");
            else if (friendship.getStatus().equals("ACCEPTED")) return Result.error("已经是好友");
        }
        friendshipMapper.acceptRequest(id1,id2);
        return Result.success();
    }

    @Override
    public Result rejectFriendshipRequest(String username) {
        Map<String,Object> claims = ThreadLocalUtil.get();
        String myUsername = (String) claims.get("username");
        if (myUsername.equals(username)) return Result.error("操作失败");
        Long myId = userMapper.selectByUsername(myUsername).getId();
        Long id = userMapper.selectByUsername(username).getId();
        Long id1 = Math.min(myId,id);
        Long id2 = Math.max(myId,id);
        Friendship friendship = friendshipMapper.selectByUserId1AndUserId2(id1,id2);
        if (friendship == null) return Result.error("好友申请不存在");
        else{
            if (friendship.getStatus().equals("ACCEPTED")) return Result.error("已经是好友");
            else if (friendship.getStatus().equals("BLOCKED")) return Result.error("你已被拉入黑名单或对方被你拉入了黑名单");
        }
        friendshipMapper.rejectRequest(id1,id2);
        return  Result.success();
    }

    @Override
    public Result deleteFriendshipRequest(String username) {
        Map<String,Object> claims = ThreadLocalUtil.get();
        String myUsername = (String) claims.get("username");
        if (myUsername.equals(username)) return Result.error("操作失败");
        Long myId = userMapper.selectByUsername(myUsername).getId();
        Long id = userMapper.selectByUsername(username).getId();
        Long id1 = Math.min(myId,id);
        Long id2 = Math.max(myId,id);
        Friendship friendship = friendshipMapper.selectByUserId1AndUserId2(id1,id2);
        if (friendship == null) return Result.error("好友不存在");
        else{
            if (friendship.getStatus().equals("PENDING")) return Result.error("好友不存在");
            else if (friendship.getStatus().equals("BLOCKED")) return Result.error("你已被拉入黑名单或对方被你拉入了黑名单");
        }
        friendshipMapper.deleteRequest(id1,id2);
        return  Result.success();
    }


}
