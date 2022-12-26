/**
 * Возвращает значение идентификатора переметра по коду
 *
 * @param p_input           Переменная входных данных вызванного события
 * @param p_code            Код параметра
 *
 * @return  Id в виде текста
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
