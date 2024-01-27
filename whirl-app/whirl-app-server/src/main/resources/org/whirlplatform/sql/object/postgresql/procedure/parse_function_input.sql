/**
 * Парсит переданные данные вызванного события и заполняет поля переменной типа "function_input"
 *
 * @param p_input           Переменная типа "character" без ограничений - данные события
 *
 * @return Переменную типа "function_input" с заполненными полями
 */
CREATE OR REPLACE FUNCTION parse_function_input(p_input character varying)
 RETURNS function_input
 LANGUAGE plpgsql
AS $function$
declare
    v_result function_input;
    v_index  numeric;
   	v_rec    record;
begin
    v_index := 0;
    v_result.parameter_index := '{}'::jsonb;
    v_result.parameter_value := '{}'::jsonb;
    v_result.parameter_type := '{}'::jsonb;
    v_result.parameter_name := '{}'::jsonb;
    v_result.parameter_row_list := '{}'::jsonb;
    for v_rec in (select xml_table.*
	    from
 		xmltable ('///function-input/parameters/parameter' passing (p_input::xml)
			COLUMNS d_type varchar PATH '@type',
				d_code varchar path '@code',
				d_value varchar path 'text()',
				d_row_list varchar path '@rowlist'
					) as xml_table
	)

    loop
	    v_index := v_index +1;

        if  v_rec.d_code is not null then
            v_result.parameter_index := v_result.parameter_index || jsonb_build_object(v_index::varchar, v_rec.d_code);
            v_result.parameter_value := v_result.parameter_value || jsonb_build_object(v_rec.d_code, v_rec.d_value);
            v_result.parameter_type :=  v_result.parameter_type || jsonb_build_object(v_rec.d_code, v_rec.d_type);
            v_result.parameter_name := v_result.parameter_name || jsonb_build_object(v_rec.d_code, v_index::varchar);
            if v_rec.d_row_list = 'true' then
                v_result.parameter_row_list := v_result.parameter_row_list || jsonb_build_object(v_rec.d_code, 'T');
            else
                v_result.parameter_row_list := v_result.parameter_row_list || jsonb_build_object(v_rec.d_code, 'F');
            end if;
        end if;
    end loop;

    return v_result;
END;
$function$
;