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
 * @name CommonCountryCodeEntity.java
 * @brief `t_common_country_c` Table Entity
 * @author Jonas Lim
 * @date 2023.12.11
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
//@DynamicInsert // When Insert, Insert without fields with null
@Entity
@Table(name = "t_common_country_c")
@ApiModel(description = "Common Country Code")
public class CommonCountryCodeEntity {

    @Id
    @Column(name = "COUNTRY_CODE", length = 2, nullable = false)
    @ApiModelProperty(notes = "Country Code")
    private String countryCode;
    
    @Column(name = "COUNTRY_NAME", length = 100, nullable = false)
    @ApiModelProperty(notes = "Country Name")
    private String countryName;

    // Getters and Setters, constructors, additional methods, etc.
}