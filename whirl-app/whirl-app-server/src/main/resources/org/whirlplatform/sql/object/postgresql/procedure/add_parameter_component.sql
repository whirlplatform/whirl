/**
 * Добавляет параметр типа "varchar" к результату выполнения события
 *
 * @param p_result          Переменная результата выполнения события
 * @param p_code            Код параметра
 * @param p_component_code  Значение параметра
 *
 * @return Переданную переменную типа "function_result" с новыми значениями
 */
CREATE OR REPLACE FUNCTION add_parameter_component (
    p_result function_result,
    p_code varchar,
    p_component_code varchar)
    RETURNS function_result
    LANGUAGE plpgsql
AS $function$
BEGIN
    p_result.parameter_index := jsonb_set(p_result.parameter_index, to_jsonb(jsonb_array_length(p_result.parameter_index)), to_jsonb(p_code));
    p_result.parameter_component := jsonb_set(p_result.parameter_component, to_jsonb(p_code), to_jsonb(p_component_code));

    RETURN p_result;
END;
$function$
LANGUAGE plpgsql
;