DO '
BEGIN
    IF (TO_NUMBER(current_setting(''server_version''), ''L9G999g999.99'') >= 13) then
        CREATE EXTENSION IF NOT EXISTS hstore;
    end IF;
END;
'