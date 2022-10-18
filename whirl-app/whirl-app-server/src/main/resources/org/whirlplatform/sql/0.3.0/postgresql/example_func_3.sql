CREATE OR REPLACE FUNCTION example_func_3()
    RETURNS text
    LANGUAGE plpgsql
AS $function$
begin
    return 'okay_3';
END;
$function$
;