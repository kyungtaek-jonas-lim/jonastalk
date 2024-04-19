
# ==========================================
# CREATE & USE Database
# ==========================================
CREATE DATABASE IF NOT EXISTS jonastalk CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE jonastalk;



# ==========================================
# User
# ==========================================
CREATE USER IF NOT EXISTS 'jonastalk'@'localhost' identified by '1234';
grant all privileges on jonastalk.* to 'jonastalk'@'localhost';



# ==========================================
# DDL
# ==========================================


-- ----------------------------
-- t_auth_user_account_l
-- drop table t_auth_user_account_l;
CREATE TABLE IF NOT EXISTS `t_auth_user_account_l` (
  `USER_ID` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT 'User Identifier',
  `USERNAME` varchar(50) NOT NULL COMMENT 'Username',
  `PASSWORD` varchar(500) NOT NULL COMMENT 'Ecrypted Password',
  `FIRST_NAME` varchar(50) NOT NULL DEFAULT '' COMMENT 'First Name',
  `LAST_NAME` varchar(50) NOT NULL DEFAULT '' COMMENT 'Last Name',
  `NICKNAME` varchar(50) NOT NULL COMMENT 'Nickname',
  `LANGUAGE_CODE` varchar(2) NOT NULL COMMENT 'Language Code(`t_common_language_c`)',
  `COUNTRY_CODE` varchar(2) NOT NULL COMMENT 'Country Code(`t_common_country_c`)',
  `COUNTRY_CODE_FOR_PHONE_NUMBER` varchar(2) NOT NULL DEFAULT '' COMMENT 'Country Code For Phone Number(`t_common_country_c`)',
  `PHONE_NUMBER` varchar(20) NOT NULL DEFAULT '' COMMENT 'Phone Number',
  `EMAIL` varchar(500) NOT NULL COMMENT 'Email Address',
  `IS_VALID_EMAIL` tinyint(1) DEFAULT 0 COMMENT 'Email Verification Yn',
  `SELF_INTRODUCTION` varchar(500) NOT NULL COMMENT 'Self Introduction',
  `ROLE_CODE` varchar(20) NOT NULL DEFAULT 'USER' COMMENT 'Role Code For Security',
  `IS_ENABLED` tinyint(1) DEFAULT 0 COMMENT 'User Account Enablement Status',
  `DISABLEMENT_CODE` varchar(3) NOT NULL DEFAULT '' COMMENT 'User Account Disablement Reason Code',
  `IS_MFA_ENABLED` tinyint(1) DEFAULT 0 COMMENT 'MFC Use Status',
  `THEME_CODE` varchar(3) NOT NULL DEFAULT 'L' COMMENT 'Background Theme Code',
  `SECURITY_QUESTION_ID` int unsigned NOT NULL COMMENT 'Security Question Identifier',
  `SECURITY_ANSWER` varchar(50) NOT NULL COMMENT 'Security Answer',
  `CREATION_DATETIME` datetime(6) NOT NULL DEFAULT current_timestamp(6) COMMENT 'User Account Creation Datetime',
  `MOTIFICATION_DATETIME` datetime(6) NOT NULL DEFAULT current_timestamp(6) COMMENT 'User Account Infomation Modification Datetime',
  `LAST_LOGIN_DATETIME` datetime(6) NOT NULL DEFAULT current_timestamp(6) COMMENT 'Last Login Datetime',
  `LAST_PASSWORD_CHANGE_DATETIME` datetime(6) NOT NULL DEFAULT current_timestamp(6) COMMENT 'Last Password Change Datetime',
  PRIMARY KEY (`USER_ID`),
  UNIQUE KEY `idx_auth_user_accounts_u01` (`USERNAME`),
  UNIQUE KEY `idx_auth_user_accounts_u02` (`NICKNAME`),
  UNIQUE KEY `idx_auth_user_accounts_u03` (`COUNTRY_CODE_FOR_PHONE_NUMBER`, `PHONE_NUMBER`),
  UNIQUE KEY `idx_auth_user_accounts_u04` (`EMAIL`),
  KEY `idx_auth_user_accounts_n01` (`COUNTRY_CODE`),
  KEY `idx_auth_user_accounts_n02` (`LAST_LOGIN_DATETIME`),
  KEY `idx_auth_user_accounts_n03` (`LAST_PASSWORD_CHANGE_DATETIME`),
  KEY `idx_auth_user_accounts_n04` (`FIRST_NAME`),
  KEY `idx_auth_user_accounts_n05` (`LAST_NAME`, `FIRST_NAME`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='Auth User Account List';



-- ----------------------------
-- t_common_role_c
-- drop table t_common_role_c ;
CREATE TABLE IF NOT EXISTS `t_common_role_c` (
  `ROLE_CODE` varchar(20) NOT NULL COMMENT 'Role Code',
  `ROLE_NAME` varchar(100) NOT NULL COMMENT 'Role Name',
--   `URL` varchar(100) NOT NULL COMMENT 'Available Request Url',
  PRIMARY KEY (`ROLE_CODE`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='Common Role Code';



-- ----------------------------
-- t_common_country_c
-- drop table t_common_country_c ;
CREATE TABLE IF NOT EXISTS `t_common_country_c` (
  `COUNTRY_CODE` varchar(2) NOT NULL COMMENT 'Country Code',
  `COUNTRY_NAME` varchar(100) NOT NULL COMMENT 'Country Name',
--   `COUNTRY_PHONE_NUMBER` varchar(20) NOT NULL COMMENT 'Country Phone Number',
  PRIMARY KEY (`COUNTRY_CODE`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='Common Country Code';



-- ----------------------------
-- t_common_language_c
-- drop table t_common_language_c ;
CREATE TABLE IF NOT EXISTS `t_common_language_c` (
  `LANGUAGE_CODE` varchar(2) NOT NULL COMMENT 'Language Code',
  `LANGUAGE_NAME` varchar(100) NOT NULL COMMENT 'Language Name',
  PRIMARY KEY (`LANGUAGE_CODE`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='Common Language Code';



-- ----------------------------
-- t_common_user_account_disablement_c
-- drop table t_common_user_account_disablement_c;
CREATE TABLE IF NOT EXISTS `t_common_user_account_disablement_c` (
  `DISABLEMENT_CODE` varchar(3) NOT NULL COMMENT 'Disablement Code',
  `DISABLEMENT_NAME` varchar(20) NOT NULL COMMENT 'Disablement Name',
  `DISABLEMENT_MESSAGE_EN` varchar(200) NOT NULL COMMENT 'Disablement Message(English)',
  PRIMARY KEY (`DISABLEMENT_CODE`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='Common User Account Disablement Code';



-- ----------------------------
-- t_common_background_theme_c
-- drop table t_common_background_theme_c;
CREATE TABLE IF NOT EXISTS `t_common_background_theme_c` (
  `THEME_CODE` varchar(3) NOT NULL COMMENT 'Background Theme Code',
  `THEME_NAME` varchar(20) NOT NULL COMMENT 'Background Theme Name',
  PRIMARY KEY (`THEME_CODE`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='Common Background Theme Code';



-- ----------------------------
-- t_common_security_question_l
-- drop table t_common_security_question_l;
CREATE TABLE IF NOT EXISTS `t_common_security_question_l` (
  `SECURITY_QUESTION_ID` int unsigned NOT NULL AUTO_INCREMENT NOT NULL COMMENT 'Security Question Identifier',
  `SECURITY_QUESTION_EN` varchar(100) NOT NULL COMMENT 'Security Question(English)',
  PRIMARY KEY (`SECURITY_QUESTION_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='Common Security Question List';




# ==========================================
# DML
# ==========================================


-- ----------------------------
-- t_common_auth_user_account_l
INSERT INTO t_auth_user_account_l (
	USERNAME
	,PASSWORD
	,FIRST_NAME
	,LAST_NAME
	,NICKNAME
	,LANGUAGE_CODE
	,COUNTRY_CODE
	,COUNTRY_CODE_FOR_PHONE_NUMBER
	,PHONE_NUMBER
	,EMAIL
	,IS_VALID_EMAIL
	,SELF_INTRODUCTION
	,ROLE_CODE
	,IS_ENABLED
	,DISABLEMENT_CODE
	,IS_MFA_ENABLED
	,THEME_CODE
	,SECURITY_QUESTION_ID
	,SECURITY_ANSWER
-- 	,CREATION_DATETIME
-- 	,MOTIFICATION_DATETIME
-- 	,LAST_LOGIN_DATETIME
-- 	,LAST_PASSWORD_CHANGE_DATETIME
	) VALUES
	(
	'admin'
	,'$2a$10$IXZphmi5wggDUblc1LfWhOaGKfEfgMqp.oGOZBz2ni7BYTIFvgsHK'
	,'Admin'
	,''
	,'Admin'
	,'ko'
	,'kr'
	,'kr'
	,'+82-10-0000-0000'
	,'jonasweet9900@gmail.com'	
	,1	
	,'This is official admin for JonasTalk'
	,'ADMIN'
	,1
	,''
	,0
	,'L'
	,1
	,'suwon'
	),
	(
	'root'
	,'$2a$10$IXZphmi5wggDUblc1LfWhOaGKfEfgMqp.oGOZBz2ni7BYTIFvgsHK'
	,'Root'
	,''
	,'Root'
	,'ko'
	,'kr'
	,'kr'
	,'+82-10-0000-0001'
	,'abc@def.com'	
	,1	
	,'This is official admin for JonasTalk'
	,'ADMIN'
	,1
	,''
	,0
	,'L'
	,1
	,'suwon'
	)
;




-- ----------------------------
-- t_common_role_c
INSERT INTO t_common_role_c (ROLE_CODE, ROLE_NAME) VALUES
('ADMIN', 'Admin'),
('USER', 'User')
;




-- ----------------------------
-- t_common_country_c
INSERT INTO t_common_country_c (COUNTRY_CODE, COUNTRY_NAME) VALUES
('AF', 'Afghanistan[b]'),
('AX', 'Åland Islands'),
('AL', 'Albania'),
('DZ', 'Algeria'),
('AS', 'American Samoa'),
('AD', 'Andorra'),
('AO', 'Angola'),
('AI', 'Anguilla'),
('AQ', 'Antarctica'),
('AG', 'Antigua and Barbuda'),
('AR', 'Argentina'),
('AM', 'Armenia'),
('AW', 'Aruba'),
('AU', 'Australia'),
('AT', 'Austria'),
('AZ', 'Azerbaijan'),
('BS', 'Bahamas'),
('BH', 'Bahrain'),
('BD', 'Bangladesh'),
('BB', 'Barbados'),
('BY', 'Belarus'),
('BE', 'Belgium'),
('BZ', 'Belize'),
('BJ', 'Benin'),
('BM', 'Bermuda'),
('BT', 'Bhutan'),
('BO', 'Bolivia (Plurinational State of)'),
('BQ', 'Bonaire, Sint Eustatius and Saba[c]'),
('BA', 'Bosnia and Herzegovina'),
('BW', 'Botswana'),
('BV', 'Bouvet Island'),
('BR', 'Brazil'),
('IO', 'British Indian Ocean Territory'),
('BN', 'Brunei Darussalam'),
('BG', 'Bulgaria'),
('BF', 'Burkina Faso'),
('BI', 'Burundi'),
('CV', 'Cabo Verde'),
('KH', 'Cambodia'),
('CM', 'Cameroon'),
('CA', 'Canada'),
('KY', 'Cayman Islands'),
('CF', 'Central African Republic'),
('TD', 'Chad'),
('CL', 'Chile'),
('CN', 'China[b]'),
('CX', 'Christmas Island'),
('CC', 'Cocos (Keeling) Islands'),
('CO', 'Colombia'),
('KM', 'Comoros'),
('CG', 'Congo'),
('CD', 'Congo, Democratic Republic of the'),
('CK', 'Cook Islands'),
('CR', 'Costa Rica'),
('CI', 'Côte d''Ivoire'),
('HR', 'Croatia'),
('CU', 'Cuba'),
('CW', 'Curaçao'),
('CY', 'Cyprus[b]'),
('CZ', 'Czechia'),
('DK', 'Denmark'),
('DJ', 'Djibouti'),
('DM', 'Dominica'),
('DO', 'Dominican Republic'),
('EC', 'Ecuador'),
('EG', 'Egypt'),
('SV', 'El Salvador'),
('GQ', 'Equatorial Guinea'),
('ER', 'Eritrea'),
('EE', 'Estonia'),
('SZ', 'Eswatini'),
('ET', 'Ethiopia'),
('FK', 'Falkland Islands (Malvinas)[b]'),
('FO', 'Faroe Islands'),
('FJ', 'Fiji'),
('FI', 'Finland'),
('FR', 'France'),
('GF', 'French Guiana'),
('PF', 'French Polynesia'),
('TF', 'French Southern Territories'),
('GA', 'Gabon'),
('GM', 'Gambia'),
('GE', 'Georgia'),
('DE', 'Germany'),
('GH', 'Ghana'),
('GI', 'Gibraltar'),
('GR', 'Greece'),
('GL', 'Greenland'),
('GD', 'Grenada'),
('GP', 'Guadeloupe'),
('GU', 'Guam'),
('GT', 'Guatemala'),
('GG', 'Guernsey'),
('GN', 'Guinea'),
('GW', 'Guinea-Bissau'),
('GY', 'Guyana'),
('HT', 'Haiti'),
('HM', 'Heard Island and McDonald Islands'),
('VA', 'Holy See'),
('HN', 'Honduras'),
('HK', 'Hong Kong'),
('HU', 'Hungary'),
('IS', 'Iceland'),
('IN', 'India'),
('ID', 'Indonesia'),
('IR', 'Iran (Islamic Republic of)'),
('IQ', 'Iraq'),
('IE', 'Ireland'),
('IM', 'Isle of Man'),
('IL', 'Israel'),
('IT', 'Italy'),
('JM', 'Jamaica'),
('JP', 'Japan'),
('JE', 'Jersey'),
('JO', 'Jordan'),
('KZ', 'Kazakhstan'),
('KE', 'Kenya'),
('KI', 'Kiribati'),
('KP', 'Korea (Democratic People''s Republic of)'),
('KR', 'Korea, Republic of'),
('KW', 'Kuwait'),
('KG', 'Kyrgyzstan'),
('LA', 'Lao People''s Democratic Republic'),
('LV', 'Latvia'),
('LB', 'Lebanon'),
('LS', 'Lesotho'),
('LR', 'Liberia'),
('LY', 'Libya'),
('LI', 'Liechtenstein'),
('LT', 'Lithuania'),
('LU', 'Luxembourg'),
('MO', 'Macao'),
('MG', 'Madagascar'),
('MW', 'Malawi'),
('MY', 'Malaysia'),
('MV', 'Maldives'),
('ML', 'Mali'),
('MT', 'Malta'),
('MH', 'Marshall Islands'),
('MQ', 'Martinique'),
('MR', 'Mauritania'),
('MU', 'Mauritius'),
('YT', 'Mayotte'),
('MX', 'Mexico'),
('FM', 'Micronesia (Federated States of)'),
('MD', 'Moldova, Republic of'),
('MC', 'Monaco'),
('MN', 'Mongolia'),
('ME', 'Montenegro'),
('MS', 'Montserrat'),
('MA', 'Morocco'),
('MZ', 'Mozambique'),
('MM', 'Myanmar'),
('NA', 'Namibia'),
('NR', 'Nauru'),
('NP', 'Nepal'),
('NL', 'Netherlands, Kingdom of the'),
('NC', 'New Caledonia'),
('NZ', 'New Zealand'),
('NI', 'Nicaragua'),
('NE', 'Niger'),
('NG', 'Nigeria'),
('NU', 'Niue'),
('NF', 'Norfolk Island'),
('MK', 'North Macedonia'),
('MP', 'Northern Mariana Islands'),
('NO', 'Norway'),
('OM', 'Oman'),
('PK', 'Pakistan'),
('PW', 'Palau'),
('PS', 'Palestine, State of[b]'),
('PA', 'Panama'),
('PG', 'Papua New Guinea'),
('PY', 'Paraguay'),
('PE', 'Peru'),
('PH', 'Philippines'),
('PN', 'Pitcairn'),
('PL', 'Poland'),
('PT', 'Portugal'),
('PR', 'Puerto Rico'),
('QA', 'Qatar'),
('RE', 'Réunion'),
('RO', 'Romania'),
('RU', 'Russian Federation'),
('RW', 'Rwanda'),
('BL', 'Saint Barthélemy'),
('SH', 'Saint Helena, Ascension and Tristan da Cunha[d]'),
('KN', 'Saint Kitts and Nevis'),
('LC', 'Saint Lucia'),
('MF', 'Saint Martin (French part)'),
('PM', 'Saint Pierre and Miquelon'),
('VC', 'Saint Vincent and the Grenadines'),
('WS', 'Samoa'),
('SM', 'San Marino'),
('ST', 'Sao Tome and Principe'),
('SA', 'Saudi Arabia'),
('SN', 'Senegal'),
('RS', 'Serbia'),
('SC', 'Seychelles'),
('SL', 'Sierra Leone'),
('SG', 'Singapore'),
('SX', 'Sint Maarten (Dutch part)'),
('SK', 'Slovakia'),
('SI', 'Slovenia'),
('SB', 'Solomon Islands'),
('SO', 'Somalia'),
('ZA', 'South Africa'),
('GS', 'South Georgia and the South Sandwich Islands'),
('SS', 'South Sudan'),
('ES', 'Spain'),
('LK', 'Sri Lanka'),
('SD', 'Sudan'),
('SR', 'Suriname'),
('SJ', 'Svalbard and Jan Mayen[e]'),
('SE', 'Sweden'),
('CH', 'Switzerland'),
('SY', 'Syrian Arab Republic'),
('TW', 'Taiwan, Province of China[b]'),
('TJ', 'Tajikistan'),
('TZ', 'Tanzania, United Republic of'),
('TH', 'Thailand'),
('TL', 'Timor-Leste'),
('TG', 'Togo'),
('TK', 'Tokelau'),
('TO', 'Tonga'),
('TT', 'Trinidad and Tobago'),
('TN', 'Tunisia'),
('TR', 'Türkiye'),
('TM', 'Turkmenistan'),
('TC', 'Turks and Caicos Islands'),
('TV', 'Tuvalu'),
('UG', 'Uganda'),
('UA', 'Ukraine'),
('AE', 'United Arab Emirates'),
('GB', 'United Kingdom of Great Britain and Northern Ireland'),
('US', 'United States of America'),
('UM', 'United States Minor Outlying Islands[f]'),
('UY', 'Uruguay'),
('UZ', 'Uzbekistan'),
('VU', 'Vanuatu'),
('VE', 'Venezuela (Bolivarian Republic of)'),
('VN', 'Viet Nam'),
('VG', 'Virgin Islands (British)'),
('VI', 'Virgin Islands (U.S.)'),
('WF', 'Wallis and Futuna'),
('EH', 'Western Sahara[b]'),
('YE', 'Yemen'),
('ZM', 'Zambia'),
('ZW', 'Zimbabwe')
;



-- ----------------------------
-- t_common_language_c
INSERT INTO t_common_language_c (LANGUAGE_CODE, LANGUAGE_NAME) VALUES
('ab', 'Abkhazian'),
('aa', 'Afar'),
('af', 'Afrikaans'),
('ak', 'Akan'),
('sq', 'Albanian'),
('am', 'Amharic'),
('ar', 'Arabic'),
('an', 'Aragonese'),
('hy', 'Armenian'),
('as', 'Assamese'),
('av', 'Avaric'),
('ae', 'Avestan'),
('ay', 'Aymara'),
('az', 'Azerbaijani'),
('bm', 'Bambara'),
('ba', 'Bashkir'),
('eu', 'Basque'),
('be', 'Belarusian'),
('bn', 'Bengali'),
('bi', 'Bislama'),
('bs', 'Bosnian'),
('br', 'Breton'),
('bg', 'Bulgarian'),
('my', 'Burmese'),
('ca', 'Catalan, Valencian'),
('ch', 'Chamorro'),
('ce', 'Chechen'),
('ny', 'Chichewa, Chewa, Nyanja'),
('zh', 'Chinese'),
('cu', 'Church Slavonic, Old Slavonic, Old Church Slavonic'),
('cv', 'Chuvash'),
('kw', 'Cornish'),
('co', 'Corsican'),
('cr', 'Cree'),
('hr', 'Croatian'),
('cs', 'Czech'),
('da', 'Danish'),
('dv', 'Divehi, Dhivehi, Maldivian'),
('nl', 'Dutch, Flemish'),
('dz', 'Dzongkha'),
('en', 'English'),
('eo', 'Esperanto'),
('et', 'Estonian'),
('ee', 'Ewe'),
('fo', 'Faroese'),
('fj', 'Fijian'),
('fi', 'Finnish'),
('fr', 'French'),
('fy', 'Western Frisian'),
('ff', 'Fulah'),
('gd', 'Gaelic, Scottish Gaelic'),
('gl', 'Galician'),
('lg', 'Ganda'),
('ka', 'Georgian'),
('de', 'German'),
('el', 'Greek, Modern (1453–)'),
('kl', 'Kalaallisut, Greenlandic'),
('gn', 'Guarani'),
('gu', 'Gujarati'),
('ht', 'Haitian, Haitian Creole'),
('ha', 'Hausa'),
('he', 'Hebrew'),
('hz', 'Herero'),
('hi', 'Hindi'),
('ho', 'Hiri Motu'),
('hu', 'Hungarian'),
('is', 'Icelandic'),
('io', 'Ido'),
('ig', 'Igbo'),
('id', 'Indonesian'),
('ia', 'Interlingua (International Auxiliary Language Association)'),
('ie', 'Interlingue, Occidental'),
('iu', 'Inuktitut'),
('ik', 'Inupiaq'),
('ga', 'Irish'),
('it', 'Italian'),
('ja', 'Japanese'),
('jv', 'Javanese'),
('kn', 'Kannada'),
('kr', 'Kanuri'),
('ks', 'Kashmiri'),
('kk', 'Kazakh'),
('km', 'Central Khmer'),
('ki', 'Kikuyu, Gikuyu'),
('rw', 'Kinyarwanda'),
('ky', 'Kirghiz, Kyrgyz'),
('kv', 'Komi'),
('kg', 'Kongo'),
('ko', 'Korean'),
('kj', 'Kuanyama, Kwanyama'),
('ku', 'Kurdish'),
('lo', 'Lao'),
('la', 'Latin'),
('lv', 'Latvian'),
('li', 'Limburgan, Limburger, Limburgish'),
('ln', 'Lingala'),
('lt', 'Lithuanian'),
('lu', 'Luba-Katanga'),
('lb', 'Luxembourgish, Letzeburgesch'),
('mk', 'Macedonian'),
('mg', 'Malagasy'),
('ms', 'Malay'),
('ml', 'Malayalam'),
('mt', 'Maltese'),
('gv', 'Manx'),
('mi', 'Maori'),
('mr', 'Marathi'),
('mh', 'Marshallese'),
('mn', 'Mongolian'),
('na', 'Nauru'),
('nv', 'Navajo, Navaho'),
('nd', 'North Ndebele'),
('nr', 'South Ndebele'),
('ng', 'Ndonga'),
('ne', 'Nepali'),
('no', 'Norwegian'),
('nb', 'Norwegian Bokmål'),
('nn', 'Norwegian Nynorsk'),
('ii', 'Sichuan Yi, Nuosu'),
('oc', 'Occitan'),
('oj', 'Ojibwa'),
('or', 'Oriya'),
('om', 'Oromo'),
('os', 'Ossetian, Ossetic'),
('pi', 'Pali'),
('ps', 'Pashto, Pushto'),
('fa', 'Persian'),
('pl', 'Polish'),
('pt', 'Portuguese'),
('pa', 'Punjabi, Panjabi'),
('qu', 'Quechua'),
('ro', 'Romanian, Moldavian, Moldovan'),
('rm', 'Romansh'),
('rn', 'Rundi'),
('ru', 'Russian'),
('se', 'Northern Sami'),
('sm', 'Samoan'),
('sg', 'Sango'),
('sa', 'Sanskrit'),
('sc', 'Sardinian'),
('sr', 'Serbian'),
('sn', 'Shona'),
('sd', 'Sindhi'),
('si', 'Sinhala, Sinhalese'),
('sk', 'Slovak'),
('sl', 'Slovenian'),
('so', 'Somali'),
('st', 'Southern Sotho'),
('es', 'Spanish, Castilian'),
('su', 'Sundanese'),
('sw', 'Swahili'),
('ss', 'Swati'),
('sv', 'Swedish'),
('tl', 'Tagalog'),
('ty', 'Tahitian'),
('tg', 'Tajik'),
('ta', 'Tamil'),
('tt', 'Tatar'),
('te', 'Telugu'),
('th', 'Thai'),
('bo', 'Tibetan'),
('ti', 'Tigrinya'),
('to', 'Tonga (Tonga Islands)'),
('ts', 'Tsonga'),
('tn', 'Tswana'),
('tr', 'Turkish'),
('tk', 'Turkmen'),
('tw', 'Twi'),
('ug', 'Uighur, Uyghur'),
('uk', 'Ukrainian'),
('ur', 'Urdu'),
('uz', 'Uzbek'),
('ve', 'Venda'),
('vi', 'Vietnamese'),
('vo', 'Volapük'),
('wa', 'Walloon'),
('cy', 'Welsh'),
('wo', 'Wolof'),
('xh', 'Xhosa'),
('yi', 'Yiddish'),
('yo', 'Yoruba'),
('za', 'Zhuang, Chuang'),
('zu', 'Zulu')
;



-- ----------------------------
-- t_common_user_account_disablement_c
INSERT INTO t_common_user_account_disablement_c (DISABLEMENT_CODE, DISABLEMENT_NAME, DISABLEMENT_MESSAGE_EN) values
('BLK', 'Blocked User', 'The User Account Is Blocked For A Reason.'),
('DMT', 'Dormant User', 'The User Account Is Dormant.')
;



-- ----------------------------
-- t_common_background_theme_c
INSERT INTO t_common_background_theme_c (THEME_CODE, THEME_NAME) values
('L', 'Light'),
('D', 'Dark')
;



-- ----------------------------
-- t_common_security_question_l
INSERT INTO t_common_security_question_l (SECURITY_QUESTION_EN) values
('What city were you born in? (except for ''city'''),
('What is your favorite motto?'),
('Who is your role model?'),
('What is your favority company? (except for ''inc'' or ''group''')
;