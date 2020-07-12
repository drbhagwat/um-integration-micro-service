#!/bin/bash

echo ""
echo ""

# Call  a script which drops all existing postgreql GIV tables
bash dropall.sh

echo ""
echo ""

read  -p "Enter id for the admin (or superuser) [press Enter key to accept the default value givadmin]: " USERNAME
USERNAME=${USERNAME:-givadmin}

read  -p "Enter full (or complete) name of this admin [press Enter key to accept the default value Giv Administrator]: " FULL_NAME
FULL_NAME=${FULL_NAME:-Giv Administrator}

read  -p "Enter e-mail id of this admin [press Enter key to accept the default value givadmin@s3groupinc.com]: " EMAIL
EMAIL=${EMAIL:-givadmin@s3groupinc.com}

read -s -p "Enter password for this admin [press Enter key to accept the default value password]: " PASSWORD
PASSWORD=${PASSWORD:-password}

# check the preequisite first
OUTPUT=$(htpasswd -nbBC 10 "$USERNAME" "$PASSWORD" | sed 's/$2y/$2a/')

if [ -z "$OUTPUT" ]; then
  echo "Please install htpasswd apache utility on this machine and then rerun this script.".
  exit
fi

encryptedPassword=$( echo "$OUTPUT" |cut -d\: -f2 )

echo ""
echo ""
echo Please write down the password you entered above somewhere safe and secure. Do not disclose it to anyone.
echo You will be able to change this password in GIV.
echo Password is encrypred for enhanced security.
echo ""
echo ""
echo "Please be patient, as GIV is prepared for initial use..."
echo ""
echo ""

# remove the existing insertall.sql file
sudo rm -f insertall.sql 

# create a new insertall.sql file
sudo touch insertall.sql 

# set proper permission - ideally rw for owner, and r for group and others but, 777 for now
sudo chmod 777 insertall.sql 

# create insertall.sql in the current directory, replacing the shell variables with what the user supplied
cat <<EOF >> insertall.sql
DROP TABLE IF EXISTS ROLE CASCADE; 
	CREATE
	TABLE
		ROLE (ID SERIAL PRIMARY KEY,
		NAME VARCHAR(255),
		CREATED_DATE_TIME TIMESTAMPTZ,
		CREATED_USER VARCHAR(255),
		LAST_UPDATED_DATE_TIME TIMESTAMPTZ,
		LAST_UPDATED_USER VARCHAR(255));

DROP TABLE IF EXISTS USERS CASCADE;
CREATE
	TABLE
		USERS (ID SERIAL PRIMARY KEY,
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
		CREATED_DATE_TIME TIMESTAMPTZ,
		CREATED_USER VARCHAR(255),
		LAST_UPDATED_DATE_TIME TIMESTAMPTZ,
		LAST_UPDATED_USER VARCHAR(255)
);

DROP TABLE IF EXISTS USERS_ROLE CASCADE;
CREATE
	TABLE
		USERS_ROLE (USERS_ID INT,
		FOREIGN KEY(USERS_ID) REFERENCES USERS(ID),
		ROLE_ID INT,
		FOREIGN KEY(ROLE_ID) REFERENCES ROLE(ID)
		);

		insert	into ROLE (NAME, CREATED_DATE_TIME,
		CREATED_USER,
		LAST_UPDATED_DATE_TIME,
		LAST_UPDATED_USER)
	values ('ROLE_ADMIN', now(), '$USERNAME', now(), '$USERNAME'),
		('ROLE_USER', now(), '$USERNAME', now(), '$USERNAME');

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
		CREATED_DATE_TIME,
		CREATED_USER,
		LAST_UPDATED_DATE_TIME,
		LAST_UPDATED_USER)
	values ( '$USERNAME', '$FULL_NAME', '$EMAIL',
	'$encryptedPassword',
	TRUE,
	FALSE,
	FALSE,
	FALSE, TRUE, TRUE, TRUE, TRUE,
	now(),
	'$USERNAME',
	now(),
	'$USERNAME');

insert
into USERS_ROLE (USERS_ID, ROLE_ID)
	values (1, 1), (1,2); 
EOF

# Call the script (should be in the same directory) to create necessary tables and insert data
bash insertall.sh

# Call the script (should be in the same directory) to configure email - server, host, sender's email and sender's password.
bash installemailcfg.sh

echo ""
echo ""
echo "Thanks for installing. You may proceed to use the GIV."
echo ""
echo ""

# need to automate the crontab entry for endofday campaign process
