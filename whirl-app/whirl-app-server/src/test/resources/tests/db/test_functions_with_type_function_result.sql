do
$$
    declare
        v_check_result text;
        v_result function_result;
    begin

        v_result.title := '';
        v_result.message := '';
        v_result.message_type := '';
        v_result.next_event := '';
        v_result.parameter_value :='{}'::jsonb;
        v_result.parameter_list_title := '{}'::jsonb;
        v_result.parameter_type := '{}'::jsonb;
        v_result.parameter_component := '{}'::jsonb;
        v_result.parameter_index := '{}'::jsonb;

        --- тtest add_parameter_boolean
        v_result := add_parameter_boolean(v_result,'code-bool', p_value := true);
        ASSERT v_result.parameter_value -> 'code-bool' = 'true', 'Результата функции "add_parameter_boolean" не совпадает с ожидаемым значением.';

        --- test add_parameter_component
        v_result := add_parameter_component(v_result, 'code-com', 'component value');
        ASSERT v_result.parameter_component ->> 'code-com' = 'component value', 'Результата функции "add_parameter_component" не совпадает с ожидаемым значением.';

        --- test add_parameter_date
        v_result :=add_parameter_date(v_result, 'code-date', '01.02.2024 13:21:32'::timestamp without time zone);
        ASSERT v_result.parameter_value ->> 'code-date' = '02.01.2024 13:21:32', 'Результата функции "add_parameter_date" не совпадает с ожидаемым значением.';

        --- test add_parameter_list
        v_result :=add_parameter_list(v_result, 'code-list', 'list-title', 'list value');
        ASSERT v_result.parameter_list_title ->> 'code-list' = 'list-title', 'Параметр parameter_list_title в pезультатe функции "add_parameter_list" не совпадает с ожидаемым значением.';
        ASSERT v_result.parameter_value ->> 'code-list' = 'list value', 'Результата функции "add_parameter_list" не совпадает с ожидаемым значением.';

        --- test add_parameter_number
        v_result :=add_parameter_number(v_result, 'code-num', 123);
        ASSERT v_result.parameter_value -> 'code-num' = '123', 'Результата функции "add_parameter_number" не совпадает с ожидаемым значением.';


        --- test add_parameter_varchar
        v_result := add_parameter_varchar(v_result, 'code-txt', 'some text');
        ASSERT v_result.parameter_value -> 'code-txt' = '"some text"', 'Результата функции "add_parameter_varchare" не совпадает с ожидаемым значением.';

        --- test ginternal_next_index ---
        ASSERT internal_next_index(v_result) = 6, 'Результата функции "internal_next_index" не совпадает с ожидаемым значением.';

        --- test set_message ---
        v_result := set_message(v_result, 'Title message', 'Text message','WARN');
        ASSERT v_result.title = 'Title message', 'Параметр title в результате функции "set_message" не совпадает с ожидаемым значением.';
        ASSERT v_result.message = 'Text message', 'Параметр massage в результате функции "set_message" не совпадает с ожидаемым значением.';
        ASSERT v_result.message_type = 'WARN', 'Параметр message_type в результате функции "set_message" не совпадает с ожидаемым значением.';

        --- test set_next_event ---
        v_result := set_next_event(v_result, 'next-event');
        ASSERT v_result.next_event = 'next-event', 'Параметр next_event в результате функции "set_next_event" не совпадает с ожидаемым значением.';


        --- test as_result
        ASSERT as_result(v_result)::jsonb ? 'result', 'Результата функции "as_result" не совпадает с ожидаемым значением';
        v_check_result :=  as_result(v_result);
        ASSERT v_check_result::jsonb #>> '{result, title}' = 'Title message', 'Параметр title в результате функции "as_result" не совпадает с ожидаемым значением.';
        ASSERT v_check_result::jsonb #>> '{result, message}' = 'Text message', 'Параметр message в результате функции "as_result" не совпадает с ожидаемым значением.';
        ASSERT v_check_result::jsonb #>> '{result, nextEvent}' = 'next-event', 'Параметр nextEvent в результате функции "as_result" не совпадает с ожидаемым значением.';
        ASSERT v_check_result::jsonb #>> '{result, messageType}' = 'WARN', 'Параметр messageType в результате функции "as_result" не совпадает с ожидаемым значением.';
        ASSERT v_check_result::jsonb #>> '{result, parameters, 1, code}' = 'code-com', 'Параметр code в результате функции "as_result" не совпадает с ожидаемым значением.';
        ASSERT v_check_result::jsonb #>> '{result, parameters, 1, index}' = '2', 'Параметр index в результате функции "as_result" не совпадает с ожидаемым значением.';
        ASSERT v_check_result::jsonb #>> '{result, parameters, 1, component}' = 'component value', 'Параметр component в результате функции "as_result" не совпадает с ожидаемым значением.';
        ASSERT v_check_result::jsonb #>> '{result, parameters, 1, code}' = 'code-com', 'Параметр code в результате функции "as_result" не совпадает с ожидаемым значением.';
        ASSERT v_check_result::jsonb #>> '{result, parameters, 3, type}' = 'LIST', 'Параметр type в результате функции "as_result" не совпадает с ожидаемым значением.';
        ASSERT v_check_result::jsonb #>> '{result, parameters, 3, value}' = 'list value', 'Параметр value в результате функции "as_result" не совпадает с ожидаемым значением.';
        ASSERT v_check_result::jsonb #>> '{result, parameters, 3, title}' = 'list-title', 'Параметр title в результате функции "as_result" не совпадает с ожидаемым значением.';

    end
$$;