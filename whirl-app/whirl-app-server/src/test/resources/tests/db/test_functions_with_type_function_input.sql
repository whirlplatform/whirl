do
$$
declare
    v_input function_input;
begin
	 v_input.parameter_value := '{"code-b": "true", "code-d": "01.02.2024 13:21:32", "code-n": 123, "code-t": "some text", "code-l": "1;T;F;F:2;T;F;T", "code-c":"component value"}'::jsonb;
	 v_input.parameter_list_title := '{"code-l": "List title"}'::jsonb;
	 v_input.parameter_row_list := '{"code-b": "F","code-d": "F","code-n": "F","code-t": "F","code-l": "T","code-c": "F"}'::jsonb;
	 v_input.parameter_type := '{"code-b": "BOOLEAN", "code-d": "DATE", "code-n": "NUMBER", "code-t": "STRING",  "code-l": "LIST"}'::jsonb;
	 v_input.parameter_component := '{"code-c":"component"}'::jsonb;
	 v_input.parameter_index := '{"1": "code-b", "2": "code-d", "3": "code-n", "4": "code-t", "5": "code-l", "6": "code-c"}'::jsonb;
	 v_input.parameter_name := '{"code-b":"1", "code-d": "2", "code-n": "3", "code-t": "4", "code-l": "5", "code-c": "6" }'::jsonb;

	 -- test get_parameter_boolean --
	 ASSERT whirl.get_parameter_boolean(v_input, 'code-b') = true, 'Результата функции "get_parameter_boolean" не совпадает с ожидаемым значением';

	 -- test get_parameter_date --
	 ASSERT whirl.get_parameter_date(v_input, 'code-d' ) = '2024-02-01 13:21:32', 'Результата функции "get_parameter_date" не совпадает с ожидаемым значением';

	--- test get_parameter_number --
	 ASSERT whirl.get_parameter_number(v_input, 'code-n') = 123, 'Результата функции "get_parameter_number" не совпадает с ожидаемым значением';

	 -- test get_parameter_text --
	 ASSERT whirl.get_parameter_text(v_input, 'code-t') = 'some text', 'Результата функции "get_parameter_text" не совпадает с ожидаемым значением';


	-- test get_parameter_list_id --
	 ASSERT whirl.get_parameter_list_id(v_input, 'code-l') = '1;T;F;F:2;T;F;T', 'Результата функции "get_parameter_list_id" не совпадает с ожидаемым значением';

	--	 test parse_row_value --
      ASSERT  whirl.parse_row_value('1;T;F;F:2;F;F;T') = '{"(1,t,f,f)","(2,f,f,t)"}', 'Результата функции "parse_row_value" не совпадает с ожидаемым значением.';

	-- test get_parameter_codes --
	 ASSERT whirl.get_parameter_codes(v_input) = '{1,2,3,4,5,6}', 'Результата функции "get_parameter_codes" не совпадает с ожидаемым значением';

	 -- test get_parameter_count --
	 ASSERT whirl.get_parameters_count(v_input) = 6, 'Результата функции "get_parameter_count" не совпадает с ожидаемым значением';

end
$$;