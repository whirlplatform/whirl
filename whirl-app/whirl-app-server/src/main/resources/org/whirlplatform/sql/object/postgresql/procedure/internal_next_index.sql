/**
 * !!!ВНИМАНИЕ: Функция не должна использоваться извне и необходима для обеспечения работы внутреннего функционала платформы
 *
 * @param p_result          Переменная результата выполнения события
 *
 * @return Количество ключей из переданной переменной
 */
CREATE OR REPLACE FUNCTION internal_next_index (
    p_result function_result)
    RETURNS bigint
    LANGUAGE plpgsql
AS $function$
DECLARE
    v_result bigint;
BEGIN
    SELECT count (*) INTO v_result FROM jsonb_object_keys(p_result.parameter_index);

    RETURN v_result;
END;
$function$
;