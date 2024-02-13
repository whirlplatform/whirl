/**
 * @type function_result   Результат выполнения события
 */
CREATE TYPE function_result AS (
     title varchar(32767),
     message varchar(32767),
     message_type varchar(32767),
     next_event varchar(32767),
     parameter_value jsonb,
     parameter_list_title jsonb,
     parameter_type jsonb,
     parameter_component jsonb,
     parameter_index jsonb)
;
