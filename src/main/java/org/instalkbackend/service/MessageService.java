package org.instalkbackend.service;

import org.instalkbackend.model.dto.MessageDTO;
import org.instalkbackend.model.vo.MessageVO;
import org.instalkbackend.model.vo.Result;

import java.util.List;

public interface MessageService {
    Result<MessageVO> sendMessage(MessageDTO messageDTO);

    Result<List<MessageVO>> getMessageList();
}
