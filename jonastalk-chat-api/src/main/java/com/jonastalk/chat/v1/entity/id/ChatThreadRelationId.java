package com.jonastalk.chat.v1.entity.id;

import java.io.Serializable;
import java.util.Objects;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

/**
 * @name  implements.java
 * @brief `t_chat_thread_r` Table id class
 * @author Jonas Lim
 * @date 2025.06.09
 */
@NoArgsConstructor
@AllArgsConstructor
public class ChatThreadRelationId implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private String chatId;
    private String chatMessageId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ChatThreadRelationId)) return false;
        ChatThreadRelationId that = (ChatThreadRelationId) o;
        return Objects.equals(chatId, that.chatId) &&
               Objects.equals(chatMessageId, that.chatMessageId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(chatId, chatMessageId);
    }
}
