/**
 * Возвращает значение параметра "DATA" по коду
 * Если параметр по указанному коду отсутствует, то возвращается null
 *
 * @param p_input           Переменная входных данных вызванного события
 * @param p_code            Код параметра
 *
 * @return Переменную типа "timestamp" без часового пояса
 */
CREATE OR REPLACE FUNCTION get_parameter_date(p_input function_input, p_code character varying)
    RETURNS timestamp without time zone
    LANGUAGE plpgsql
AS $function$
DECLARE
    v_time timestamp;
    v_input text;
BEGIN
    v_input := (p_input.parameter_value ->> p_code);
    v_time := to_timestamp(v_input, 'DD.MM.YYYY HH24:MI:SS');
    RETURN v_time;
EXCEPTION
    WHEN OTHERS THEN
        RAISE NOTICE 'Invalid date value: "%". Returning NULL.', v_input;
        RETURN NULL;
END;
$function$
;