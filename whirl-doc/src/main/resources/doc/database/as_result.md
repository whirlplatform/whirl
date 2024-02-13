[Содержание](index.md)

# **AS_RESULT Function**
Функция формирует ответ возвращаемый в качестве результата выполнения события. 
Сериализует тип "function_result" в текстовое представление.

### Parameters
| Name      | Description                                        |
|-----------|----------------------------------------------------|
| p_result  | Переменная результата выполнения события           |
| *return*  | Текст со значением параметров входящей переменной  |

### Syntax
    CREATE OR REPLACE FUNCTION as_result(p_result function_result)
        RETURNS text
        LANGUAGE plpgsql
    AS $$
    DECLARE
        v_result          jsonb;
        v_parameter_array jsonb[];
        v_parameter       jsonb;
        v_parameter_rec   record;
        v_parameter_index integer;
        v_parameter_code  varchar(4000);
        v_parameter_type  varchar(4000);
        v_out             text;
    BEGIN
        v_result := '{}'::jsonb;
    
        IF p_result.message IS NOT NULL
        THEN
            v_result := v_result || jsonb_build_object('message', p_result.message);
            v_result := v_result || jsonb_build_object('title', p_result.title);
            v_result := v_result || jsonb_build_object('messageType', p_result.message_type);
        END IF;
    
        IF p_result.next_event IS NOT NULL
        THEN
            v_result := v_result || jsonb_build_object('nextEvent', p_result.next_event);
        END IF;
    
        v_parameter_array := '{}';
        v_parameter_index := 0;
    
        FOR v_parameter_rec IN (SELECT *
                    FROM jsonb_each_text(p_result.parameter_index))
        LOOP
            v_parameter_index := v_parameter_index + 1;

            v_parameter := '{}'::jsonb;

            v_parameter := v_parameter || jsonb_build_object('index', v_parameter_index::varchar);

            v_parameter_code := v_parameter_rec.value;
            v_parameter := v_parameter || jsonb_build_object('code', v_parameter_code);

            IF p_result.parameter_component ? v_parameter_code
            THEN
                v_parameter := v_parameter || jsonb_build_object('component', p_result.parameter_component -> v_parameter_code);
            ELSE
                v_parameter_type := p_result.parameter_type ->> v_parameter_code;
                v_parameter := v_parameter || jsonb_build_object('type', v_parameter_type);

                IF v_parameter_type = 'LIST'
                THEN
                    v_parameter := v_parameter || jsonb_build_object('title', p_result.parameter_list_title ->> v_parameter_code);
                END IF;

                v_parameter := v_parameter || jsonb_build_object('value', p_result.parameter_value -> v_parameter_code);
            END IF;

            v_parameter_array[v_parameter_index] := v_parameter;
        END LOOP;

        v_result := v_result || jsonb_build_object('parameters', v_parameter_array);
        v_out := jsonb_build_object('result', v_result);
        RETURN v_out;
    END;
    $$;
