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
 * @name CommonRoleCodeEntity.java
 * @brief `t_common_role_c` Table Entity
 * @author Jonas Lim
 * @date 2023.12.11
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
//@DynamicInsert // When Insert, Insert without fields with null
@Entity
@Table(name = "t_common_role_c")
@ApiModel(description = "Common Role Code")
public class CommonRoleCodeEntity {

    @Id
    @Column(name = "ROLE_CODE", length = 20, nullable = false)
    @ApiModelProperty(notes = "Role Code")
    private String roleCode;
    
    @Column(name = "ROLE_NAME", length = 100, nullable = false)
    @ApiModelProperty(notes = "Role Name")
    private String roleName;

    // Getters and Setters, constructors, additional methods, etc.
}