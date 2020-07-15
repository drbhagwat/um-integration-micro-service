insert
	into
		users (name,
		password,
		active)
	values ('admin',
	'$2a$10$v0DshXxSGW2Yp49X2vZCvOITwhSBL9oVok4tcxmE9NNWAvXm6U5ye',
    'true') ON CONFLICT (name) DO NOTHING;

insert
	into
		role (name,
		users_name)
	values ('ROLE_ADMIN',
	'admin') ON CONFLICT (name) DO NOTHING;

insert
	into
		users_role (users_name,
		role_name)
	values ('admin', 'ROLE_ADMIN') ON CONFLICT (users_name, role_name) DO NOTHING;
