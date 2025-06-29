package com.jonastalk.chat.v1.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Index;
import javax.persistence.Table;

import com.jonastalk.chat.v1.entity.id.ChatParticipantListId;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @name ChatParticipantListEntity.java
 * @brief `t_chat_chat_participant_l` Table Entity
 * @author Jonas Lim
 * @date 2025.06.09
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@IdClass(ChatParticipantListId.class)
@Table(
    name = "t_chat_chat_participant_l",
    indexes = {
        @Index(name = "idx_chat_chat_participant_l_n01", columnList = "USER_ID, LAST_PARTICIPATION_DATETIME")
    }
)
@ApiModel(description = "Chat Participant List")
public class ChatParticipantListEntity {

    @Id
    @Column(name = "USER_ID", length = 42, nullable = false)
    @ApiModelProperty(notes = "User Identifier")
    private String userId;

    @Id
    @Column(name = "CHAT_ID", length = 42, nullable = false)
    @ApiModelProperty(notes = "Chat Room Identifier")
    private String chatId;

    @Column(name = "ENTRANCE_DATETIME", nullable = false,
    		insertable = false, updatable = false,
    		columnDefinition = "DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6)")
    @ApiModelProperty(notes = "Entrance Datetime")
    private LocalDateTime entranceDatetime;
    
    @Column(name = "ENTRANCE_STATUS_CODE", length = 1, nullable = false)
    @ApiModelProperty(notes = "Entrance Status Code (e.g., W: WAIT, R: REJECT, A: APPROVE)")
    private String entranceStatusCode;

    @Column(name = "LAST_PARTICIPATION_DATETIME", nullable = false,
    		insertable = false, updatable = false,
            columnDefinition = "DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6)")
    @ApiModelProperty(notes = "Last Participation Datetime")
    private LocalDateTime lastParticipationDatetime;

    @Column(name = "ANNOUNCEMENT_CHECK_YN", nullable = false)
    @ApiModelProperty(notes = "Whether the user checked the latest announcement")
    private boolean announcementCheckYn;
}
