package org.instalkbackend.controller;

import org.instalkbackend.model.dto.AiChatDTO;
import org.instalkbackend.model.dto.UserAiConfigDTO;
import org.instalkbackend.model.vo.AiConversationVO;
import org.instalkbackend.model.vo.AiMessageVO;
import org.instalkbackend.model.vo.Result;
import org.instalkbackend.service.AiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;

@RestController
@RequestMapping("/ai")
public class AiController {

    @Autowired
    private AiService aiService;

    @GetMapping("/credential")
    public Result<String> getCredential() {
        return aiService.getCredential();
    }

    @PostMapping("/update")
    public Result update(@RequestBody UserAiConfigDTO userAiConfigDTO){
        if (userAiConfigDTO == null) return Result.error("参数错误");
        if (userAiConfigDTO.getRobotId() == null) return Result.error("参数错误");
        return aiService.update(userAiConfigDTO);
    }

    @GetMapping("/conversationList")
    public Result<List<AiConversationVO>> getConversationList(@RequestParam Long robotId){
        return aiService.getConversationList(robotId);
    }

    @GetMapping("/messageList")
    public Result<List<AiMessageVO>> getMessageList(@RequestParam Long conversationId){
        return aiService.getMessageList(conversationId);
    }


    @PostMapping("/chat-stream")
    public SseEmitter streamChat(@RequestBody AiChatDTO aiChatDTO){
        return aiService.streamChat(aiChatDTO);
    }
}
