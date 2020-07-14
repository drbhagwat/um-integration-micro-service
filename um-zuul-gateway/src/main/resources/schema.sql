create
	table if not exists
		users (
		user_name varchar(255) primary key,
		password varchar(255) not null,
		active boolean not null,
		roles varchar(255)
);