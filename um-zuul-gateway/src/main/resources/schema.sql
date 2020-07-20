create
	table if not exists
		users (
		name varchar(255) primary key,
		password varchar(255) not null,
		active boolean not null,
		created_date_time TIMESTAMPTZ,
		created_user VARCHAR(255),
		last_updated_date_time TIMESTAMPTZ,
		last_updated_user VARCHAR(255)
);

create
	table if not exists
		role (
		name varchar(255) primary key,
		users_name varchar(255) references users(name),
		created_date_time TIMESTAMPTZ,
		created_user VARCHAR(255),
		last_updated_date_time TIMESTAMPTZ,
		last_updated_user VARCHAR(255)
);

create
	table if not exists
		users_role (
		users_name varchar(255) references users(name),
		role_name varchar(255) references role(name),
		created_date_time TIMESTAMPTZ,
		created_user varchar(255),
		last_updated_date_time TIMESTAMPTZ,
		last_updated_user varchar(255),
		primary key(users_name, role_name)
);