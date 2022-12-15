/**
 * Находит значение параметра "parameter_value" переменной "p_input" по ключу - "p_code"
 * Если значение "p_code" не валидно возвращает null
 *
 * @param p_input           Переменная типа "function_input"
 * @param p_code            Переменная типа "character" без ограничений
 *
 * @return Возвращает дату в формате "DD.MM.YYYY HH24:MI:SS"
 */
CREATE OR REPLACE FUNCTION get_parameter_date(p_input function_input, p_code character varying)
 RETURNS timestamp without time zone
 LANGUAGE plpgsql
AS $function$
	declare 
	v_time timestamp;
	v_input text;
	begin
		v_input := (p_input.parameter_value -> p_code);
		v_time := to_timestamp(v_input, 'DD.MM.YYYY HH24:MI:SS');
	 return v_time;
	 EXCEPTION
    WHEN OTHERS THEN
        RAISE NOTICE 'Invalid date value: "%".  Returning NULL.', v_input;
      return null;
	END;
$function$
;
