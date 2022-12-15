/**
 * Возвращает все значения параметра "parameter_index"
 *
 * @param p_input           Переменная типа "function_input"
 *
 * @return Возвращает значения в виде массива текста
 */
CREATE OR REPLACE FUNCTION get_parameter_codes(p_input function_input)
 RETURNS text[]
 LANGUAGE plpgsql
AS $function$
BEGIN
    return akeys (p_input.parameter_index);
END;
$function$
;
