package org.instalkbackend.controller;

import org.instalkbackend.mapper.GroupMemberMapper;
import org.instalkbackend.mapper.MessageMapper;
import org.instalkbackend.model.dto.MessageDTO;
import org.instalkbackend.model.vo.MessageVO;
import org.instalkbackend.model.vo.Result;
import org.instalkbackend.service.MessageService;
import org.instalkbackend.util.ThreadLocalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/message")
public class MessageController {

    @Autowired
    private MessageService messageService;
    @Autowired
    private GroupMemberMapper groupMemberMapper;
    @Autowired
    private MessageMapper messageMapper;

    @PostMapping("/send")
    public Result<MessageVO> sendMessage(@RequestBody MessageDTO messageDTO){
        if (messageDTO == null) return Result.error("参数为空");
        return messageService.sendMessage(messageDTO);
    }

    @GetMapping("/messageList")
    public Result<List<MessageVO> > getMessageList(){
        return messageService.getMessageList();
    }


    @PostMapping("/newMessageList")
    public Result<List<MessageVO>> getNewMessageList(@RequestBody MessageVO lastMessage){
        if (lastMessage == null) Result.success();
        Long myId = ThreadLocalUtil.getId();
        if (lastMessage.getSenderId()!=myId &&
                lastMessage.getReceiverId()!=myId &&
                groupMemberMapper.select(myId,lastMessage.getGroupId())==null
        ) Result.error("参数错误");
        return messageService.getNewMessageList(lastMessage.getId());
    }

    @PostMapping("/read")
    public Result readMessage(@RequestParam Long messageId){
        if (messageId == null) return Result.error("参数错误");
        if (messageMapper.selectById(messageId)==null) return Result.error("消息不存在");
        return messageService.readMessage(messageId);
    }

    @PostMapping("/readList")
    public Result readMessageList(@RequestParam List<Long> messageIds){
        if (messageIds==null) return Result.error("参数错误");
        return messageService.readMessageList(messageIds);
    }
}
