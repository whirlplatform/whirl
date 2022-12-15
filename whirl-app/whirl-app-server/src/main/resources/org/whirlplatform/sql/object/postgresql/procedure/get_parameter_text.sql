CREATE OR REPLACE FUNCTION get_parameter_text(p_input function_input, p_code character varying)
 RETURNS text
 LANGUAGE plpgsql
AS $function$
	BEGIN
		return (p_input.parameter_value -> p_code)::text;	
	END;
$function$
;
