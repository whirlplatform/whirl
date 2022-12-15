/**
 * Находит значение параметра "parameter_value" переменной "p_input" по ключу "p_code"
 *
 * @param p_input           Переменная типа "function_input"
 * @param p_code            Переменная типа "character" без ограничений
 *
 * @return Возвращает id в виде текст
 */
CREATE OR REPLACE FUNCTION get_parameter_list_id(p_input function_input, p_code character varying)
 RETURNS text
 LANGUAGE plpgsql
AS $function$
declare
 v_val text; 
begin
	v_val := (p_input.parameter_value -> p_code)::text;
    return v_val;
	END;
$function$
;
