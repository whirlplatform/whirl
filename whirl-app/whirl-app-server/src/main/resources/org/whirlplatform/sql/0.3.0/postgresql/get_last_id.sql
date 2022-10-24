CREATE OR REPLACE FUNCTION get_last_id()
    RETURNS text
    LANGUAGE plpgsql
AS $function$
begin
    return (select nextval('whirl_users_id_seq'))::text;
END;
$function$
;
