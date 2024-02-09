[Содержание](index.md)

# **GET_PARAMETER_CODES Function**
Возвращает список кодов параметров вызванного события

### Parameters
| Name     | Description                                  |
|----------|----------------------------------------------|
| p_input  | Переменная входных данных вызванного события |
| *return* | Значения в виде массива текста               |

### Syntax
    CREATE OR REPLACE FUNCTION get_parameter_codes(
        p_input function_input)
        RETURNS text[]
        LANGUAGE plpgsql
    AS $$
    BEGIN
        RETURN array(SELECT jsonb_object_keys(p_input.parameter_index));
    END;
    $$;
