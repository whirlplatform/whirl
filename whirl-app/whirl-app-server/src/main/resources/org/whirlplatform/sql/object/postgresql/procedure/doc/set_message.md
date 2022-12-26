# **SET_MESSAGE Function**
Устанавливает заголовок, текст и тип отображаемого пользователю сообщения

### Parameters
| Name           | Description                                                               |
|----------------|---------------------------------------------------------------------------|
| p_result       | Переменная результата выполнения события                                  |
| p_title        | Заголовок - строковая переменная, без ограничений                         |
| p_message      | Тело сообщения - строковая переменная, без ограничений                    |
| p_message_type | Тип сообщения - строковая переменная с заданным по умолчанию типом 'INFO' |
| *return*       | Переданную переменную с новыми параметрами                                |

### Syntax
     CREATE OR REPLACE FUNCTION SET_MESSAGE (
    p_result function_result,
    p_title varchar,
    p_message varchar,
    p_message_type varchar default 'INFO')
    RETURNS function_result
    AS
    $$
    BEGIN
    p_result.title := p_title;
    p_result.message := p_message;
    p_result.message_type := p_message_type;
    return p_result;
    END;
