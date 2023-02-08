DO '
BEGIN
    IF (TO_NUMBER(current_setting(''server_version''), ''L9G999g999.99'') >= 13) then
        DROP EXTENSION hstore;
    end IF;
END;
'
