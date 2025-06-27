package com.jonastalk.chat.v1.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
/**
 * @name ChatChatDetailEntity.java
 * @brief `t_chat_chat_d` Table Entity
 * @author Jonas Lim
 * @date 2025.06.09
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(
    name = "t_chat_chat_d",
    indexes = {
        @Index(name = "idx_chat_chat_d_n01", columnList = "CHAT_MANAGER_USER_ID, CHAT_MANAGER_MODIFICATION_DATETIME"),
        @Index(name = "idx_chat_chat_d_n02", columnList = "TITLE"),
        @Index(name = "idx_chat_chat_d_n03", columnList = "INVITATION_LINK")
    }
)
@ApiModel(description = "Chat Detail")
public class ChatChatDetailEntity {

    @Id
    @Column(name = "CHAT_ID", length = 42, nullable = false)
    @ApiModelProperty(notes = "Chat Room Identifier")
    private String chatId;

    @Column(name = "CREATER_USER_ID", length = 42, nullable = false)
    @ApiModelProperty(notes = "User ID who created the chat room")
    private String creatorUserId;

    @Column(name = "CREATER_DATETIME", nullable = false,
    		columnDefinition = "DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6)")
    @ApiModelProperty(notes = "Chat Room Creation Datetime")
    private LocalDateTime creationDatetime;

    @Column(name = "CHAT_MANAGER_USER_ID", length = 42, nullable = false)
    @ApiModelProperty(notes = "Current Chat Room Manager User ID")
    private String chatManagerUserId;

    @Column(name = "CHAT_MANAGER_MODIFICATION_DATETIME", nullable = false,
    		columnDefinition = "DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6)")
    @ApiModelProperty(notes = "Datetime when the chat manager was last changed")
    private LocalDateTime chatManagerModificationDatetime;

    @Column(name = "CHAT_TYPE_CODE", length = 4, nullable = false)
    @ApiModelProperty(notes = "Chat Room Type Code")
    private String chatTypeCode;

    @Column(name = "CHAT_INFO_MODIFICATION_DATETIME", nullable = false,
    		columnDefinition = "DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6)")
    @ApiModelProperty(notes = "Datetime when the chat room info was last modified")
    private LocalDateTime chatInfoModificationDatetime;

    @Column(name = "TITLE", length = 20, nullable = false)
    @ApiModelProperty(notes = "Chat Room Title")
    private String title;

    @Column(name = "INTRO", length = 255, nullable = false)
    @ApiModelProperty(notes = "Chat Room Introduction")
    private String intro;

    @Column(name = "PASSWORD", length = 255, nullable = false)
    @ApiModelProperty(notes = "Chat Room Password")
    private String password;

    @Column(name = "ANNOUNCEMENT_YN", nullable = false)
    @ApiModelProperty(notes = "Whether this chat room is for announcements only")
    private boolean announcementYn;

    @Column(name = "PHOTO_EXISTENCE_YN", nullable = false)
    @ApiModelProperty(notes = "Whether a photo exists for the chat room")
    private boolean photoExistenceYn;

    @Column(name = "INVITATION_LINK", length = 500, nullable = false)
    @ApiModelProperty(notes = "Invitation Link to Join the Chat Room")
    private String invitationLink;

    // Getters, Setters, utility methods, etc.
}