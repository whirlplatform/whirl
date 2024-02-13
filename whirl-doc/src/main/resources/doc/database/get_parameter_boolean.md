[Содержание](index.md)

# **GET_PARAMETER_BOOLEAN Function**
Возвращает значение параметра типа "boolean" по коду.
Если параметр по указанному коду отсутствует, то возвращается null.

### Parameters
| Name     | Description                                  |
|----------|----------------------------------------------|
| p_input  | Переменная входных данных вызванного события |
| p_code   | Код параметра                                |
| *return* | true/false                                   |

### Syntax
    CREATE OR REPLACE FUNCTION get_parameter_boolean(
        p_input function_input, 
        p_code character varying)
        RETURNS boolean
        LANGUAGE plpgsql
    AS $$
    DECLARE
        v_bool boolean;
        v_input jsonb;
    BEGIN
        v_input := p_input.parameter_value -> p_code;
        v_bool := (v_input ->> 0)::boolean;
        RETURN v_bool;
        EXCEPTION
            WHEN OTHERS THEN
                RAISE NOTICE 'Invalid boolean value: "%". Returning NULL.', v_input;
                RETURN NULL;
    END;
    $$;
