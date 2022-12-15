/**
 * Находит значение параметра "parameter_value" переменной "p_input" по ключу - "p_code"
 * Если значение "p_code" не валидно возвращает null
 *
 * @param p_input           Переменная типа "function_input"
 * @param p_code            Переменная типа "character" без ограничений
 *
 * @return Возвращает true/false
 */
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
