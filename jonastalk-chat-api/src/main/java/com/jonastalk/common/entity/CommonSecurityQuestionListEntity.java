package com.jonastalk.common.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @name CommonSecurityQuestionListEntity.java
 * @brief `t_common_security_question_l` Table Entity
 * @author Jonas Lim
 * @date 2023.12.11
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
//@DynamicInsert // When Insert, Insert without fields with null
@Entity
@Table(name = "t_common_security_question_l")
@ApiModel(description = "Common Security Question List")
public class CommonSecurityQuestionListEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "SECURITY_QUESTION_ID", nullable = false)
    @ApiModelProperty(notes = "Security Question Identifier")
    private Long securityQuestionId;
    
    @Column(name = "SECURITY_QUESTION_EN", length = 100, nullable = false)
    @ApiModelProperty(notes = "Security Question(English)")
    private String securityQuestionEn;

    // Getters and Setters, constructors, additional methods, etc.
}