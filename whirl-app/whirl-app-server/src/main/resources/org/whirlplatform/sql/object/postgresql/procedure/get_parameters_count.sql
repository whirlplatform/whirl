/**
 * Считает количество ключей в параметре "parameter_index" входящей переменной типа "function_input"
 *
 * @param p_input           Переменная типа "function_input"
 *
 * @return Возвращает количество ключей параметра "parameter_index"
 */
CREATE OR REPLACE FUNCTION get_parameters_count(p_input function_input)
 RETURNS numeric
 LANGUAGE plpgsql
AS $function$
declare 
	v_index numeric;
begin
	SELECT count (*) INTO v_index FROM skeys(p_input.parameter_index);
    return v_index;
END;
$function$
;
