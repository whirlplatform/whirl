/**
 * Возвращает значение идентификатора параметра по коду
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
DECLARE
    v_val text;
BEGIN
    v_val := (p_input.parameter_value ->> p_code);
    RETURN v_val;
END;
$function$
;