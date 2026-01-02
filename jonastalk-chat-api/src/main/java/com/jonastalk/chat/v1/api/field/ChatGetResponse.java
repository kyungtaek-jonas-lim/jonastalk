package com.jonastalk.chat.v1.api.field;

import com.jonastalk.common.api.field.IResponseParam;
import com.jonastalk.common.consts.EnumApiParamType;

import lombok.Getter;

/**
 * @name ChatGetResponse.java
 * @brief Chat Get Response Data
 * @author Jonas Lim
 * @date July 7, 2025
 */
@Getter
public enum ChatGetResponse implements IResponseParam {

	CHAT_ID
		("chatId",
			"Chat Id",
			EnumApiParamType.STRING
		)
	,CREATOR_USER_ID
		("creatorUserId",
			"Creator User Id",
			EnumApiParamType.STRING
		)
	,CREATION_DATE_TIME
		("creationDatetime",
			"Creation Datetime",
			EnumApiParamType.STRING
		)
	,CHAT_MANAGER_USER_ID
		("chatManagerUserId",
			"Chat Manager User Id",
			EnumApiParamType.STRING
		)
	,CHAT_MANAGER_MODIFITION_DATETIME
		("chatManagerModificationDatetime",
			"Chat Manager Modification Datetime",
			EnumApiParamType.STRING
		)
	,CHAT_TYPE_CODE
		("chatTypeCode",
			"Chat Type Code",
			EnumApiParamType.STRING
		)
	,CHAT_INFO_MODIFICATION_DATETIME
		("chatInfoModificationDatetime",
			"Chat Info Modification Datetime",
			EnumApiParamType.STRING
		)
	,TITLE
		("title",
			"Title",
			EnumApiParamType.STRING
		)
	,INTRO
		("intro",
			"Intro",
			EnumApiParamType.STRING
		)
	,PASSWORD
		("password",
			"Password",
			EnumApiParamType.STRING
		)
	,ANNOUNCEMENT_YN
		("announcementYn",
			"Announcement Yn",
			EnumApiParamType.STRING
		)
	,PHOTO_EXISTENCE_YN
		("photoExistenceYn",
			"Photo Existence Yn",
			EnumApiParamType.STRING
		)
	,INVITATION_LINK
		("invitationLink",
			"Invitation Link",
			EnumApiParamType.STRING
		)
	;

	private String name;
	private String explanation;
	private EnumApiParamType type;
	private ChatGetResponse (String name, String explanation, EnumApiParamType type) {
		this.name				= name;
		this.explanation 		= explanation;
		this.type 				= type;
	}
}