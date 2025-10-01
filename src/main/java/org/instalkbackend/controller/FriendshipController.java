package org.instalkbackend.controller;

import org.instalkbackend.mapper.UserMapper;
import org.instalkbackend.model.vo.FriendVO;
import org.instalkbackend.model.vo.Result;
import org.instalkbackend.service.FriendshipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/friendship")
public class FriendshipController {

    @Autowired
    private FriendshipService friendshipService;
    @Autowired
    private UserMapper userMapper;
    @PostMapping("/send")
    public Result sendFriendshipRequest(@RequestParam String username) {
        if (username == null) return Result.error("用户名为空");
        if (userMapper.selectByUsername(username) == null) return Result.error("用户不存在");
        return friendshipService.sendFriendshipRequest(username);
    }

    @PostMapping("/accept")
    public Result acceptFriendshipRequest(@RequestParam String username) {
        if (username == null) return Result.error("用户名为空");
        if (userMapper.selectByUsername(username) == null) return Result.error("用户不存在");
        return friendshipService.acceptFriendshipRequest(username);
    }

    @PostMapping("/reject")
    public Result rejectFriendshipRequest(@RequestParam String username) {
        if (username == null) return Result.error("用户名为空");
        if (userMapper.selectByUsername(username) == null) return Result.error("用户不存在");
        return friendshipService.rejectFriendshipRequest(username);
    }

    @PostMapping("/delete")
    public Result deleteFriendship(@RequestParam String username) {
        if (username == null) return Result.error("用户名为空");
        if (userMapper.selectByUsername(username) == null) return Result.error("用户不存在");
        return friendshipService.deleteFriendship(username);
    }

    @GetMapping("/friendList")
    public Result<List<FriendVO>> getFriendList() {
        return friendshipService.getFriendList();
    }

    @GetMapping("/pendingList")
    public Result<List<FriendVO>> getPendingList() {
        return friendshipService.getPendingList();
    }


}
