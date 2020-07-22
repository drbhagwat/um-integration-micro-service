insert into users(name, password, active,
    first_successful_login, is_most_recent_login_successful, created_date_time,
    created_user, last_updated_date_time, last_updated_user)
	values ('admin', '$2a$10$v0DshXxSGW2Yp49X2vZCvOITwhSBL9oVok4tcxmE9NNWAvXm6U5ye', 'true',
	'false', 'false', now(),
	'admin', now(), 'admin')
	ON CONFLICT (name) DO NOTHING;

insert into role(name, users_name, created_date_time, created_user,
    last_updated_date_time, last_updated_user)
	values ('ROLE_ADMIN', 'admin', now(), 'admin',
	now(), 'admin')
	ON CONFLICT (name) DO NOTHING;

insert into users_role(users_name, role_name, created_date_time,
    created_user, last_updated_date_time, last_updated_user)
	values ('admin', 'ROLE_ADMIN', now(),
	'admin', now(), 'admin')
	ON CONFLICT (users_name, role_name) DO NOTHING;