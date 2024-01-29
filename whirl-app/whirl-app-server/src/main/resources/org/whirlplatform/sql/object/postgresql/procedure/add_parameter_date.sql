/**
 * Добавляет параметр типа "timestamp" к результату выполнения события.
 * Формат даты 'DD.MM.YYYY HH24:MI:SS'
 *
 * @param p_result          Переменная результата выполнения события
 * @param p_code            Код параметра
 * @param p_value           Значение параметра
 *
 * @return  Переданную переменную типа "function_result" с новыми значениями
 */
CREATE OR REPLACE FUNCTION add_parameter_date (
    p_result function_result,
    p_code varchar,
    p_value timestamp)
    RETURNS function_result
    LANGUAGE plpgsql
AS $function$
BEGIN
    p_result.parameter_index := jsonb_set(p_result.parameter_index, to_jsonb(jsonb_array_length(p_result.parameter_index)::text), to_jsonb(p_code));

    IF p_value IS NOT NULL THEN
        p_result.parameter_value := jsonb_set(p_result.parameter_value, to_jsonb(p_code), to_jsonb(to_char(p_value, 'DD.MM.YYYY HH24:MI:SS')));
    END IF;

    p_result.parameter_type := jsonb_set(p_result.parameter_type, to_jsonb(p_code), to_jsonb('DATE'::varchar));

    RETURN p_result;
END;
$function$
LANGUAGE plpgsql
;