[Содержание](index.md)

# **ADD_PARAMETER_COMPONENT Function**
Добавляет параметр типа "varchar" к результату выполнения события

### Parameters
| Name             | Description                                                      |
|------------------|------------------------------------------------------------------|
| p_result         | Переменная результата выполнения события                         |
| p_code           | Код параметра                                                    |
| p_component_code | Значение параметра                                               |
| *return*         | Переданную переменную типа "function_result" с новыми значениями |

### Syntax
     CREATE OR REPLACE FUNCTION add_parameter_component (
    p_result function_result,
    p_code varchar,
    p_component_code varchar)
    RETURNS function_result
    AS
    $$
    DECLARE
    v_index integer;
    BEGIN
    v_index := internal_next_index (p_result);

    IF (SELECT count (*)
          FROM skeys (p_result.parameter_index)) > 0
    THEN
        p_result.parameter_index := p_result.parameter_index::hstore || hstore (v_index::varchar, p_code);
    ELSE
        p_result.parameter_index := hstore (v_index::varchar, p_code);
    END IF;

    IF (SELECT count (*)
          FROM skeys (p_result.parameter_component)) > 0
    THEN
        p_result.parameter_component := p_result.parameter_component::hstore || hstore (p_code, p_component_code);
    ELSE
        p_result.parameter_component := hstore (p_code, p_component_code);
    END IF;

    RETURN p_result;
    END;
