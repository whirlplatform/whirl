/**
 * Считает количество параметров входящей переменной
 *
 * @param p_input     Переменная входных данных вызванного события
 *
 * @return Количество параметров
 */
CREATE OR REPLACE FUNCTION get_parameters_count(p_input function_input)
    RETURNS numeric
    LANGUAGE plpgsql
AS $function$
DECLARE
    v_index numeric;
BEGIN
    SELECT count(*) INTO v_index FROM jsonb_object_keys(p_input.parameter_index);
    RETURN v_index;
END;
$function$
;