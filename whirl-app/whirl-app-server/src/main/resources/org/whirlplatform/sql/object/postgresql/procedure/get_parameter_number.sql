/**
 * Находит значение параметра "parameter_value" переменной "p_input" по ключу "p_code"
 * Если "p_code" не валидно возвращает null
 *
 * @param p_input           Переменная типа "function_input"
 * @param p_code            Переменная типа "character" без ограничений
 *
 * @return Возвращает число
 */
CREATE OR REPLACE FUNCTION get_parameter_number(p_input function_input, p_code character varying)
 RETURNS numeric
 LANGUAGE plpgsql
AS $function$
declare
 v_num numeric;
 v_val text;
begin
	v_val := p_input.parameter_value -> p_code;
	v_num := v_val::numeric;
return v_num;
  EXCEPTION
    WHEN OTHERS THEN
        RAISE NOTICE 'Invalid numeric value: "%".  Returning NULL.', v_val;
      return null;
END; 
$function$
;
