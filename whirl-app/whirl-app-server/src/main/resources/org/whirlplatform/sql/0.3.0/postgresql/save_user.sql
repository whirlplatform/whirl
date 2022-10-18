CREATE OR REPLACE FUNCTION whirl_admin.save_user(new_username text, new_email text, new_password text, new_password_repeat text)
 RETURNS text
 LANGUAGE plpgsql
AS $function$
    begin
		if new_username is null then
			return whirl_admin.show_message('Creating user with an empty name field is unallowed!', 'ERROR');
        end if;
		if LENGTH(new_username) < 3 then
			return whirl_admin.show_message('Username should be longer than 3 elements!', 'ERROR');
        end if;
		----
		if new_email is null then
			return whirl_admin.show_message('Creating user with an empty email field is unallowed!', 'ERROR');
        end if;
		if new_email not like '%@%' then
			return whirl_admin.show_message('Enter email in a proper manner!', 'ERROR');
        end if;
		----
		if new_password is null then
			return whirl_admin.show_message('Creating user with an empty password field is unallowed!', 'ERROR');
        end if;
		if new_password_repeat is null then
			-- RAISE EXCEPTION 'Enter both password fields!';
			-- return 'Enter both password fields!';
			return whirl_admin.show_message('Enter both password fields!', 'ERROR');
        end if;
		if LENGTH(new_password) <= 3 then
			return whirl_admin.show_message('Password should be longer than 3 elements!', 'ERROR');
        end if;
		if new_password_repeat != new_password then
			return whirl_admin.show_message('Password fields are not equal!', 'ERROR');
        end if;
        insert into whirl_users(login, name, email, password_hash, creation_date) values(CONCAT('user', get_last_id()), new_username, new_email, crypt(new_password, gen_salt('bf')), NOW());
        return whirl_admin.show_message('User was successfully created!', 'INFO');
    END;
$function$
;
