[Содержание](index.md)

# **GET_PARAMETER_TEXT Function**
Возвращает значение параметра типа "text" по коду.

### Parameters
| Name     | Description                                    |
|----------|------------------------------------------------|
| p_input  | Переменная входных данных вызванного события   |
| p_code   | Код параметра                                  |
| *return* | Текст со значением параметра "parameter_value" |

### Syntax
    CREATE OR REPLACE FUNCTION get_parameter_text(
        p_input function_input, 
        p_code character varying)
        RETURNS text
        LANGUAGE plpgsql
    AS $$
    BEGIN
        RETURN (p_input.parameter_value ->> p_code);
    END;
    $$;
