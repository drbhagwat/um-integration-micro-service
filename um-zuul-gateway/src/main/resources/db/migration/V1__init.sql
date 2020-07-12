CREATE
	TABLE
		IF NOT EXISTS USERS (ID SERIAL PRIMARY KEY,
		USERNAME VARCHAR(255) NOT NULL UNIQUE,
		FULL_NAME VARCHAR(255) NOT NULL,
		EMAIL VARCHAR(255) NOT NULL UNIQUE,
		PASSWORD VARCHAR(255) NOT NULL,
		ENABLED BOOLEAN NOT NULL,
		ACCOUNT_EXPIRED BOOLEAN NOT NULL,
		CREDENTIALS_EXPIRED BOOLEAN NOT NULL,
		ACCOUNT_LOCKED BOOLEAN NOT NULL,
		IS_ACTIVE BOOLEAN NOT NULL,
		IS_ADMIN BOOLEAN NOT NULL,
		API_ENABLED BOOLEAN NOT NULL,
        FIRST_LOGIN BOOLEAN NOT NULL,
		EXPIRED_DATE DATE
);

CREATE
	TABLE
		IF NOT EXISTS ROLE (ID SERIAL PRIMARY KEY,
		NAME VARCHAR(255),
		USERS_ID BIGINT,
		FOREIGN KEY (USERS_ID) REFERENCES USERS (ID)
		);

CREATE
	TABLE
		IF NOT EXISTS GROUPS (ID SERIAL PRIMARY KEY,
		GROUP_NAME VARCHAR(255),
		USERS_ID BIGINT,
		FOREIGN KEY (USERS_ID) REFERENCES USERS (ID)
		);

CREATE
	TABLE
		IF NOT EXISTS PERMISSIONS (ID SERIAL PRIMARY KEY,
		PERMISSION_NAME VARCHAR(255),
		PERMISSION_VALUE VARCHAR(255),
		GROUPS_ID BIGINT,
		FOREIGN KEY (GROUPS_ID) REFERENCES GROUPS (ID)
		);

--INSERT INTO PERMISSIONS (PERMISSION_NAME) VALUES ('ADD');
--
--INSERT INTO PERMISSIONS (PERMISSION_NAME) VALUES ('VIEW');
--
--INSERT INTO PERMISSIONS (PERMISSION_NAME) VALUES ('DELETE');
--
--INSERT INTO PERMISSIONS (PERMISSION_NAME) VALUES ('UPDATE');

INSERT INTO PERMISSIONS (PERMISSION_NAME, PERMISSION_VALUE) VALUES ('CORE', 'giv/api/core');

INSERT INTO PERMISSIONS (PERMISSION_NAME, PERMISSION_VALUE) VALUES ('INVENTORY', 'giv/api/external/inventory');

INSERT INTO PERMISSIONS (PERMISSION_NAME, PERMISSION_VALUE) VALUES ('CAMPAIGN', 'giv/api/external/campaign');

INSERT INTO PERMISSIONS (PERMISSION_NAME, PERMISSION_VALUE) VALUES ('CHANNEL', 'giv/api/external/channel');

insert
	into
		USERS (USERNAME, FULL_NAME,
		EMAIL,
		PASSWORD,
		ENABLED,
		ACCOUNT_EXPIRED,
		CREDENTIALS_EXPIRED,
		ACCOUNT_LOCKED,
		IS_ACTIVE,
		IS_ADMIN,
		API_ENABLED,
		FIRST_LOGIN,
		EXPIRED_DATE)
	values ('umadmin', 'UM Administrator', 'bdinesh@s3groupinc.com',
	'$2a$10$53soz/n.T.ShzzZQPfIonOG.vF8URPGHGPRULwdI8CkhX0inkCHaO',
	TRUE,
	FALSE,
	FALSE,
	FALSE, TRUE, TRUE, TRUE, TRUE,
     now() + interval '60 days');

insert	into ROLE (NAME, USERS_ID)
	values ('ROLE_ADMIN', 1),
		('ROLE_USER', 1);

insert	into GROUPS (GROUP_NAME, USERS_ID)
	values ('GIV', 1);

--insert	into PERMISSIONS (PERMISSION_NAME, PERMISSION_VALUE, GROUPS_ID)
--	values ('VIEW', 1),
--	('ADD', 1),
--	('UPDATE', 1),
--	('DELETE', 1);

insert	into PERMISSIONS (PERMISSION_NAME, PERMISSION_VALUE,  GROUPS_ID)
	values ('CORE', 'giv/api/core', 1),
	('INVENTORY', 'giv/api/external/inventory', 1),
	('CAMPAIGN', 'giv/api/external/campaign', 1),
	('CHANNEL', 'giv/api/external/channel', 1);