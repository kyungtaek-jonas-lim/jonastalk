package com.jonastalk.common.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @name CommonUserAccountDisablementCodeEntity.java
 * @brief `t_common_user_account_disablement_c` Table Entity
 * @author Jonas Lim
 * @date 2023.12.11
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
//@DynamicInsert // When Insert, Insert without fields with null
@Entity
@Table(name = "t_common_user_account_disablement_c")
@ApiModel(description = "Common User Account Disablement Code")
public class CommonUserAccountDisablementCodeEntity {

    @Id
    @Column(name = "DISABLEMENT_CODE", length = 3, nullable = false)
    @ApiModelProperty(notes = "Disablement Code")
    private String disablementCode;
    
    @Column(name = "DISABLEMENT_NAME", length = 20, nullable = false)
    @ApiModelProperty(notes = "Disablement Name")
    private String disablementName;
    
    @Column(name = "DISABLEMENT_MESSAGE_EN", length = 200, nullable = false)
    @ApiModelProperty(notes = "Disablement Message(English)")
    private String disablementMessageEn;

    // Getters and Setters, constructors, additional methods, etc.
}