[Содержание](index.md)

# **GET_PARAMETER_NUMBER Function**
Возвращает значение параметра типа "number" по коду.
Если параметр по указанному коду отсутствует, то возвращается null.

### Parameters
| Name     | Description                                  |
|----------|----------------------------------------------|
| p_input  | Переменная входных данных вызванного события |
| p_code   | Код параметра                                |
| *return* | Число                                        |

### Syntax
    CREATE OR REPLACE FUNCTION get_parameter_number(
        p_input function_input, 
        p_code character varying)
        RETURNS numeric
        LANGUAGE plpgsql
    AS $$
    DECLARE
        v_num numeric;
        v_val text;
    BEGIN
        v_val := (p_input.parameter_value ->> p_code);
        v_num := v_val::numeric;
        RETURN v_num;
        EXCEPTION
            WHEN OTHERS THEN
                RAISE NOTICE 'Invalid numeric value: "%". Returning NULL.', v_val;
                    RETURN NULL;
    END;
    $$;

