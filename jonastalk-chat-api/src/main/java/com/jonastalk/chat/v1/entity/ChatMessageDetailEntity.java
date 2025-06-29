package com.jonastalk.chat.v1.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Index;
import javax.persistence.Table;

import com.jonastalk.chat.v1.entity.id.ChatMessageDetailId;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @name ChatMessageDetailEntity.java
 * @brief `t_chat_message_d` Table Entity
 * @author Jonas Lim
 * @date 2025.06.09
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@IdClass(ChatMessageDetailId.class)
@Table(
    name = "t_chat_message_d",
    indexes = {
        @Index(name = "idx_chat_message_d_n01", columnList = "USER_ID"),
        @Index(name = "idx_chat_message_d_n02", columnList = "REGISTRATION_DATETIME")
    }
)
@ApiModel(description = "Chat Message Detail")
public class ChatMessageDetailEntity {

    @Id
    @Column(name = "CHAT_ID", length = 42, nullable = false)
    @ApiModelProperty(notes = "Chat Room Identifier")
    private String chatId;

    @Id
    @Column(name = "CHAT_MESSAGE_ID", length = 42, nullable = false)
    @ApiModelProperty(notes = "Chat Message Identifier")
    private String chatMessageId;

    @Column(name = "USER_ID", length = 42, nullable = false)
    @ApiModelProperty(notes = "User Identifier")
    private String userId;

    @Column(name = "REGISTRATION_DATETIME", nullable = false,
    		insertable = false, updatable = false,
    		columnDefinition = "DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6)")
    @ApiModelProperty(notes = "Message Registration Datetime")
    private LocalDateTime registrationDatetime;

    @Column(name = "CHAT_CONTENT", length = 500, nullable = false)
    @ApiModelProperty(notes = "Chat Message Content")
    private String chatContent;

    @Column(name = "CHAT_LONG_MESSAGE_YN", nullable = false)
    @ApiModelProperty(notes = "Whether this is a long message")
    private boolean chatLongMessageYn;

    @Column(name = "CHAT_FILE_EXISTENCE_YN", nullable = false)
    @ApiModelProperty(notes = "Whether a file exists in the message")
    private boolean chatFileExistenceYn;

    @Column(name = "PHOTO_EXISTENCE_YN", nullable = false)
    @ApiModelProperty(notes = "Whether a photo exists in the message")
    private boolean photoExistenceYn;

    @Column(name = "PHOTO_URL", length = 500, nullable = false)
    @ApiModelProperty(notes = "URL of the photo in the message (S3 or other storage)")
    private String photoUrl;
}
