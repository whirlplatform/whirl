[Содержание](index.md)

# **GET_PARAMETER_COUNT Function**
Считает количество параметров входящей переменной.

### Parameters
| Name     | Description                                  |
|----------|----------------------------------------------|
| p_input  | Переменная входных данных вызванного события |
| *return* | Количество параметров                        |

### Syntax
    CREATE OR REPLACE FUNCTION get_parameters_count(p_input function_input)
        RETURNS numeric
        LANGUAGE plpgsql
    AS $$
    DECLARE
        v_index numeric;
    BEGIN
        SELECT count(*) INTO v_index FROM jsonb_object_keys(p_input.parameter_index);
        RETURN v_index;
    END;
    $$;
