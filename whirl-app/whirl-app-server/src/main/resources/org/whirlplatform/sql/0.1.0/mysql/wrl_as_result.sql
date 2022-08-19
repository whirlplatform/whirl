CREATE FUNCTION wrl_as_result(
    p_result JSON)
    RETURNS varchar(65535)
    LANGUAGE SQL
BEGIN
    RETURN JSON_INSERT(JSON_OBJECT(), '$.result', p_result);
END