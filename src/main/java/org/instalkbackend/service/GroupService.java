package org.instalkbackend.service;

import org.instalkbackend.model.dto.GroupDTO;
import org.instalkbackend.model.vo.GroupVO;
import org.instalkbackend.model.vo.Result;

import java.util.List;

public interface GroupService {
    Result createGroup(GroupDTO groupDTO);

    Result joinGroup(Long groupId);

    Result<List<GroupVO>> getMyGroupListAndMembers();

    Result<List<GroupVO>> getGroupListAndMembers();

    Result<List<GroupVO>> search(String nameLike);
}
