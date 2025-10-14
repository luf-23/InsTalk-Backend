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
    public Result sendFriendshipRequest(@RequestParam Long id) {
        if (id == null) return Result.error("用户id为空");
        if (userMapper.selectById( id) == null) return Result.error("用户不存在");
        return friendshipService.sendFriendshipRequest(id);
    }

    @PostMapping("/accept")
    public Result<FriendVO> acceptFriendshipRequest(@RequestParam Long id) {
        if (id == null) return Result.error("用户id为空");
        if (userMapper.selectById( id) == null) return Result.error("用户不存在");
        return friendshipService.acceptFriendshipRequest(id);
    }

    @PostMapping("/reject")
    public Result rejectFriendshipRequest(@RequestParam Long id) {
        if (id == null) return Result.error("用户id为空");
        if (userMapper.selectById( id) == null) return Result.error("用户不存在");
        return friendshipService.rejectFriendshipRequest(id);
    }

    @PostMapping("/delete")
    public Result deleteFriendship(@RequestParam Long id) {
        if (id == null) return Result.error("用户id为空");
        if (userMapper.selectById( id) == null) return Result.error("用户不存在");
        return friendshipService.deleteFriendship(id);
    }

    @GetMapping("/friendList")
    public Result<List<FriendVO>> getFriendList() {
        return friendshipService.getFriendList();
    }

    @GetMapping("/pendingList")
    public Result<List<FriendVO>> getPendingList() {
        return friendshipService.getPendingList();
    }

    @GetMapping("/search")
    public Result<List<FriendVO>> searchFriend(@RequestParam String username) {
        return friendshipService.searchByUsername(username);
    }

}
