package com.jonastalk.chat.v1.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Index;
import javax.persistence.Lob;
import javax.persistence.Table;

import com.jonastalk.chat.v1.entity.id.ChatLongMessageListId;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @name ChatLongMessageListEntity.java
 * @brief `t_chat_long_message_l` Table Entity
 * @author Jonas Lim
 * @date 2025.06.09
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@IdClass(ChatLongMessageListId.class)
@Table(
    name = "t_chat_long_message_l",
    indexes = {
        @Index(name = "idx_chat_long_message_l_n01", columnList = "CHAT_CONTENT")
    }
)
@ApiModel(description = "Chat Long Message List")
public class ChatLongMessageListEntity {

    @Id
    @Column(name = "CHAT_ID", length = 42, nullable = false)
    @ApiModelProperty(notes = "Chat Room Identifier")
    private String chatId;

    @Id
    @Column(name = "CHAT_MESSAGE_ID", length = 42, nullable = false)
    @ApiModelProperty(notes = "Chat Message Identifier")
    private String chatMessageId;

    @Lob
    @Column(name = "CHAT_CONTENT", nullable = false, columnDefinition = "MEDIUMTEXT")
    @ApiModelProperty(notes = "Long Chat Message Content")
    private String chatContent;
}
