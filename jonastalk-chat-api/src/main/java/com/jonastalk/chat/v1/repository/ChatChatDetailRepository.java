package com.jonastalk.chat.v1.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jonastalk.chat.v1.entity.ChatChatDetailEntity;

/**
 * @name ChatChatDetailRepository.java
 * @brief `t_chat_chat_d` Table Repository
 * @author Jonas Lim
 * @date June 13, 2025
 */
@Repository
public interface ChatChatDetailRepository extends JpaRepository<ChatChatDetailEntity, String> {
    // This interface inherits basic CRUD methods from JpaRepository
    // You can add custom query methods here if needed
}
