package com.jonastalk.chat.v1.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Index;
import javax.persistence.Table;

import com.jonastalk.chat.v1.entity.id.ChatTagListId;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @name ChatTagListEntity.java
 * @brief `t_chat_chat_tag_l` Table Entity
 * @author Jonas Lim
 * @date 2025.06.09
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@IdClass(ChatTagListId.class)
@Table(
    name = "t_chat_chat_tag_l",
    indexes = {
        @Index(name = "idx_chat_chat_tag_l_n01", columnList = "TAG, CHAT_ID")
    }
)
@ApiModel(description = "Chat Tag List")
public class ChatTagListEntity {

    @Id
    @Column(name = "CHAT_ID", length = 42, nullable = false)
    @ApiModelProperty(notes = "Chat Room Identifier")
    private String chatId;

    @Id
    @Column(name = "TAG", length = 40, nullable = false)
    @ApiModelProperty(notes = "Tag")
    private String tag;

    @Column(name = "REGISTRATION_DATETIME", nullable = false, columnDefinition = "DATETIME(6)")
    @ApiModelProperty(notes = "Tag Registration Datetime")
    private LocalDateTime registrationDatetime;
}
