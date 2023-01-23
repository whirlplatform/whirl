/**
 * @type function_result   Результат выполнения события
 */
CREATE TYPE whirl.function_result AS (
     title varchar(32767),
     message varchar(32767),
     message_type varchar(32767),
     next_event varchar(32767),
     parameter_value hstore,
     parameter_list_title hstore,
     parameter_type hstore,
     parameter_component hstore,
     parameter_index hstore)
;