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
 * @name ChatPhotoRelationEntity.java
 * @brief `t_chat_chat_photo_r` Table Entity
 * @author Jonas Lim
 * @date 2025.06.09
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(
    name = "t_chat_chat_photo_r",
    indexes = {
        @Index(name = "idx_chat_chat_photo_r_n01", columnList = "REGISTRATION_DATETIME")
    }
)
@ApiModel(description = "Chat Photo Relation")
public class ChatPhotoRelationEntity {

    @Id
    @Column(name = "CHAT_ID", length = 42, nullable = false)
    @ApiModelProperty(notes = "Chat Room Identifier")
    private String chatId;

    @Column(name = "PHOTO_URL", length = 500, nullable = false)
    @ApiModelProperty(notes = "URL of the Chat Room Photo (S3 or other storage)")
    private String photoUrl;

    @Column(name = "REGISTRATION_DATETIME", nullable = false, columnDefinition = "DATETIME(6)")
    @ApiModelProperty(notes = "Photo Registration Datetime")
    private LocalDateTime registrationDatetime;
}
