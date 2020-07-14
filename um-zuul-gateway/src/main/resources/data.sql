insert
	into
		users (user_name,
		password,
		active, roles)
	values ('admin',
	'$2a$10$v0DshXxSGW2Yp49X2vZCvOITwhSBL9oVok4tcxmE9NNWAvXm6U5ye',
    'true', 'ROLE_ADMIN') ON CONFLICT (user_name) DO NOTHING;
