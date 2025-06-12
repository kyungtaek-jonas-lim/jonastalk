
# ==========================================
# CREATE & USE Database
# ==========================================
CREATE DATABASE IF NOT EXISTS jonastalk CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE jonastalk;

select * from mysql.user;

# ==========================================
# User
# ==========================================
CREATE USER IF NOT EXISTS 'jonastalk'@'localhost' identified by '1234';
grant all privileges on jonastalk.* to 'jonastalk'@'localhost';



# ==========================================
# DDL
# ==========================================


-- ----------------------------
-- t_chat_chat_d
-- drop table t_chat_chat_d;
CREATE TABLE t_chat_chat_d (
  CHAT_ID VARCHAR(42) NOT NULL COMMENT 'Chat Room Identifier',
  CREATER_USER_ID VARCHAR(42) NOT NULL COMMENT 'User ID who created the chat room',
  CREATER_DATETIME DATETIME(6) NOT NULL COMMENT 'Chat Room Creation Datetime',
  CHAT_MANAGER_USER_ID VARCHAR(42) NOT NULL COMMENT 'Current Chat Room Manager User ID',
  CHAT_MANAGER_MODIFICATION_DATETIME DATETIME(6) NOT NULL COMMENT 'Datetime when the chat manager was last changed',
  CHAT_TYPE_CODE VARCHAR(4) NOT NULL COMMENT 'Chat Room Type Code',
  CHAT_INFO_MODIFICATION_DATETIME DATETIME(6) NOT NULL COMMENT 'Datetime when the chat room info was last modified',
  TITLE VARCHAR(20) NOT NULL COMMENT 'Chat Room Title',
  INTRO VARCHAR(255) NOT NULL COMMENT 'Chat Room Introduction',
  PASSWORD VARCHAR(255) NOT NULL COMMENT 'Chat Room Password',
  ANNOUNCEMENT_YN TINYINT(1) NOT NULL COMMENT 'Whether this chat room is for announcements only',
  PHOTO_EXISTENCE_YN TINYINT(1) NOT NULL COMMENT 'Whether a photo exists for the chat room',
  INVITATION_LINK VARCHAR(500) NOT NULL COMMENT 'Invitation Link to Join the Chat Room',
  PRIMARY KEY (CHAT_ID),
  KEY idx_chat_chat_d_n01 (CHAT_MANAGER_USER_ID, CHAT_MANAGER_MODIFICATION_DATETIME),
  KEY idx_chat_chat_d_n02 (TITLE),
  KEY idx_chat_chat_d_n03 (INVITATION_LINK)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='Chat Room Detail';


-- ----------------------------
-- t_chat_chat_tag_l
-- drop table t_chat_chat_tag_l;
CREATE TABLE `t_chat_chat_tag_l` (
  `CHAT_ID` VARCHAR(42) NOT NULL COMMENT 'Chat Room Identifier',
  `TAG` VARCHAR(40) NOT NULL COMMENT 'Tag',
  `REGISTRATION_DATETIME` DATETIME(6) NOT NULL COMMENT 'Tag Registration Datetime',
  PRIMARY KEY (`CHAT_ID`, `TAG`),
  KEY `idx_chat_chat_tag_l_n01` (`TAG`, `CHAT_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='Chat Tag List';


-- ----------------------------
-- t_chat_chat_participant_l
-- drop table t_chat_chat_participant_l;
CREATE TABLE `t_chat_chat_participant_l` (
  `USER_ID` VARCHAR(42) NOT NULL COMMENT 'User Identifier',
  `CHAT_ID` VARCHAR(42) NOT NULL COMMENT 'Chat Room Identifier',
  `ENTRANCE_DATETIME` DATETIME(6) NOT NULL COMMENT 'Entrance Datetime',
  `LAST_PARTICIPATION_DATETIME` DATETIME(6) NOT NULL COMMENT 'Last Participation Datetime',
  `ANNOUNCEMENT_CHECK_YN` TINYINT(1) NOT NULL COMMENT 'Whether the user checked the latest announcement',
  PRIMARY KEY (`USER_ID`, `CHAT_ID`),
  KEY `idx_chat_chat_participant_l_n01` (`USER_ID`, `LAST_PARTICIPATION_DATETIME`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='Chat Participant List';


-- ----------------------------
-- t_chat_chat_photo_r
-- drop table t_chat_chat_photo_r;
CREATE TABLE `t_chat_chat_photo_r` (
  `CHAT_ID` VARCHAR(42) NOT NULL COMMENT 'Chat Room Identifier',
  `PHOTO` VARCHAR(500) NOT NULL COMMENT 'URL of the Chat Room Photo (S3 or other storage)',
  `REGISTRATION_DATETIME` DATETIME(6) NOT NULL COMMENT 'Photo Registration Datetime',
  PRIMARY KEY (`CHAT_ID`),
  KEY `idx_chat_chat_photo_r_n01` (`REGISTRATION_DATETIME`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='Chat Photo Relation';


-- ----------------------------
-- t_chat_chat_announcement_h
-- drop table t_chat_chat_announcement_h;
CREATE TABLE `t_chat_chat_announcement_h` (
  `CHAT_ID` VARCHAR(42) NOT NULL COMMENT 'Chat Room Identifier',
  `REGISTRATION_DATETIME` DATETIME(6) NOT NULL COMMENT 'Announcement Registration Datetime',
  `ANNOUNCEMENT_CONTENT` VARCHAR(200) NOT NULL COMMENT 'Announcement Content',
  PRIMARY KEY (`CHAT_ID`, `REGISTRATION_DATETIME`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='Chat Announcement History';


-- ----------------------------
-- t_chat_message_d
-- drop table t_chat_message_d;
CREATE TABLE `t_chat_message_d` (
  `CHAT_ID` VARCHAR(42) NOT NULL COMMENT 'Chat Room Identifier',
  `CHAT_MESSAGE_ID` VARCHAR(42) NOT NULL COMMENT 'Chat Message Identifier',
  `USER_ID` VARCHAR(42) NOT NULL COMMENT 'User Identifier',
  `REGISTRATION_DATETIME` DATETIME(6) NOT NULL COMMENT 'Message Registration Datetime',
  `CHAT_CONTENT` VARCHAR(500) NOT NULL COMMENT 'Chat Message Content',
  `CHAT_LONG_MESSAGE_YN` TINYINT(1) NOT NULL COMMENT 'Whether this is a long message',
  `CHAT_FILE_EXISTENCE_YN` TINYINT(1) NOT NULL COMMENT 'Whether a file exists in the message',
  `PHOTO_EXISTENCE_YN` TINYINT(1) NOT NULL COMMENT 'Whether a photo exists in the message',
  `PHOTO` VARCHAR(500) NOT NULL COMMENT 'URL of the photo in the message (S3 or other storage)',
  PRIMARY KEY (`CHAT_ID`, `CHAT_MESSAGE_ID`),
  KEY `idx_chat_message_d_n01` (`USER_ID`),
  KEY `idx_chat_message_d_n02` (`REGISTRATION_DATETIME`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='Chat Message Detail';


-- ----------------------------
-- t_chat_message_sympathy_h
-- drop table t_chat_message_sympathy_h;
CREATE TABLE `t_chat_message_sympathy_h` (
  `CHAT_ID` VARCHAR(42) NOT NULL COMMENT 'Chat Room Identifier',
  `CHAT_MESSAGE_ID` VARCHAR(42) NOT NULL COMMENT 'Chat Message Identifier',
  `USER_ID` VARCHAR(42) NOT NULL COMMENT 'User Identifier',
  `REGISTRATION_DATETIME` DATETIME(6) NOT NULL COMMENT 'Sympathy Registration Datetime',
  `SYMPARHY_CODE` VARCHAR(10) NOT NULL COMMENT 'Sympathy Code',
  PRIMARY KEY (`CHAT_ID`, `CHAT_MESSAGE_ID`, `USER_ID`),
  KEY `idx_chat_message_sympathy_h_n01` (`USER_ID`),
  KEY `idx_chat_message_sympathy_h_n02` (`REGISTRATION_DATETIME`),
  KEY `idx_chat_message_sympathy_h_n03` (`SYMPARHY_CODE`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='Chat Message Sympathy History';


-- ----------------------------
-- t_chat_long_message_l
-- drop table t_chat_long_message_l;
CREATE TABLE `t_chat_long_message_l` (
  `CHAT_ID` VARCHAR(42) NOT NULL COMMENT 'Chat Room Identifier',
  `CHAT_MESSAGE_ID` VARCHAR(42) NOT NULL COMMENT 'Chat Message Identifier',
  `CHAT_CONTENT` MEDIUMTEXT NOT NULL COMMENT 'Long Chat Message Content',
  PRIMARY KEY (`CHAT_ID`, `CHAT_MESSAGE_ID`),
  KEY `idx_chat_long_message_l_n01` (`CHAT_CONTENT`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='Chat Long Message List';


-- ----------------------------
-- t_chat_file_l
-- drop table t_chat_file_l;
CREATE TABLE `t_chat_file_l` (
  `CHAT_ID` VARCHAR(42) NOT NULL COMMENT 'Chat Room Identifier',
  `CHAT_MESSAGE_ID` VARCHAR(42) NOT NULL COMMENT 'Chat Message Identifier',
  `FILE_NAME` VARCHAR(100) NOT NULL COMMENT 'File Name',
  `FILE` VARCHAR(500) NOT NULL COMMENT 'File URL (S3 or other storage)',
  `FILE_SIZE` TINYINT(12) NOT NULL COMMENT 'File Size',
  PRIMARY KEY (`CHAT_ID`, `CHAT_MESSAGE_ID`),
  KEY `idx_chat_file_l_n01` (`FILE_NAME`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='Chat File List';


-- ----------------------------
-- t_chat_thread_r
-- drop table t_chat_thread_r;
CREATE TABLE `t_chat_thread_r` (
  `CHAT_ID` VARCHAR(42) NOT NULL COMMENT 'Chat Room Identifier',
  `CHAT_MESSAGE_ID` VARCHAR(42) NOT NULL COMMENT 'Chat Message Identifier',
  `CHAT_THREAD_ID` VARCHAR(42) NOT NULL COMMENT 'Chat Thread Identifier',
  PRIMARY KEY (`CHAT_ID`, `CHAT_MESSAGE_ID`),
  KEY `idx_chat_thread_r_n01` (`CHAT_THREAD_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='Chat Thread Relation';


-- ----------------------------
-- t_chat_thread_bookmark_l
-- drop table t_chat_thread_bookmark_l;
CREATE TABLE `t_chat_thread_bookmark_l` (
  `USER_ID` VARCHAR(42) NOT NULL COMMENT 'User Identifier',
  `CHAT_ID` VARCHAR(42) NOT NULL COMMENT 'Chat Room Identifier',
  `CHAT_THREAD_ID` VARCHAR(42) NOT NULL COMMENT 'Chat Thread Identifier',
  PRIMARY KEY (`USER_ID`, `CHAT_ID`, `CHAT_THREAD_ID`),
  KEY `idx_chat_thread_bookmark_l_n01` (`CHAT_ID`),
  KEY `idx_chat_thread_bookmark_l_n02` (`CHAT_THREAD_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='Chat Thread Bookmark List';
