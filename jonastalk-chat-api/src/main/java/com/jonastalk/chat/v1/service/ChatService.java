package com.jonastalk.chat.v1.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.jonastalk.chat.v1.api.field.ChatCreateRequest;
import com.jonastalk.chat.v1.api.field.ChatCreateResponse;
import com.jonastalk.chat.v1.entity.ChatChatDetailEntity;
import com.jonastalk.chat.v1.entity.ChatParticipantListEntity;
import com.jonastalk.chat.v1.repository.ChatChatDetailRepository;
import com.jonastalk.chat.v1.repository.ChatParticipantListRepository;
import com.jonastalk.common.api.field.CommonParams;
import com.jonastalk.common.consts.EnumErrorCode;
import com.jonastalk.common.exception.CustomException;

import io.azam.ulidj.ULID;

/**
 * @name ChatService.java
 * @brief Chat Service
 * @author Jonas Lim
 * @date Jun 27, 2025
 */
@Service
public class ChatService {
	
	@Autowired
	ChatParticipantListRepository chatParticipantListRepository;
	
	@Autowired
	ChatChatDetailRepository chatChatDetailRepository;

	/**
	 * @name createChat(@Map<String, Object> param)
	 * @brief Create chat
	 * @author Jonas Lim
	 * @date June 27, 2025
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> createChat(Map<String, Object> param) throws Exception {
		
	   	// ----------------------
    	// 1. Get Params
    	// ----------------------
    	final List<String> userIds = (List) param.get(ChatCreateRequest.TO_USER_IDS.getName());
    	final String username = (String) param.get(CommonParams.USERNAME.getValue());
//    	final List<String> userRoles = (List) param.get(CommonParams.USER_ROLES.getValue());
    	
    	
    	// ----------------------
    	// 2. Biz Logic
    	// ----------------------
    	final String chatId = createChatAndReturnChatId(username, userIds);
    	
    	// ----------------------
    	// 3. Response
    	// ----------------------
    	Map<String, Object> responseData = new HashMap<>();
    	responseData.put(ChatCreateResponse.CHAT_ID.getName(), chatId);
		return responseData;
	}
	

    @Transactional
	public String createChatAndReturnChatId(String fromUserId, List<String> toUserIds) throws Exception {

		// --------------
		// 1. Validate Chat
    	String chatId = findChatIdsWithExactParticipants(toUserIds);
    	if (StringUtils.hasText(chatId)) {
    		// Already exists
            throw new CustomException(EnumErrorCode.ERR_CHAT001);
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
	
	
	public String findChatIdsWithExactParticipants(List<String> userIds) throws Exception {
    	return chatParticipantListRepository.findChatIdsWithExactParticipants(userIds, userIds.size());
	}

}
