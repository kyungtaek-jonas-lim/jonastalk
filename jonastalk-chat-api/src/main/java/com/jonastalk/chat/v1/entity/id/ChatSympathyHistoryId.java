package com.jonastalk.chat.v1.entity.id;

import java.io.Serializable;
import java.util.Objects;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

/**
 * @name ChatSympathyHistoryId.java
 * @brief `t_chat_message_sympathy_h` Table id class
 * @author Jonas Lim
 * @date 2025.06.09
 */
@NoArgsConstructor
@AllArgsConstructor
public class ChatSympathyHistoryId implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String chatId;
    private String chatMessageId;
    private String userId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ChatSympathyHistoryId)) return false;
        ChatSympathyHistoryId that = (ChatSympathyHistoryId) o;
        return Objects.equals(chatId, that.chatId) &&
               Objects.equals(chatMessageId, that.chatMessageId) &&
               Objects.equals(userId, that.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(chatId, chatMessageId, userId);
    }
}
