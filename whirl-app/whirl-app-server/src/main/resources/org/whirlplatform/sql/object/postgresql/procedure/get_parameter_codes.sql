/**
 * Возвращает список кодов параметров вызванного события
 *
 * @param p_input           Переменная входных данных вызванного события
 *
 * @return Значения в виде массива текста
 */
CREATE OR REPLACE FUNCTION get_parameter_codes(p_input function_input)
    RETURNS text[]
    LANGUAGE plpgsql
AS $function$
BEGIN
    RETURN array(SELECT jsonb_object_keys(p_input.parameter_index));
END;
$function$
;
