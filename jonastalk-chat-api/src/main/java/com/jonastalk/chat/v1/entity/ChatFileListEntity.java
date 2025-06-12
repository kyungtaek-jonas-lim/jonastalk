package com.jonastalk.chat.v1.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Index;
import javax.persistence.Table;

import com.jonastalk.chat.v1.entity.id.ChatFileListId;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @name ChatFileListEntity.java
 * @brief `t_chat_file_l` Table Entity
 * @author Jonas Lim
 * @date 2025.06.09
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@IdClass(ChatFileListId.class)
@Table(
    name = "t_chat_file_l",
    indexes = {
        @Index(name = "idx_chat_file_l_n01", columnList = "FILE_NAME")
    }
)
@ApiModel(description = "Chat File List")
public class ChatFileListEntity {

    @Id
    @Column(name = "CHAT_ID", length = 42, nullable = false)
    @ApiModelProperty(notes = "Chat Room Identifier")
    private String chatId;

    @Id
    @Column(name = "CHAT_MESSAGE_ID", length = 42, nullable = false)
    @ApiModelProperty(notes = "Chat Message Identifier")
    private String chatMessageId;

    @Column(name = "FILE_NAME", length = 100, nullable = false)
    @ApiModelProperty(notes = "File Name")
    private String fileName;

    @Column(name = "FILE_URL", length = 500, nullable = false)
    @ApiModelProperty(notes = "File URL (S3 or other storage)")
    private String fileUrl;

    @Column(name = "FILE_SIZE", nullable = false)
    @ApiModelProperty(notes = "File Size")
    private Integer fileSize;
}
