/**
 * Находит значение параметра "parameter_value" переменной "p_input" по ключу "p_code"
 *
 * @param p_input           Переменная типа "function_input"
 * @param p_code            Переменная типа "character" без ограничений
 *
 * @return Возвращает текст со значением параметра "parameter_value"
 */
CREATE OR REPLACE FUNCTION get_parameter_text(p_input function_input, p_code character varying)
 RETURNS text
 LANGUAGE plpgsql
AS $function$
	BEGIN
		return (p_input.parameter_value -> p_code)::text;	
	END;
$function$
;
