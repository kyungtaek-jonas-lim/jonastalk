package com.jonastalk.chat.v1.entity.id;

import java.io.Serializable;
import java.util.Objects;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

/**
 * @name ChatThreadBookmarkListId.java
 * @brief `t_chat_thread_bookmark_l` Table id class
 * @author Jonas Lim
 * @date 2025.06.09
 */
@NoArgsConstructor
@AllArgsConstructor
public class ChatThreadBookmarkListId implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String userId;
    private String chatId;
    private String chatThreadId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ChatThreadBookmarkListId)) return false;
        ChatThreadBookmarkListId that = (ChatThreadBookmarkListId) o;
        return Objects.equals(userId, that.userId) &&
               Objects.equals(chatId, that.chatId) &&
               Objects.equals(chatThreadId, that.chatThreadId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, chatId, chatThreadId);
    }
}
