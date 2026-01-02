package com.jonastalk.chat.v1.service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.jonastalk.chat.v1.api.field.ChatGetResponse;
import com.jonastalk.chat.v1.entity.ChatChatDetailEntity;
import com.jonastalk.chat.v1.entity.ChatParticipantListEntity;
import com.jonastalk.chat.v1.repository.ChatChatDetailRepository;
import com.jonastalk.chat.v1.repository.ChatParticipantListRepository;

import io.azam.ulidj.ULID;
import lombok.extern.slf4j.Slf4j;

/**
 * @name ChatService.java
 * @brief Chat Service
 * @author Jonas Lim
 * @date Jun 27, 2025
 */
@Slf4j
@Service
public class ChatService {
	
	@Autowired
	ChatParticipantListRepository chatParticipantListRepository;
	
	@Autowired
	ChatChatDetailRepository chatChatDetailRepository;

	/**
	 * @name createChat(String fromUserId, Set<String> toUserIds)
	 * @brief Create chat
	 * @author Jonas Lim
	 * @date June 27, 2025
	 * @param param
	 * @return
	 * @throws Exception
	 */
    @Transactional
	public String createChat(String fromUserId, Set<String> toUserIds) throws Exception {

		// --------------
		// 1. Validate Chat
    	toUserIds.add(fromUserId); // Add Sender
    	String chatId = findChatIdsWithExactParticipants(toUserIds);
    	if (StringUtils.hasText(chatId)) {
    		// Already exists
//            throw new CustomException(EnumErrorCode.ERR_CHAT001);
            return chatId;
    	}
    	
		// --------------
		// 2. Generate ChatId
    	int retryCnt = 1;
    	while (retryCnt < 3) {
        	chatId = ULID.random() + ULID.random().substring(10);
	    	Optional<ChatChatDetailEntity> entityOpt = chatChatDetailRepository.findById(chatId);
	    	if (!entityOpt.isPresent()) {
	    	    break;
	    	}
	    	retryCnt++;
    	}
    	
		// --------------
		// 3. Create Chat
    	ChatChatDetailEntity chatChatDetailEntity = ChatChatDetailEntity.builder()
        		.chatId(chatId)
        		.creatorUserId(fromUserId)
        		.chatManagerUserId(fromUserId)
        		.chatTypeCode("")
        		.title(toUserIds.toString())
        		.intro("")
        		.password("")
        		.announcementYn(false)
        		.photoExistenceYn(false)
        		.invitationLink("")
        		.build();
        chatChatDetailRepository.save(chatChatDetailEntity);
    	
		// --------------
		// 3. Save Participant
        
        // Inviter
        ChatParticipantListEntity chatParticipantListEntity = ChatParticipantListEntity.builder()
        		.chatId(chatId)
        		.userId(fromUserId)
        		.entranceStatusCode("A")
        		.build();
    	chatParticipantListRepository.save(chatParticipantListEntity);
    	
    	// Invitee
    	toUserIds.remove(fromUserId); // Remove Sender
    	for (String toUserId: toUserIds) {
            ChatParticipantListEntity entity = ChatParticipantListEntity.builder()
            		.chatId(chatId)
            		.userId(toUserId)
            		.entranceStatusCode("A") // @TODO: Change it to "W" and make an API for entrance approval
            		.build();
        	chatParticipantListRepository.save(entity);
    	}
        
        return chatId;
	}
	
	
	public String findChatIdsWithExactParticipants(Set<String> userIds) throws Exception {
    	return chatParticipantListRepository.findChatIdsWithExactParticipants(userIds, userIds.size());
	}
	
	
	public Map<String, Object> getChat(String userId, String chatId) throws Exception {

		ChatChatDetailEntity chatDetail = chatChatDetailRepository.findById(chatId)
			.orElseThrow(() -> new Exception("Chat not found: " + chatId));

		Map<String, Object> chatInfo = new HashMap<>();
		chatInfo.put(ChatGetResponse.CHAT_ID.getName(), chatDetail.getChatId());
		chatInfo.put(ChatGetResponse.CREATOR_USER_ID.getName(), chatDetail.getCreatorUserId());
		chatInfo.put(ChatGetResponse.CREATION_DATE_TIME.getName(), chatDetail.getCreationDatetime());
		chatInfo.put(ChatGetResponse.CHAT_MANAGER_USER_ID.getName(), chatDetail.getChatManagerUserId());
		chatInfo.put(ChatGetResponse.CHAT_MANAGER_MODIFITION_DATETIME.getName(), chatDetail.getChatManagerModificationDatetime());
		chatInfo.put(ChatGetResponse.CHAT_TYPE_CODE.getName(), chatDetail.getChatTypeCode());
		chatInfo.put(ChatGetResponse.CHAT_INFO_MODIFICATION_DATETIME.getName(), chatDetail.getChatInfoModificationDatetime());
		chatInfo.put(ChatGetResponse.TITLE.getName(), chatDetail.getTitle());
		chatInfo.put(ChatGetResponse.INTRO.getName(), chatDetail.getIntro());
		chatInfo.put(ChatGetResponse.PASSWORD.getName(), chatDetail.getPassword());
		chatInfo.put(ChatGetResponse.ANNOUNCEMENT_YN.getName(), chatDetail.isAnnouncementYn());
		chatInfo.put(ChatGetResponse.PHOTO_EXISTENCE_YN.getName(), chatDetail.isPhotoExistenceYn());
		chatInfo.put(ChatGetResponse.INVITATION_LINK.getName(), chatDetail.getInvitationLink());

		return chatInfo;
	}

}
