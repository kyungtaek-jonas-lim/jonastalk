package com.jonastalk.chat.v1.entity.id;

import java.io.Serializable;
import java.util.Objects;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

/**
 * @name ChatParticipantListId.java
 * @brief `t_chat_chat_participant_l` Table id class
 * @author Jonas Lim
 * @date 2025.06.09
 */
@NoArgsConstructor
@AllArgsConstructor
public class ChatParticipantListId implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String userId;
    private String chatId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ChatParticipantListId)) return false;
        ChatParticipantListId that = (ChatParticipantListId) o;
        return Objects.equals(userId, that.userId) &&
               Objects.equals(chatId, that.chatId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, chatId);
    }
}
