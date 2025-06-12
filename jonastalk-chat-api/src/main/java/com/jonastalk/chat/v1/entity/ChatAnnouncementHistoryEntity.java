package com.jonastalk.chat.v1.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

import com.jonastalk.chat.v1.entity.id.ChatAnnouncementHistoryId;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @name ChatAnnouncementHistoryEntity.java
 * @brief `t_chat_chat_announcement_h` Table Entity
 * @author Jonas Lim
 * @date 2025.06.09
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@IdClass(ChatAnnouncementHistoryId.class)
@Table(name = "t_chat_chat_announcement_h")
@ApiModel(description = "Chat Announcement History")
public class ChatAnnouncementHistoryEntity {

    @Id
    @Column(name = "CHAT_ID", length = 42, nullable = false)
    @ApiModelProperty(notes = "Chat Room Identifier")
    private String chatId;

    @Id
    @Column(name = "REGISTRATION_DATETIME", nullable = false, columnDefinition = "DATETIME(6)")
    @ApiModelProperty(notes = "Announcement Registration Datetime")
    private LocalDateTime registrationDatetime;

    @Column(name = "ANNOUNCEMENT_CONTENT", length = 200, nullable = false)
    @ApiModelProperty(notes = "Announcement Content")
    private String announcementContent;
}
