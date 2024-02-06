/**
 * Добавляет параметр типа "boolean" к результату выполнения события
 *
 * @param p_result          Переменная результата выполнения события
 * @param p_code            Код параметра
 * @param p_value           Значение параметра
 *
 * @return  Переданную переменную типа "function_result" с новыми значениями
 */
CREATE OR REPLACE FUNCTION add_parameter_boolean (
    p_result function_result,
    p_code varchar,
    p_value boolean)
    RETURNS function_result
    LANGUAGE plpgsql
AS $$
DECLARE
    v_index integer;
    v_value jsonb;
BEGIN
    SELECT count(*) INTO v_index FROM jsonb_object_keys(p_result.parameter_index);
    v_index := v_index + 1;

    IF p_value IS NOT NULL THEN
        v_value := to_jsonb(p_value);
    ELSE
        v_value := NULL;
    END IF;

    p_result.parameter_index := jsonb_set(p_result.parameter_index, array[v_index]::text[], to_jsonb(p_code));
    p_result.parameter_value := jsonb_set(p_result.parameter_value, array[p_code], v_value);
    p_result.parameter_type := jsonb_set(p_result.parameter_type, array[p_code], to_jsonb('BOOLEAN'::varchar));

    RETURN p_result;
END;
$$
;