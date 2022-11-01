CREATE OR REPLACE FUNCTION whirl.get_parameter_codes(p_input function_input)
 RETURNS text[]
 LANGUAGE plpgsql
AS $function$
BEGIN
    return akeys (p_input.parameter_index);
END;
$function$
;
