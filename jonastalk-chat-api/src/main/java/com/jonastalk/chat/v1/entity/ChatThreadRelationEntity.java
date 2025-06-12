package com.jonastalk.chat.v1.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Index;
import javax.persistence.Table;

import com.jonastalk.chat.v1.entity.id.ChatThreadRelationId;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @name ChatThreadRelationEntity.java
 * @brief `t_chat_thread_r` Table Entity
 * @author Jonas Lim
 * @date 2025.06.09
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@IdClass(ChatThreadRelationId.class)
@Table(
    name = "t_chat_thread_r",
    indexes = {
        @Index(name = "idx_chat_thread_r_n01", columnList = "CHAT_THREAD_ID")
    }
)
@ApiModel(description = "Chat Thread Relation")
public class ChatThreadRelationEntity {

    @Id
    @Column(name = "CHAT_ID", length = 42, nullable = false)
    @ApiModelProperty(notes = "Chat Room Identifier")
    private String chatId;

    @Id
    @Column(name = "CHAT_MESSAGE_ID", length = 42, nullable = false)
    @ApiModelProperty(notes = "Chat Message Identifier")
    private String chatMessageId;

    @Column(name = "CHAT_THREAD_ID", length = 42, nullable = false)
    @ApiModelProperty(notes = "Chat Thread Identifier")
    private String chatThreadId;
}
