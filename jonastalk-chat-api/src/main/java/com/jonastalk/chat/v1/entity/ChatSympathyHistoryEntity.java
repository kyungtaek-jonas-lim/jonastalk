package com.jonastalk.chat.v1.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Index;
import javax.persistence.Table;

import com.jonastalk.chat.v1.entity.id.ChatSympathyHistoryId;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @name ChatSympathyHistoryEntity.java
 * @brief `t_chat_message_sympathy_h` Table Entity
 * @author Jonas Lim
 * @date 2025.06.09
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@IdClass(ChatSympathyHistoryId.class)
@Table(
    name = "t_chat_message_sympathy_h",
    indexes = {
        @Index(name = "idx_chat_message_sympathy_h_n01", columnList = "USER_ID"),
        @Index(name = "idx_chat_message_sympathy_h_n02", columnList = "REGISTRATION_DATETIME"),
        @Index(name = "idx_chat_message_sympathy_h_n03", columnList = "SYMPARHY_CODE")
    }
)
@ApiModel(description = "Chat Message Sympathy History")
public class ChatSympathyHistoryEntity {

    @Id
    @Column(name = "CHAT_ID", length = 42, nullable = false)
    @ApiModelProperty(notes = "Chat Room Identifier")
    private String chatId;

    @Id
    @Column(name = "CHAT_MESSAGE_ID", length = 42, nullable = false)
    @ApiModelProperty(notes = "Chat Message Identifier")
    private String chatMessageId;

    @Id
    @Column(name = "USER_ID", length = 42, nullable = false)
    @ApiModelProperty(notes = "User Identifier")
    private String userId;

    @Column(name = "REGISTRATION_DATETIME", nullable = false, columnDefinition = "DATETIME(6)")
    @ApiModelProperty(notes = "Sympathy Registration Datetime")
    private LocalDateTime registrationDatetime;

    @Column(name = "SYMPARHY_CODE", length = 10, nullable = false)
    @ApiModelProperty(notes = "Sympathy Code")
    private String sympathyCode;
}
