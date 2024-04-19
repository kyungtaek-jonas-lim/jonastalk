package com.jonastalk.auth.v1.entity;

import java.math.BigInteger;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.ColumnDefault;

import com.jonastalk.common.consts.RoleType;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @name AuthUserAccountListEntity.java
 * @brief `t_auth_user_account_l` Table Entity
 * @author Jonas Lim
 * @date 2023.12.11
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
//@DynamicInsert // When Insert, Insert without fields with null
@Entity
@Table(name = "t_auth_user_account_l")
@ApiModel(description = "Auth User Account List")
public class AuthUserAccountListEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "USER_ID", length = 20, nullable = false)
    @ApiModelProperty(notes = "User Identifier")
    private BigInteger userId;
    
    @Column(name = "USERNAME", length = 50, nullable = false)
    @ApiModelProperty(notes = "Username")
    private String username;

    @Column(name = "PASSWORD", length = 500, nullable = false)
    @ApiModelProperty(notes = "Ecrypted Password")
    private String password;

    @Column(name = "FIRST_NAME", length = 50, nullable = false)
    @ColumnDefault("''")
    @ApiModelProperty(notes = "First Name")
    private String firstName = "";

    @Column(name = "LAST_NAME", length = 50, nullable = false)
    @ColumnDefault("''")
    @ApiModelProperty(notes = "Last Name")
    private String lastName = "";
    
    @Column(name = "NICKNAME", length = 50, nullable = false)
    @ApiModelProperty(notes = "Nickname")
    private String nickname;
    
    @Column(name = "LANGUAGE_CODE", length = 2, nullable = false)
    @ApiModelProperty(notes = "Language Code(`t_common_language_c`)")
    private String languageCode;
    
    @Column(name = "COUNTRY_CODE", length = 2, nullable = false)
    @ApiModelProperty(notes = "Country Code(`t_common_country_c`)")
    private String countryCode;
    
    @Column(name = "COUNTRY_CODE_FOR_PHONE_NUMBER", length = 2, nullable = false)
    @ColumnDefault("''")
    @ApiModelProperty(notes = "Country Code For Phone Number(`t_common_country_c`)")
    private String countryCodeForPhoneNumber = "";

    @Column(name = "PHONE_NUMBER", length = 20, nullable = false)
    @ColumnDefault("''")
    @ApiModelProperty(notes = "Phone Number")
    private String phoneNumber = "";
    
    @Column(name = "EMAIL", length = 500, nullable = false)
    @ApiModelProperty(notes = "Email Address")
    private String email;

    @Column(name = "IS_VALID_EMAIL")
    @ColumnDefault("0")
    @ApiModelProperty(notes = "Email Verification Yn")
    private boolean isValidEmail = false;
    
    @Column(name = "SELF_INTRODUCTION", length = 500, nullable = false)
    @ApiModelProperty(notes = "Self Introduction")
    private String selfIntroduction;
    
    @Column(name = "ROLE_CODE", length = 20, nullable = false)
    @ColumnDefault("'USER'")
	@Enumerated(EnumType.STRING)
    @ApiModelProperty(notes = "Role Code For Security")
    private RoleType authority = RoleType.USER;

    @Column(name = "IS_ENABLED")
    @ColumnDefault("0")
    @ApiModelProperty(notes = "User Account Enablement Status")
    private boolean isEnabled = false;

    @Column(name = "DISABLEMENT_CODE", length = 3, nullable = false)
    @ColumnDefault("''")
    @ApiModelProperty(notes = "User Account Disablement Reason Code")
    private String disablementCode = "";

    @Column(name = "IS_MFA_ENABLED")
    @ColumnDefault("0")
    @ApiModelProperty(notes = "MFC Use Status")
    private boolean isMfaEnabled = false;

    @Column(name = "THEME_CODE", length = 3, nullable = false)
    @ColumnDefault("'L'")
    @ApiModelProperty(notes = "Background Theme Code")
    private String themeCode = "L";
    
    @Column(name = "SECURITY_QUESTION_ID", nullable = false)
    @ApiModelProperty(notes = "Security Question Identifier")
    private Long securityQuestionId;
    
    @Column(name = "SECURITY_ANSWER", length = 50, nullable = false)
    @ApiModelProperty(notes = "Security Answer")
    private String securityAnswer;
    
    @Column(name = "CREATION_DATETIME", length = 6, nullable = false)
    @ApiModelProperty(notes = "User Account Creation Datetime")
    private LocalDateTime creationDatetime = LocalDateTime.now();
    
    @Column(name = "MOTIFICATION_DATETIME", length = 6, nullable = false)
    @ApiModelProperty(notes = "User Account Infomation Modification Datetime")
    private LocalDateTime modificationDatetime = LocalDateTime.now();
    
    @Column(name = "LAST_LOGIN_DATETIME", length = 6, nullable = false)
    @ApiModelProperty(notes = "Last Login Datetime")
    private LocalDateTime lastLoginDatetime = LocalDateTime.now();
    
    @Column(name = "LAST_PASSWORD_CHANGE_DATETIME", length = 6, nullable = false)
    @ApiModelProperty(notes = "Last Password Change Datetime")
    private LocalDateTime lastPasswordChangeDatetime = LocalDateTime.now();

    // Getters and Setters, constructors, additional methods, etc.
}