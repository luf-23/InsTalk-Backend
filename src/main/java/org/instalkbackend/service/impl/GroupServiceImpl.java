package org.instalkbackend.service.impl;

import org.instalkbackend.mapper.ChatGroupMapper;
import org.instalkbackend.mapper.GroupMember;
import org.instalkbackend.model.dto.GroupDTO;
import org.instalkbackend.model.po.ChatGroup;
import org.instalkbackend.model.vo.GroupVO;
import org.instalkbackend.model.vo.Result;
import org.instalkbackend.service.GroupService;
import org.instalkbackend.util.ThreadLocalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class GroupServiceImpl implements GroupService {

    @Autowired
    private ChatGroupMapper chatGroupMapper;
    @Autowired
    private GroupMember groupMemberMapper;

    @Override
    public Result createGroup(GroupDTO groupDTO) {
        Long ownerId = ThreadLocalUtil.getId();
        ChatGroup chatGroup = new ChatGroup();
        chatGroup.setName(groupDTO.getName());
        chatGroup.setDescription(groupDTO.getDescription());
        chatGroup.setOwnerId(ownerId);
        chatGroupMapper.add(chatGroup);
        groupMemberMapper.addOwner(ownerId, chatGroup.getId());
        return Result.success();
    }

    @Override
    public Result joinGroup(Long groupId) {
        Long userId = ThreadLocalUtil.getId();
        if (groupMemberMapper.select(userId, groupId)!=null) return Result.error("您已加入该群");
        groupMemberMapper.addMember(userId, groupId);
        return Result.success();
    }

    @Override
    public Result<List<GroupVO>> getMyGroupListAndMembers() {
        Long userId = ThreadLocalUtil.getId();
        List<GroupVO> groupVOS = chatGroupMapper.selectByOwnerId(userId).stream().map(chatGroup -> {
            GroupVO groupVO = new GroupVO();
            groupVO.setId(chatGroup.getId());
            groupVO.setName(chatGroup.getName());
            groupVO.setDescription(chatGroup.getDescription());
            groupVO.setAvatar(chatGroup.getAvatar());
            groupVO.setOwnerId(chatGroup.getOwnerId());
            groupVO.setCreatedAt(chatGroup.getCreatedAt());
            groupVO.setAdminIds(groupMemberMapper.selectAdminIdsByGroupId(chatGroup.getId()));
            groupVO.setMembers(groupMemberMapper.selectMembersByGroupId(chatGroup.getId()));
            return groupVO;
        }).toList();
        return Result.success(groupVOS);
    }

    @Override
    public Result<List<GroupVO>> getGroupListAndMembers() {
        Long userId = ThreadLocalUtil.getId();
        List<GroupVO> groupVOS = groupMemberMapper.selectGroupIdIfIam(userId).stream().map(groupId -> {
            GroupVO groupVO = new GroupVO();
            groupVO.setId(groupId);
            ChatGroup chatGroup = chatGroupMapper.selectById(groupId);
            groupVO.setName(chatGroup.getName());
            groupVO.setDescription(chatGroup.getDescription());
            groupVO.setAvatar(chatGroup.getAvatar());
            groupVO.setOwnerId(chatGroup.getOwnerId());
            groupVO.setCreatedAt(chatGroup.getCreatedAt());
            groupVO.setAdminIds(groupMemberMapper.selectAdminIdsByGroupId(groupId));
            groupVO.setMembers(groupMemberMapper.selectMembersByGroupId(groupId));
            return groupVO;
        }).toList();
        return Result.success(groupVOS);
    }

    @Override
    public Result<List<GroupVO>> search(String nameLike) {
        List<GroupVO> groupVOS = chatGroupMapper.selectByNameLike(nameLike).stream().map(chatGroup -> {
            GroupVO groupVO = new GroupVO();
            groupVO.setId(chatGroup.getId());
            groupVO.setName(chatGroup.getName());
            groupVO.setDescription(chatGroup.getDescription());
            groupVO.setAvatar(chatGroup.getAvatar());
            groupVO.setOwnerId(chatGroup.getOwnerId());
            groupVO.setCreatedAt(chatGroup.getCreatedAt());
            groupVO.setAdminIds(groupMemberMapper.selectAdminIdsByGroupId(chatGroup.getId()));
            groupVO.setMembers(groupMemberMapper.selectMembersByGroupId(chatGroup.getId()));
            return groupVO;
        }).toList();
        return Result.success(groupVOS);
    }
}
