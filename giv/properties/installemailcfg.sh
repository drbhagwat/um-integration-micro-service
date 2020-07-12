#!/bin/bash

# define GIV config directory - should be absolute path
file="$CONF_DIR"/email.cfg

# remove existing config fiel if any
sudo rm -f "$file"

# create an empty email config file
sudo touch "$file"

# set proper permission - ideally rw for owner, and r for group and others (644) but 777 for now
sudo chmod 777 "$file"

# write all static e-mail configuration first
cat <<EOF >>"$file"
spring.mail.isHtml=true
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.connectiontimeout=5000
spring.mail.properties.mail.smtp.timeout=5000
spring.mail.properties.mail.smtp.writetimeout=5000
spring.mail.properties.mail.smtp.starttls.enable=true
registrationSubject: You are successfully registered with GIV.
updateSubject: Your GIV credentials are successfully updated.
SUBJECT_AVAILABLE_ZERO_ALERT: alert
RECIPIENTS: msunderesh@s3groupinc.com, vboopathi@s3groupinc.com
EOF

read -p "Do you want e-mail to be enabled?  [press Enter key to accept the default value n]: " emailEnabled
emailEnabled=${emailEnabled:-n}

if [ "$emailEnabled" == "n" ]; then
  {
    echo "spring.mail.enabled=n"
    echo "spring.mail.host="
    echo "spring.mail.port="
    echo "spring.mail.username="
    echo "spring.mail.password="
  } >> "$file"
else
  echo "spring.mail.enabled=y" >>"$file"

  host=""
  while [ -z $host ]; do
    read -p "Please enter the e-mail host (mandatory - e.g., smtp.gmail.com): " host

    if [[ -z "$host" ]]; then
      :
    else
      echo "spring.mail.host=$host" >>"$file"
    fi
  done

  port=""
  while [ -z $port ]; do
    read -p "Please enter the port number on this host (mandatory - e.g., 587): " port

    if [[ -z "$port" ]]; then
      :
    else
      echo "spring.mail.port=$port" >>"$file"
    fi
  done

  email=""
  while [ -z $email ]; do
    read -p "Please enter sender's e-mail id (mandatory - e.g., givadmin@s3groupinc.com): " email

    if [[ -z "$email" ]]; then
      :
    else
      echo "spring.mail.username=$email" >>"$file"
    fi
  done

  password=""
  while [ -z $password ]; do
    read -s -p "Please enter valid password of this e-mail: (mandatory): " password

    if [[ -z "$password" ]]; then
      :
    else
      echo "spring.mail.password=$password" >>"$file"
    fi
  done
fi