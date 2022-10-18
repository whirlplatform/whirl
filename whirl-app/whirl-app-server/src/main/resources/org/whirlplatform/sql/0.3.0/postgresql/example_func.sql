CREATE OR REPLACE FUNCTION whirl_admin.example_func()
    RETURNS text
    LANGUAGE plpgsql
AS $function$
    begin
        return 'okay';
    END;
$function$
;