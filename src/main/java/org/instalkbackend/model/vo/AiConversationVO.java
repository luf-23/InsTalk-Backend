package org.instalkbackend.model.vo;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.instalkbackend.model.po.AiConversation;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class AiConversationVO {
    private Long id;
    private Long robotId;
    private String title;
    private String summary;
    private LocalDateTime lastMessageAt;
    private LocalDateTime createdAt;

    public AiConversationVO(AiConversation aiConversation){
        this.id = aiConversation.getId();
        this.robotId = aiConversation.getRobotId();
        this.title = aiConversation.getTitle();
        this.summary = aiConversation.getSummary();
        this.lastMessageAt = aiConversation.getLastMessageAt();
        this.createdAt = aiConversation.getCreatedAt();
    }
}
