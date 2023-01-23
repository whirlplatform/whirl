/**
 * @type function_input   Входные данные вызванного события
 */
CREATE TYPE whirl.function_input AS (
        parameter_value hstore,
        parameter_list_title hstore,
        parameter_row_list hstore,
        parameter_type hstore,
        parameter_component hstore,
        parameter_index hstore,
        parameter_name hstore)
;