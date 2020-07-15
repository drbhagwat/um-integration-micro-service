create
	table if not exists
		users (
		name varchar(255) primary key,
		password varchar(255) not null,
		active boolean not null
);

create
	table if not exists
		role (
		name varchar(255) primary key,
		users_name varchar(255) references users(name)
);

create
	table if not exists
		users_role (
		users_name varchar(255) references users(name),
		role_name varchar(255) references role(name),
		primary key(users_name, role_name)
);