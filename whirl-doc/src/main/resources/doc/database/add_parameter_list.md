[Содержание](index.md)

# **ADD_PARAMETER_LIST Function**
Добавляет параметр типа "LIST" к результату выполнения события

### Parameters
| Name         | Description                                                      |
|--------------|------------------------------------------------------------------|
| p_result     | Переменная результата выполнения события                         |
| p_code       | Код параметра                                                    |
| p_list_title | Отображаемое значение(лейбл) элемента                            |
| p_list_value | Идентификатор элемента                                           |
| *return*     | Переданную переменную типа "function_result" с новыми значениями |

### Syntax
    CREATE OR REPLACE FUNCTION add_parameter_list (
        p_result function_result,
        p_code varchar,
        p_list_title varchar,
        p_list_value varchar)
        RETURNS function_result
        LANGUAGE plpgsql
    AS $$
    DECLARE
        v_index integer;
    BEGIN
        SELECT count(*) INTO v_index FROM jsonb_object_keys(p_result.parameter_index);
    
        p_result.parameter_index := jsonb_set(p_result.parameter_index, array[v_index + 1]::text[], to_jsonb(p_code));
    
        p_result.parameter_list_title := jsonb_set(p_result.parameter_list_title, array[p_code], to_jsonb(p_list_title));
    
        p_result.parameter_value := jsonb_set(p_result.parameter_value, array[p_code], to_jsonb(p_list_value));
    
        p_result.parameter_type := jsonb_set(p_result.parameter_type, array[p_code], to_jsonb('LIST'::varchar));
    
        RETURN p_result;
    END;
    $$;
