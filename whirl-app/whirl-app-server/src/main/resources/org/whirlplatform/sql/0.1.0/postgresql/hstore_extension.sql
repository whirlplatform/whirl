DO '
BEGIN
    IF (TO_NUMBER(current_setting(''server_version''), ''999999.99'') >= 13) then
        CREATE EXTENSION hstore;
    end IF;
END;
'
