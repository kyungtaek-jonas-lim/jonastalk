package com.jonastalk.auth.v1.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jonastalk.auth.v1.entity.AuthUserAccountListEntity;

/**
 * @name AccountListRepository.java
 * @brief `ACCOUNT_LIST` Table Repository
 * @author Jonas Lim
 * @date 2023.11.27
 */
@Repository
public interface AccountListRepository extends JpaRepository<AuthUserAccountListEntity, String> {
    // This interface inherits basic CRUD methods from JpaRepository
    // You can add custom query methods here if needed
	AuthUserAccountListEntity findByUsername(String username);
}
