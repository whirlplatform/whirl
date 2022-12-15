CREATE OR REPLACE FUNCTION get_parameter_boolean(p_input function_input, p_code character varying)
 RETURNS boolean
 LANGUAGE plpgsql
AS $function$
declare 
	v_bool boolean;
	v_input text;
	begin
		v_input:= p_input.parameter_value -> p_code;
		v_bool := v_input::boolean;
 return v_bool;
 	 EXCEPTION
    WHEN OTHERS THEN
        RAISE NOTICE 'Invalid date value: "%".  Returning NULL.', v_input;
      return null;
	END;
$function$
;
