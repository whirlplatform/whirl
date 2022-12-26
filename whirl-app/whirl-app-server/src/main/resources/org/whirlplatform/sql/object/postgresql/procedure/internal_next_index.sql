/**
 * !!!ВНИМАНИЕ: Функция не должна использоваться извне и необходима для обеспечения работы внутреннего функционала платформы
 *
 * @param p_result          Переменная результата выполнения события
 *
 * @return Количество ключей из переданной переменной
 */
CREATE OR REPLACE FUNCTION internal_next_index (
    p_result function_result)
RETURNS integer
AS
$$
DECLARE
    v_result integer;
BEGIN
    SELECT count (*) INTO v_result FROM skeys (p_result.parameter_index);

    RETURN v_result;
END;
$$
LANGUAGE plpgsql