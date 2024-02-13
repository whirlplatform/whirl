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
AS $$
DECLARE
    v_index integer;
BEGIN
    SELECT count(*) INTO v_index FROM jsonb_object_keys(p_result.parameter_index);

    p_result.parameter_index := jsonb_set(p_result.parameter_index, array[v_index + 1]::text[], to_jsonb(p_code));
    p_result.parameter_component := jsonb_set(p_result.parameter_component, array[p_code], to_jsonb(p_component_code));

    RETURN p_result;
END;
$$
;