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
 * @name CommonBackgroundThemeCodeEntity.java
 * @brief `t_common_background_theme_c` Table Entity
 * @author Jonas Lim
 * @date 2023.12.11
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
// @DynamicInsert // When Insert, Insert without fields with null
@Entity
@Table(name = "t_common_background_theme_c")
@ApiModel(description = "Common Background Theme Code")
public class CommonBackgroundThemeCodeEntity {

    @Id
    @Column(name = "THEME_CODE", length = 3, nullable = false)
    @ApiModelProperty(notes = "Background Theme Code")
    private String backgroundThemeCode;
    
    @Column(name = "THEME_NAME", length = 20, nullable = false)
    @ApiModelProperty(notes = "Background Theme Name")
    private String backgroundThemeName;

    // Getters and Setters, constructors, additional methods, etc.
}