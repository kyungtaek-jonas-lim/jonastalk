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
 * @name CommonLanguageCodeEntity.java
 * @brief `t_common_language_c` Table Entity
 * @author Jonas Lim
 * @date 2023.12.11
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
//@DynamicInsert // When Insert, Insert without fields with null
@Entity
@Table(name = "t_common_language_c")
@ApiModel(description = "Common Language Code")
public class CommonLanguageCodeEntity {

    @Id
    @Column(name = "LANGUAGE_CODE", length = 2, nullable = false)
    @ApiModelProperty(notes = "Language Code")
    private String languageCode;
    
    @Column(name = "LANGUAGE_NAME", length = 100, nullable = false)
    @ApiModelProperty(notes = "Language Name")
    private String languageName;

    // Getters and Setters, constructors, additional methods, etc.
}