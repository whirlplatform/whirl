/**
 * @type function_input   Входные данные вызванного события
 */
CREATE TYPE function_input AS (
    parameter_value jsonb,
    parameter_list_title jsonb,
    parameter_row_list jsonb,
    parameter_type jsonb,
    parameter_component jsonb,
    parameter_index jsonb,
    parameter_name jsonb)
;