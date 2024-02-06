/**
 * Добавляет параметр типа "varchar" к результату выполнения события
 *
 * @param p_result          Переменная результата выполнения события
 * @param p_code            Код параметра
 * @param p_value           Значение параметра
 *
 * @return  Переданную переменную типа "function_result" с новыми значениями
 */
CREATE OR REPLACE FUNCTION add_parameter_varchar (
    p_result function_result,
    p_code varchar,
    p_value varchar)
    RETURNS function_result
    LANGUAGE plpgsql
AS $$
DECLARE
    v_index integer;
BEGIN
    SELECT count(*) INTO v_index FROM jsonb_object_keys(p_result.parameter_index);

    p_result.parameter_index := jsonb_set(p_result.parameter_index, array[v_index + 1]::text[], to_jsonb(p_code));

    p_result.parameter_value := jsonb_set(p_result.parameter_value, array[p_code], to_jsonb(p_value));

    p_result.parameter_type := jsonb_set(p_result.parameter_type, array[p_code], to_jsonb('STRING'::varchar));

    RETURN p_result;
END;
$$
;