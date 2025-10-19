package org.instalkbackend.service.impl;

import org.instalkbackend.mapper.FriendshipMapper;
import org.instalkbackend.mapper.GroupMemberMapper;
import org.instalkbackend.mapper.MessageMapper;
import org.instalkbackend.mapper.MessageStatusMapper;
import org.instalkbackend.model.dto.MessageDTO;
import org.instalkbackend.model.po.Message;
import org.instalkbackend.model.vo.GroupVO;
import org.instalkbackend.model.vo.MessageVO;
import org.instalkbackend.model.vo.Result;
import org.instalkbackend.service.MessageService;
import org.instalkbackend.util.ThreadLocalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MessageServiceImpl implements MessageService {

    @Autowired
    private MessageMapper messageMapper;
    @Autowired
    private MessageStatusMapper messageStatusMapper;
    @Autowired
    private GroupMemberMapper groupMemberMapper;
    @Autowired
    private FriendshipMapper friendshipMapper;

    @Override
    public Result<MessageVO> sendMessage(MessageDTO messageDTO) {
        Long senderId = ThreadLocalUtil.getId();
        Message message = new Message();
        message.setSenderId(senderId);
        message.setContent(messageDTO.getContent());
        message.setMessageType(messageDTO.getMessageType());
        message.setReceiverId(messageDTO.getReceiverId());
        message.setGroupId(messageDTO.getGroupId());

        if (message.getReceiverId()!= null){
            if (!friendshipMapper.selectByUserId1AndUserId2(Long.min(senderId,message.getReceiverId()),Long.max(senderId,message.getReceiverId())).getStatus().equals("ACCEPTED")){
                return Result.error("请先添加对方为好友");
            }
            messageMapper.addPrivateMessage(message);
            messageStatusMapper.add(message.getId(),message.getReceiverId());
        }
        if (message.getGroupId()!= null){
            if(groupMemberMapper.select(senderId,message.getGroupId())== null){
                return Result.error("您不是群成员");
            }
            messageMapper.addGroupMessage(message);
            List<GroupVO.Member> receiverIds = groupMemberMapper.selectMembersByGroupId(message.getGroupId());
            for (GroupVO.Member member : receiverIds) {
                if (member.getId()==senderId) continue;
                messageStatusMapper.add(message.getId(),member.getId());
            }
        }
        message.setSentAt(messageMapper.selectSentAtById(message.getId()));
        MessageVO messageVO = new MessageVO(message,Boolean.TRUE);
        return Result.success(messageVO);
    }

    @Override
    public Result<List<MessageVO>> getMessageList() {
        Long userId = ThreadLocalUtil.getId();
        List<MessageVO> messageVOS1 = messageMapper.selectBySenderId(userId).stream().map(message -> {
            MessageVO messageVO = new MessageVO();
            messageVO.setId(message.getId());
            messageVO.setSenderId(message.getSenderId());
            messageVO.setReceiverId(message.getReceiverId());
            messageVO.setGroupId(message.getGroupId());
            messageVO.setContent(message.getContent());
            messageVO.setMessageType(message.getMessageType());
            messageVO.setSentAt(message.getSentAt());
            messageVO.setIsRead(Boolean.TRUE);
            return messageVO;
        }).toList();
        List<MessageVO> messageVOS2 = messageMapper.selectByReceiverId(userId).stream().map(message -> {
            MessageVO messageVO = new MessageVO();
            messageVO.setId(message.getId());
            messageVO.setSenderId(message.getSenderId());
            messageVO.setReceiverId(message.getReceiverId());
            messageVO.setGroupId(message.getGroupId());
            messageVO.setContent(message.getContent());
            messageVO.setMessageType(message.getMessageType());
            messageVO.setSentAt(message.getSentAt());
            messageVO.setIsRead(messageStatusMapper.select(message.getId(),userId));
            return messageVO;
        }).toList();
        List<MessageVO> messageVOS3 = messageMapper.selectGroupMessagesAsReceiver(userId).stream().map(message -> {
            MessageVO messageVO = new MessageVO();
            messageVO.setId(message.getId());
            messageVO.setSenderId(message.getSenderId());
            messageVO.setReceiverId(message.getReceiverId());
            messageVO.setGroupId(message.getGroupId());
            messageVO.setContent(message.getContent());
            messageVO.setMessageType(message.getMessageType());
            messageVO.setSentAt(message.getSentAt());
            messageVO.setIsRead(messageStatusMapper.select(message.getId(),userId));
            return messageVO;
        }).toList();
        List<MessageVO> messageVOS = new ArrayList<>();
        messageVOS.addAll(messageVOS1);
        messageVOS.addAll(messageVOS2);
        messageVOS.addAll(messageVOS3);
        return Result.success(messageVOS);
    }

    @Override
    public Result<List<MessageVO>> getNewMessageList(Long messageId) {
        Long userId = ThreadLocalUtil.getId();
        List<MessageVO> messageVOS1 = messageMapper.selectNewByReceiverId(userId,messageId).stream().map(message -> {
            MessageVO messageVO = new MessageVO();
            messageVO.setId(message.getId());
            messageVO.setSenderId(message.getSenderId());
            messageVO.setReceiverId(message.getReceiverId());
            messageVO.setGroupId(message.getGroupId());
            messageVO.setContent(message.getContent());
            messageVO.setMessageType(message.getMessageType());
            messageVO.setSentAt(message.getSentAt());
            messageVO.setIsRead(messageStatusMapper.select(message.getId(),userId));
            return messageVO;
        }).toList();
        List<MessageVO> messageVOS2 = messageMapper.selectNewGroupMessagesAsReceiver(userId,messageId).stream().map(message -> {
            MessageVO messageVO = new MessageVO();
            messageVO.setId(message.getId());
            messageVO.setSenderId(message.getSenderId());
            messageVO.setReceiverId(message.getReceiverId());
            messageVO.setGroupId(message.getGroupId());
            messageVO.setContent(message.getContent());
            messageVO.setMessageType(message.getMessageType());
            messageVO.setSentAt(message.getSentAt());
            messageVO.setIsRead(messageStatusMapper.select(message.getId(),userId));
            return messageVO;
        }).toList();
        List<MessageVO> messageVOS = new ArrayList<>();
        messageVOS.addAll(messageVOS1);
        messageVOS.addAll(messageVOS2);
        return Result.success(messageVOS);
    }

    @Override
    public Result readMessage(Long messageId) {
        Long userId = ThreadLocalUtil.getId();
        messageStatusMapper.updateToRead(messageId,userId);
        return Result.success();
    }

    @Override
    public Result readMessageList(List<Long> messageIds) {
        Long userId = ThreadLocalUtil.getId();
        messageStatusMapper.updateListToRead(userId,messageIds);
        return Result.success();
    }
}
