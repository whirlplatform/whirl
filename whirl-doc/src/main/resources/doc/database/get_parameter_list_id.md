[Содержание](index.md)

# **GET_PARAMETER_LIST_ID Function**
Возвращает значение идентификатора параметра по коду.

### Parameters
| Name     | Description                                  |
|----------|----------------------------------------------|
| p_input  | Переменная входных данных вызванного события |
| p_code   | Код параметра                                |
| *return* | Id в виде текста                             |

### Syntax
    CREATE OR REPLACE FUNCTION get_parameter_list_id(
        p_input function_input, 
        p_code character varying)
        RETURNS text
        LANGUAGE plpgsql
    AS $$
    DECLARE
        v_val text;
    BEGIN
        v_val := (p_input.parameter_value ->> p_code);
        RETURN v_val;
    END;
    $$;
