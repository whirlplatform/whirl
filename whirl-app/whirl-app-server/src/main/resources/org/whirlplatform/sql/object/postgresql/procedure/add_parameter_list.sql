/**
 * Добавляет параметр типа "LIST" к результату выполнения события
 *
 * @param p_result          Переменная результата выполнения события
 * @param p_code            Код параметра
 * @param p_list_title      Отображаемое значение(лейбл) элемента
 * @param p_list_value      Идентификатор элемента
 *
 * @return Переданную переменную типа "function_result" с новыми значениями
 */
CREATE OR REPLACE FUNCTION add_parameter_list (
    p_result function_result,
    p_code varchar,
    p_list_title varchar,
    p_list_value varchar)
    RETURNS function_result
    LANGUAGE plpgsql
AS $function$
BEGIN
    p_result.parameter_index := jsonb_set(p_result.parameter_index, to_jsonb(jsonb_array_length(p_result.parameter_index)), to_jsonb(p_code));

    p_result.parameter_list_title := jsonb_set(p_result.parameter_list_title, to_jsonb(p_code), to_jsonb(p_list_title));

    p_result.parameter_value := jsonb_set(p_result.parameter_value, to_jsonb(p_code), to_jsonb(p_list_value));

    p_result.parameter_type := jsonb_set(p_result.parameter_type, to_jsonb(p_code), to_jsonb('LIST'::varchar));

    RETURN p_result;
END;
$function$
LANGUAGE plpgsql
;