package com.jonastalk.chat.v1.repository;

import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.jonastalk.chat.v1.entity.ChatParticipantListEntity;

/**
 * @name ChatParticipantListRepository.java
 * @brief `t_chat_chat_participant_l` Table Repository
 * @author Jonas Lim
 * @date June 27, 2025
 */
@Repository
public interface ChatParticipantListRepository extends JpaRepository<ChatParticipantListEntity, String> {
    // This interface inherits basic CRUD methods from JpaRepository
    // You can add custom query methods here if needed
	
//	@Query(value = """
//		    SELECT CHAT_ID
//		    FROM t_chat_chat_participant_l
//		    WHERE USER_ID IN (:userIds)
//		    GROUP BY CHAT_ID
//		    HAVING COUNT(DISTINCT USER_ID) = :size
//		    """, nativeQuery = true)
	@Query(value = "\r\n"
			+ "    SELECT CHAT_ID\r\n"
			+ "    FROM t_chat_chat_participant_l\r\n"
			+ "    GROUP BY CHAT_ID\r\n"
			+ "    HAVING \r\n"
			+ "        COUNT(*) = :size\r\n"
			+ "        AND COUNT(CASE WHEN USER_ID IN (:userIds) THEN 1 END) = :size", nativeQuery = true)
	String findChatIdsWithExactParticipants(@Param("userIds") Set<String> userIds, @Param("size") int size);

}
