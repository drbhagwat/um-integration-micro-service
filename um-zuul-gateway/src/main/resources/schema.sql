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