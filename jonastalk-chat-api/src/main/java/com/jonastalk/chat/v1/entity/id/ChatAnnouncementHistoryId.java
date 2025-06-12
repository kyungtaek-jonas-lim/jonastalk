package com.jonastalk.chat.v1.entity.id;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

/**
 * @name ChatAnnouncementHistoryId.java
 * @brief `t_chat_chat_announcement_h` Table id class
 * @author Jonas Lim
 * @date 2025.06.09
 */
@NoArgsConstructor
@AllArgsConstructor
public class ChatAnnouncementHistoryId implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String chatId;
    private LocalDateTime registrationDatetime;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ChatAnnouncementHistoryId)) return false;
        ChatAnnouncementHistoryId that = (ChatAnnouncementHistoryId) o;
        return Objects.equals(chatId, that.chatId) &&
               Objects.equals(registrationDatetime, that.registrationDatetime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(chatId, registrationDatetime);
    }
}
