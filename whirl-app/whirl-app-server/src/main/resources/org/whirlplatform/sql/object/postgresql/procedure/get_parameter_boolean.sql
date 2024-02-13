/**
 * Возвращает значение параметра типа "boolean" по коду
 * Если параметр по указанному коду отсутствует, то возвращается null
 *
 * @param p_input           Переменная входных данных вызванного события
 * @param p_code            Код параметра
 *
 * @return  true/false
 */
CREATE OR REPLACE FUNCTION get_parameter_boolean(p_input function_input, p_code character varying)
    RETURNS boolean
    LANGUAGE plpgsql
AS $function$
DECLARE
    v_bool boolean;
    v_input jsonb;
BEGIN
    v_input := p_input.parameter_value -> p_code;
    v_bool := (v_input ->> 0)::boolean;
    RETURN v_bool;
EXCEPTION
    WHEN OTHERS THEN
        RAISE NOTICE 'Invalid boolean value: "%". Returning NULL.', v_input;
        RETURN NULL;
END;
$function$
;