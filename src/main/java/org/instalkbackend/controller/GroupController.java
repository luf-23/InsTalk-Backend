package org.instalkbackend.controller;

import org.instalkbackend.model.dto.GroupDTO;
import org.instalkbackend.model.vo.GroupVO;
import org.instalkbackend.model.vo.Result;
import org.instalkbackend.service.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/group")
public class GroupController {

    @Autowired
    private GroupService groupService;

    @PostMapping("/create")
    public Result createGroup(@RequestBody GroupDTO groupDTO){
        return groupService.createGroup(groupDTO);
    }

    @PostMapping("join")
    public Result joinGroup(@RequestParam Long groupId){
        return groupService.joinGroup(groupId);
    }

    @GetMapping("/myGroupList")
    public Result<List<GroupVO>> getGroupListAndMembers(){
        return groupService.getMyGroupListAndMembers();
    }

    @GetMapping("/groupList")
    public Result<List<GroupVO>> getGroupList(){
        return groupService.getGroupListAndMembers();
    }

    @GetMapping("/search")
    public Result<List<GroupVO>> search(@RequestParam String nameLike){
        return groupService.search(nameLike);
    }

}
